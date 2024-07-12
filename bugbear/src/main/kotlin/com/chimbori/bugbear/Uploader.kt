package com.chimbori.bugbear

import android.content.Context
import android.util.Base64.NO_WRAP
import android.util.Base64.encodeToString
import android.util.Log
import androidx.work.BackoffPolicy.EXPONENTIAL
import androidx.work.Constraints
import androidx.work.ListenableWorker.Result.failure
import androidx.work.ListenableWorker.Result.retry
import androidx.work.ListenableWorker.Result.success
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest.Companion.MIN_BACKOFF_MILLIS
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chimbori.bugbear.BuildConfig.VERSION_NAME
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.TimeUnit.MILLISECONDS

internal fun uploadReports(workManager: WorkManager) {
  Log.e(TAG, "Queuing up ${bugBear.store.list().size} reports for upload")
  val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
    .setConstraints(
      Constraints.Builder()
        .setRequiredNetworkType(CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()
    )
    .setBackoffCriteria(EXPONENTIAL, MIN_BACKOFF_MILLIS, MILLISECONDS)
    .build()
  workManager.enqueue(uploadWorkRequest)
}

public class UploadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
  override fun doWork(): Result = try {
    val uploadUrl = bugBear.config?.uploadUrl
    when {
      uploadUrl.isNullOrBlank() -> {
        Log.e(TAG, "uploadUrl not provided")
        failure()
      }
      uploadPendingReports(URL(uploadUrl), bugBear.store) -> success()
      else -> retry()
    }
  } catch (t: Throwable) {
    Log.e(TAG, "Upload failed: ${t.stackTraceToString()}")
    failure()
  }
}

private fun uploadReport(uploadUrl: URL, file: File): Boolean {
  val httpURLConnection = uploadUrl.openConnection() as HttpURLConnection

  uploadUrl.userInfo?.nullIfBlank()?.let { userInfo ->
    httpURLConnection.setRequestProperty(
      "Authorization", "Basic ${encodeToString(userInfo.toByteArray(UTF_8), NO_WRAP)}"
    )
  }

  val responseCode: Int
  try {
    httpURLConnection.apply {
      setRequestProperty("Content-Type", "application/json")
      setRequestProperty("User-Agent", "BugBear $VERSION_NAME")
      doOutput = true
      outputStream.bufferedWriter().use {
        it.write(file.readText())
      }

      responseCode = httpURLConnection.responseCode

      if (responseCode != 200) {
        Log.e(TAG, "Upload failed: ${uploadUrl.host} responded with HTTP $responseCode")
        val outputStream = if (responseCode >= 400) inputStream else errorStream
        Log.e(TAG, outputStream.bufferedReader().readText())
      }
    }
  } finally {
    httpURLConnection.disconnect()
  }
  return responseCode == 200
}

private fun uploadPendingReports(uploadUrl: URL, store: ReportStore): Boolean {
  val reportFiles = store.list()
  Log.i(TAG, "${reportFiles.size} reports to upload")
  return reportFiles.all { reportFile ->
    Log.i(TAG, "Uploading ${reportFile.name}")
    uploadReport(uploadUrl, reportFile).also { uploadedSuccessfully ->
      if (uploadedSuccessfully) {
        reportFile.delete()
      }
    }
  }
}

private const val TAG = "BugBear.Uploader"
