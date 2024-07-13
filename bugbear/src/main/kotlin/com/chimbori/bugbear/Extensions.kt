package com.chimbori.bugbear

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

internal fun String?.nullIfBlank() = this?.ifBlank { null }

internal fun Long.offsetDateTime(): OffsetDateTime =
  OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

internal fun OffsetDateTime.toIso8601(): String = format(ISO_OFFSET_DATE_TIME)

internal fun OffsetDateTime.yyyymmddhhmmss(): String = format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss"))

/** Creates a subdirectory under the given path and creates it if it doesn’t already exist. */
internal fun File.subDir(subDirectory: String) = File(this, subDirectory).also { it.mkdirs() }

internal fun withDelay(delayMs: Long = 0, block: () -> Unit) {
  Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed(Runnable(block), delayMs)
}

internal fun String.md5(): String =
  BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16).padStart(32, '0')

/**
 * [tag] is of type [Any] so we can avoid calling [.toString()] on it unless required.
 * If [tag] & [method] are both null, nothing will be logged. Any exceptions thrown will be squelched silently.
 */
internal inline fun <T> catchAll(
  tag: Any? = null,
  method: String? = null,
  noinline message: (() -> String)? = null,
  block: () -> T
): T? = try {
  block()
} catch (throwable: Throwable) {
  if (tag != null && method != null) {
    Log.e(tag.toString(), "$method: $message")
    throwable.printStackTrace()
  }
  if (throwable is UninitializedPropertyAccessException) {
    throw throwable  // Crash because this is a developer error.
  }
  null
}
