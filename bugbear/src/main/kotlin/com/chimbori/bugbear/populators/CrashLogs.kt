package com.chimbori.bugbear.populators

import android.util.Log
import com.chimbori.bugbear.Populator
import com.chimbori.bugbear.Report
import com.chimbori.bugbear.offsetDateTime
import com.chimbori.bugbear.toIso8601

internal class Crash : Populator {
  override fun populate(report: Report) = report.apply {
    report.throwable?.let {
      stackTrace = it.stackTraceToString()
      stackTraceHash = it.stackTraceHash()
      userCrashDate = System.currentTimeMillis().offsetDateTime().toIso8601()
    }
  }
}

internal class LogCat : Populator {
  override fun populate(report: Report) = report.apply {
    val process: Process? = try {
      ProcessBuilder()
        .command("logcat -t 100 -v time *:V".split(" "))
        .redirectErrorStream(true)
        .start()
    } catch (t: Throwable) {
      Log.e(TAG, "Failed: ${t.stackTraceToString()}")
      null
    }
    report.logcat = try {
      process?.inputStream?.bufferedReader().use { it?.readText() }
    } finally {
      process?.destroy()
    }
  }
}

private fun Throwable?.stackTraceHash(): String {
  val stringified = StringBuilder()
  var cause = this
  while (cause != null) {
    val stackTraceElements = cause.stackTrace
    for (e in stackTraceElements) {
      stringified.append(e.className)
      stringified.append(e.methodName)
    }
    cause = cause.cause
  }
  return Integer.toHexString(stringified.toString().hashCode())
}

private const val TAG = "BugBear.CrashLogs"
