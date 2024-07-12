package com.chimbori.bugbear.populators

import android.content.Context
import com.chimbori.bugbear.Populator

public fun createDefaultPopulators(context: Context): List<Populator> = listOf(
  // App Related
  Startup(),
  AppPackageInfo(context),

  // User Related
  UserInfo(),
  OptionalInfo(),

  // Device Related
  DeviceInfo(),
  DisplayInfo(context),
  ResourceConfigurationInfo(context),

  // Crash Related
  Crash(),
  LogCat(),
)
