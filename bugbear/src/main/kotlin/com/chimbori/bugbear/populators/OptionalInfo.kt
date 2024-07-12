package com.chimbori.bugbear.populators

import com.chimbori.bugbear.Populator
import com.chimbori.bugbear.Report

public class OptionalInfo : Populator {
  private val customData = mutableMapOf<String, String>()
  private val sharedPreferences = mutableMapOf<String, String>()
  private val deviceFeatures = mutableMapOf<String, Boolean>()

  public fun logCustomData(key: String, value: String?) {
    value?.let { customData[key] = it }
  }

  public fun logSharedPreference(key: String, value: String?) {
    value?.let { sharedPreferences[key] = it }
  }

  public fun logDeviceFeature(feature: String, isAvailable: Boolean) {
    deviceFeatures[feature] = isAvailable
  }

  override fun populate(report: Report): Report = report.apply {
    // Merge into existing keys, but don’t clear out any existing ones.
    customData.putAll(customData.filterValues(String::isNotBlank))
    sharedPreferences.putAll(sharedPreferences.filterValues(String::isNotBlank))
    deviceFeatures.putAll(deviceFeatures)
  }
}
