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

  internal var store = ReportStore(reportDir = appContext.cacheDir.subDir("crash-reports"))
    private set

  init {
    if (hostedConfigUrl != null && config == null) {
      runBlocking {
        config = fetchHostedConfig(hostedConfigUrl)?.findConfig(context.packageName)
      }
    }

    if (!config?.uploadUrl.isNullOrBlank()) {
      setDefaultUncaughtExceptionHandler { thread, throwable ->
        Log.e(TAG, "Caught ${throwable.javaClass.name} for ${appContext.packageName}")
        store.write(generateReport(throwable, uncaught = true))
        alsoForwardTo?.uncaughtException(thread, throwable)
        ProcessPhoenix.triggerRebirth(appContext)
      }
      withDelay(3_000) {
        uploadReports()
      }
      Log.i(TAG, "BugBear initialized; ${config?.uploadUrl}")
    } else {
      Log.e(TAG, "BugBear not initialized; `config` is null, and no matching `HostedConfig` found.")
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
      bugBear.config?.uploadUrl.isNullOrBlank() -> {
        Log.e(TAG, "Upload failed; `uploadUrl` not provided in `config`.")
        return
      }
      store.list().isEmpty() -> return
      else -> uploadReports(workManager = workManager)
    }
  }
}

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

private const val TAG = "BugBear"
