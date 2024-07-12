package com.chimbori.bugbear.populators

import android.content.Context
import android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE
import com.chimbori.bugbear.Populator
import com.chimbori.bugbear.Report
import com.chimbori.bugbear.offsetDateTime
import com.chimbori.bugbear.toIso8601

internal class AppPackageInfo(private val context: Context) : Populator {
  private val packageInfo by lazy {
    try {
      context.packageManager?.getPackageInfo(context.packageName, 0)
    } catch (t: Throwable) {
      null
    }
  }

  private val applicationInfo by lazy {
    try {
      context.packageManager?.getApplicationInfo(context.packageName, 0)
    } catch (t: Throwable) {
      null
    }
  }

  override fun populate(report: Report) = report.apply {
    appVersionCode = packageInfo?.longVersionCode
    appVersionName = packageInfo?.versionName
    packageName = packageInfo?.packageName
    filePath = context.filesDir.absolutePath

    if (build == null) {
      build = Report.Build()
    }
    build?.apply {
      isDebuggable = (applicationInfo?.flags ?: 0) and FLAG_DEBUGGABLE > 0
    }
  }
}

internal class Startup : Populator {
  private val instantiatedAtMs = System.currentTimeMillis()

  override fun populate(report: Report) = report.apply {
    if (report.userAppStartDate == null) {
      report.userAppStartDate = instantiatedAtMs.offsetDateTime().toIso8601()
    }
  }
}
