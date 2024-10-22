package com.chimbori.bugbear

import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import com.chimbori.bugbear.populators.createDefaultPopulators
import com.jakewharton.processphoenix.ProcessPhoenix
import java.lang.Thread.UncaughtExceptionHandler
import java.lang.Thread.setDefaultUncaughtExceptionHandler
import kotlin.reflect.KClass
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

public lateinit var bugBear: BugBear

/**
 * This class is the public entry-point to BugBear. To initialize BugBear, create an instance of this class and assign
 * it to the `lateinit var` [bugBear] which serves as a long-lived static variable used throughout the app.
 *
 * @param config Pass a configuration to initialize BugBear at startup, or set this to `null` if using a [hostedConfigUrl] instead.
 * @param hostedConfigUrl BugBear can fetch its own config from a remote server at runtime; set this to a JSON URL to enable.
 * @param populators To add, remove, or customize the set of [Populator]s used by BugBear, provide a list at initialization.
 */
public class BugBear(
  context: Context,
  internal var config: Config?,
  internal val hostedConfigUrl: String? = null,
  private val populators: List<Populator> = createDefaultPopulators(context.applicationContext),
  private val alsoForwardTo: UncaughtExceptionHandler? = null,
) {
  private val appContext = context.applicationContext
  private val workManager by lazy { WorkManager.getInstance(appContext) }

  private val cacheDir = appContext.cacheDir.subDir("crash-reports")
  internal var store = ReportStore(reportDir = cacheDir)
    private set

  init {
    if (hostedConfigUrl != null && config == null) {
      runBlocking {
        config = CachedTextFile(hostedConfigUrl, cacheDir).fetch()?.let {
          Json.decodeFromString<HostedConfig>(it).findConfig(context.packageName)
        }
      }
    }

    if (config?.isAnyDestinationEnabled == true) {
      setDefaultUncaughtExceptionHandler { thread, throwable ->
        Log.e(TAG, "Caught ${throwable.javaClass.name} for ${appContext.packageName}")
        store.write(generateReport(throwable, uncaught = true))
        alsoForwardTo?.uncaughtException(thread, throwable)
        ProcessPhoenix.triggerRebirth(appContext)
      }
      withDelay(3_000) {
        uploadReports()
      }
      Log.i(TAG, "BugBear initialized.")
    } else {
      Log.e(TAG, "BugBear not initialized; no destinations are enabled, no matching `HostedConfig` found.")
    }
  }

  @Suppress("UNCHECKED_CAST")
  public operator fun <T : Populator> get(clazz: KClass<T>): T = populators.first { it::class == clazz } as T

  public fun report(throwable: Throwable) {
    Log.e(TAG, "Logged ${throwable.javaClass.name} for ${appContext.packageName}")
    store.write(generateReport(throwable, uncaught = false))
    uploadReports()
  }

  public fun generateReport(t: Throwable? = null, uncaught: Boolean = false): Report {
    Log.i(TAG, "Generated a report for ${appContext.packageName}")
    return populators.fold(Report(throwable = t, isSilent = !uncaught)) { report, populator ->
      populator.populate(report)
    }
  }

  public fun uploadReports() {
    when {
      config?.isAnyDestinationEnabled != true -> {
        Log.e(TAG, "Upload: no destinations enabled.")
        return
      }
      store.list().isEmpty() -> {
        Log.e(TAG, "Upload: No reports to upload.")
        return
      }
      else -> uploadReports(workManager = workManager)
    }
  }
}

@Serializable
public data class Config(
  /**
   * Each BugBear instance can be configured to upload to multiple destinations. This can be useful when migrating
   * from one destination to another, or for redundancy.
   */
  val destinations: Collection<Destination>,
) {
  val isAnyDestinationEnabled: Boolean = destinations.any { it.enabled }
}

@Serializable
public data class Destination(
  /**
   * Can include all components of a typical URL, i.e. username, password, host, port, and path.
   * E.g. https://username:password@host:port/path/endpoint
   */
  val uploadUrl: String,

  /** Each destination can be enabled or disabled individually. */
  val enabled: Boolean = true,
)

public fun interface Populator {
  public fun populate(report: Report): Report
}

private const val TAG = "BugBear"
