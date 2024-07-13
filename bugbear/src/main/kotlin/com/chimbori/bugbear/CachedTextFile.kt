package com.chimbori.bugbear

import android.util.Log
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit.DAYS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Returns to the caller the contents of a remote URL, from one of three stores.
 * 1. if previously requested in this session, then it’s read from an in-memory cache;
 * 2. if not in memory, but was previously fetched from a remote URL, then it’s read from a disk cache;
 * 3. if not cached on disk, then it is fetched from the remote URL (and cached on disk and in memory for future use).
 **/
internal class CachedTextFile(
  private val url: String,
  cacheDir: File,
  private val expirationMs: Long = DEFAULT_EXPIRATION_MS
) {
  private val fetchMutex = Mutex()
  private var inMemoryCache: String? = null
  private val diskCacheFile = File(cacheDir, url.md5())

  /**
   * If this requires a network fetch, then the fetch must be synchronized to avoid multiple threads fetching it at the
   * same time and overwriting each others’ partially downloaded files. Since the disk write happens in [#fetch] and
   * not [#fetchFromNetwork], this entire method is marked synchronized.
   */
  internal suspend fun fetch(): String? {
    Log.e(TAG, "fetch: url: $url; diskCacheFile: ${diskCacheFile.path}")
    try {
      fetchMutex.withLock {
        return inMemoryCache
          ?: readFromDiskCache().also {
            it?.let { fromDiskCache ->
              inMemoryCache = fromDiskCache
            }
          }
          ?: fetchFromNetwork().also {
            it?.let { fromNetwork ->
              inMemoryCache = fromNetwork
              withContext(Dispatchers.IO) {
                diskCacheFile.parentFile?.mkdirs()
                diskCacheFile.writeText(fromNetwork)
              }
            }
          }
      }
    } catch (e: Throwable) {
      e.printStackTrace()
      return null
    }
  }

  /** @return null if cached file is absent, expired, or could not be read. */
  private suspend fun readFromDiskCache(): String? = withContext(Dispatchers.IO) {
    if (diskCacheFile.exists()) {
      Log.i(TAG, "readFromDiskCache: diskCacheFile.exists()")
      val lastModifiedTimeMs = catchAll(TAG, "readFromDiskCache") {
        diskCacheFile.lastModified()
      }
        ?: System.currentTimeMillis()

      val diskCacheFileExpired = lastModifiedTimeMs < System.currentTimeMillis() - expirationMs
      if (diskCacheFileExpired) {
        return@withContext null
      }

      catchAll(TAG, "readFromDiskCache", { "diskCacheFile: ${diskCacheFile.path}" }) {
        diskCacheFile.readText()
      }
    } else {
      Log.i(TAG, "readFromDiskCache: diskCacheFile missing!")
      null
    }
  }

  private suspend fun fetchFromNetwork(): String? = withContext(Dispatchers.IO) {
    Log.i(TAG, "fetchFromNetwork: url: $url")
    if (!isNetworkConnected()) {
      return@withContext null
    }
    val url = URL(url)
    (url.openConnection() as HttpURLConnection).run {
      if (responseCode == 200) {
        return@withContext inputStream.bufferedReader().readText()
      } else {
        Log.e(TAG, "fetchFromNetwork: failed: ${url.host} responded with HTTP $responseCode")
        return@withContext null
      }
    }
  }

  private fun isNetworkConnected(): Boolean {
    // TODO: Implement this for real
    return true
  }

  internal suspend fun clearCached() {
    Log.i(TAG, "clearCached: url: $url")
    inMemoryCache = null
    withContext(Dispatchers.IO) {
      diskCacheFile.deleteRecursively()
    }
  }

  companion object {
    private const val TAG = "CachedTextFile"

    /** Default expiration time for [CachedTextFile]. Shorter than one week for quicker updates. */
    private val DEFAULT_EXPIRATION_MS = DAYS.toMillis(3)
  }
}
