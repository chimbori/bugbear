package com.chimbori.bugbear

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
public data class Report(
  @SerialName("REPORT_ID") var reportId: String? = null,
  @SerialName("INSTALLATION_ID") var installationId: String? = null,
  @SerialName("USER_EMAIL") var userEmail: String? = null,
  @SerialName("USER_COMMENT") var userComment: String? = null,

  @SerialName("APP_VERSION_CODE") var appVersionCode: Long? = null,
  @SerialName("APP_VERSION_NAME") var appVersionName: String? = null,
  @SerialName("PACKAGE_NAME") var packageName: String? = null,
  @SerialName("FILE_PATH") var filePath: String? = null,

  @SerialName("USER_APP_START_DATE") var userAppStartDate: String? = null,

  @SerialName("BRAND") var brand: String? = null,
  @SerialName("PHONE_MODEL") var phoneModel: String? = null,
  @SerialName("PRODUCT") var product: String? = null,
  @SerialName("ANDROID_VERSION") var androidVersion: String? = null,
  @SerialName("BUILD") var build: Build? = null,

  @Transient val throwable: Throwable? = null,
  @SerialName("IS_SILENT") var isSilent: Boolean? = null,
  @SerialName("STACK_TRACE") var stackTrace: String? = null,
  @SerialName("STACK_TRACE_HASH") var stackTraceHash: String? = null,
  @SerialName("USER_CRASH_DATE") var userCrashDate: String? = null,

  @SerialName("LOGCAT") var logcat: String? = null,

  @SerialName("INITIAL_CONFIGURATION") var initialConfiguration: ResourceConfiguration? = null,
  @SerialName("CRASH_CONFIGURATION") var crashConfiguration: ResourceConfiguration? = null,

  @SerialName("DISPLAY") var display: Displays? = null,

  @SerialName("CUSTOM_DATA") var customData: MutableMap<String, String> = mutableMapOf(),
  @SerialName("DEVICE_FEATURES") var deviceFeatures: MutableMap<String, Boolean> = mutableMapOf(),
  @SerialName("SHARED_PREFERENCES") var sharedPreferences: MutableMap<String, String> = mutableMapOf(),
) {
  @Serializable
  public data class Build(
    @SerialName("IS_DEBUGGABLE") var isDebuggable: Boolean? = null,

    @SerialName("BOARD") var board: String? = null,
    @SerialName("BOOTLOADER") var bootloader: String? = null,
    @SerialName("BRAND") var brand: String? = null,
    @SerialName("DEVICE") var device: String? = null,
    @SerialName("DISPLAY") var display: String? = null,
    @SerialName("FINGERPRINT") var fingerprint: String? = null,
    @SerialName("HARDWARE") var hardware: String? = null,
    @SerialName("HOST") var host: String? = null,
    @SerialName("ID") var id: String? = null,
    @SerialName("MANUFACTURER") var manufacturer: String? = null,
    @SerialName("MODEL") var model: String? = null,
    @SerialName("ODM_SKU") var odmSku: String? = null,
    @SerialName("PRODUCT") var product: String? = null,
    @SerialName("RADIO") var radio: String? = null,
    @SerialName("SKU") var sku: String? = null,
    @SerialName("SOC_MANUFACTURER") var socManufacturer: String? = null,
    @SerialName("SOC_MODEL") var socModel: String? = null,
    @SerialName("SUPPORTED_32_BIT_ABIS") var supported32BitAbis: List<String>? = null,
    @SerialName("SUPPORTED_64_BIT_ABIS") var supported64BitAbis: List<String>? = null,
    @SerialName("SUPPORTED_ABIS") var supportedAbis: List<String>? = null,
    @SerialName("TAGS") var tags: String? = null,
    @SerialName("TIME") var time: Long? = null,
    @SerialName("TYPE") var type: String? = null,
    @SerialName("USER") var user: String? = null,
    @SerialName("VERSION") var version: Version? = null,
  ) {
    @Serializable
    public data class Version(
      @SerialName("INCREMENTAL") var incremental: String? = null,
      @SerialName("MEDIA_PERFORMANCE_CLASS") var mediaPerformanceClass: Int? = null,
      @SerialName("PREVIEW_SDK_INT") var previewSdkInt: Int? = null,
      @SerialName("RELEASE") var release: String? = null,
      @SerialName("SDK_INT") var sdkInt: Int? = null,
      @SerialName("SECURITY_PATCH") var securityPatch: String? = null,
    )
  }

  @Serializable
  public data class ResourceConfiguration(
    @SerialName("colorMode") var colorMode: Int? = null,
    @SerialName("densityDpi") var densityDpi: Int? = null,
    @SerialName("fontScale") var fontScale: Float? = null,
    @SerialName("fontWeightAdjustment") var fontWeightAdjustment: Int? = null,
    @SerialName("hardKeyboardHidden") var hardKeyboardHidden: String? = null,
    @SerialName("keyboard") var keyboard: String? = null,
    @SerialName("keyboardHidden") var keyboardHidden: String? = null,
    @SerialName("locale") var locale: String? = null,
    @SerialName("locales") var locales: List<String>? = null,
    @SerialName("mcc") var mcc: Int? = null,
    @SerialName("mnc") var mnc: Int? = null,
    @SerialName("navigation") var navigation: String? = null,
    @SerialName("navigationHidden") var navigationHidden: String? = null,
    @SerialName("orientation") var orientation: String? = null,
    @SerialName("screenHeightDp") var screenHeightDp: Int? = null,
    @SerialName("screenLayout") var screenLayout: String? = null,
    @SerialName("screenWidthDp") var screenWidthDp: Int? = null,
    @SerialName("smallestScreenWidthDp") var smallestScreenWidthDp: Int? = null,
    @SerialName("touchscreen") var touchscreen: String? = null,
    @SerialName("uiMode") var uiMode: String? = null,
  )

  @Serializable
  public data class Displays(
    @SerialName("0") var x0: Display? = null,
    @SerialName("1") var x1: Display? = null,
    @SerialName("2") var x2: Display? = null,
    @SerialName("3") var x3: Display? = null,
  ) {
    @Serializable
    public data class Display(
      @SerialName("currentSizeRange") var currentSizeRange: CurrentSizeRange? = null,
      @SerialName("flags") var flags: String? = null,
      @SerialName("metrics") var metrics: DisplayMetrics? = null,
      @SerialName("realMetrics") var realMetrics: DisplayMetrics? = null,
      @SerialName("name") var name: String? = null,
      @SerialName("realSize") var realSize: List<Int>? = null,
      @SerialName("rectSize") var rectSize: List<Int>? = null,
      @SerialName("size") var size: List<Int>? = null,
      @SerialName("isValid") var isValid: Boolean? = null,

      @SerialName("rotation") var rotation: String? = null,
      @SerialName("orientation") var orientation: Int? = null,

      @SerialName("refreshRate") var refreshRate: Float? = null,
      @SerialName("height") var height: Int? = null,
      @SerialName("width") var width: Int? = null,
      @SerialName("pixelFormat") var pixelFormat: Int? = null,
    ) {
      @Serializable
      public data class CurrentSizeRange(
        @SerialName("smallest") var smallest: List<Int>? = null,
        @SerialName("largest") var largest: List<Int>? = null,
      )

      @Serializable
      public data class DisplayMetrics(
        @SerialName("density") var density: Float? = null,
        @SerialName("xdpi") var xdpi: Float? = null,
        @SerialName("ydpi") var ydpi: Float? = null,
        @SerialName("densityDpi") var densityDpi: Int? = null,
        @SerialName("scaledDensity") var scaledDensity: String? = null,
        @SerialName("widthPixels") var widthPixels: Int? = null,
        @SerialName("heightPixels") var heightPixels: Int? = null,
      )
    }
  }
}

public fun Report.toJson(): String = Json.encodeToString(this)
