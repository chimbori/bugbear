package com.chimbori.bugbear

import android.os.Handler
import android.os.Looper
import java.io.File
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
