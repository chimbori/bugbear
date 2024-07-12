package com.chimbori.bugbear

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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

public suspend fun fetchHostedConfig(hostedConfigUrl: String): HostedConfig? = withContext(Dispatchers.IO) {
  try {
    val url = URL(hostedConfigUrl)
    (url.openConnection() as HttpURLConnection).run {
      if (responseCode == 200) {
        try {
          return@withContext Json.decodeFromString<HostedConfig>(inputStream.bufferedReader().readText())
        } catch (t: Throwable) {
          Log.e(TAG, "fetchHostedConfig: failed to parse JSON from $hostedConfigUrl")
          t.printStackTrace()
          return@withContext null
        }
      } else {
        Log.e(TAG, "fetchHostedConfig: failed: ${url.host} responded with HTTP $responseCode")
        return@withContext null
      }
    }
  } catch (t: Throwable) {
    t.printStackTrace()
    return@withContext null
  }
}

internal fun HostedConfig.findConfig(packageName: String): Config? =
  apps.firstOrNull { it.packageName == packageName }?.config

private const val TAG = "BugBear.HostedConfig"
