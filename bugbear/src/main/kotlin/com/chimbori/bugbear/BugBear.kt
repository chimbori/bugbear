package com.chimbori.bugbear

import kotlinx.serialization.Serializable

@Serializable
public data class Config(
  /**
   * Can include all components of a typical URL, i.e. username, password, host, port, and path.
   * E.g. https://username:password@host:port/path/endpoint
   *
   * Setting this to `null` will disable BugBear entirely.
   */
  val uploadUrl: String?,
)

public fun interface Populator {
  public fun populate(report: Report): Report
}
