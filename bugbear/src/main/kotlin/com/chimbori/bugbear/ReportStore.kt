package com.chimbori.bugbear

import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class ReportStore(private val reportDir: File) {
  fun write(report: Report) {
    report.file().writeText(Json.encodeToString(report))
  }

  fun list(): List<File> =
    reportDir.listFiles { file -> file.isFile && file.extension == REPORT_FILE_EXTENSION }?.toList() ?: emptyList()

  private fun Report.file() = File(reportDir, fileName())

  companion object {
    private const val REPORT_FILE_EXTENSION = "stacktrace"

    private fun Report.fileName(): String {
      val crashTimeStamp = System.currentTimeMillis().offsetDateTime()
      if (userCrashDate.isNullOrBlank()) {
        userCrashDate = crashTimeStamp.toIso8601()
      }
      return "${crashTimeStamp.yyyymmddhhmmss()}.$REPORT_FILE_EXTENSION"
    }
  }
}
