package com.chimbori.bugbear

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * BugBear can download its own configuration from a remote server at runtime, in addition to being statically
 * configured within the app source code. This allows developers to change the reporting endpoint (or disable
 * reporting completely) even after the client app binary has been deployed and distributed.
 */
@Serializable
public data class HostedConfig(
  /** Each config file can contain the configuration for multiple apps. */
  @SerialName("apps") val apps: List<AppConfig>,
) {
  @Serializable
  public data class AppConfig(
    /** App’s package name for this config. */
    @SerialName("package_name") val packageName: String,
    /** The config to use for this app. Set this to `null` to disable crash reporting & uploads. */
    @SerialName("config") val config: Config?,
  )
}

internal fun HostedConfig.findConfig(packageName: String): Config? =
  apps.firstOrNull { it.packageName == packageName }?.config
