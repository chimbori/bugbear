package com.chimbori.bugbear.populators

import com.chimbori.bugbear.Populator
import com.chimbori.bugbear.Report
import com.chimbori.bugbear.nullIfBlank
import java.util.UUID

public class UserInfo : Populator {
  public var installationId: String? = null
  public var userEmail: String? = null
  public var userComment: String? = null

  override fun populate(report: Report): Report {
    // Only overwrite existing values in [Report] if new values are non-null & non-blank.
    if (report.reportId == null) {
      report.reportId = UUID.randomUUID().toString()
    }
    installationId?.nullIfBlank()?.let { report.installationId = it }
    userEmail?.nullIfBlank()?.let { report.userEmail = it }
    userComment?.nullIfBlank()?.let { report.userComment = it }
    return report
  }
}
