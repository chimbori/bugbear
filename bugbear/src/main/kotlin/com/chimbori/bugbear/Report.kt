package com.chimbori.bugbear

import com.chimbori.bugbear.populators.AppPackageInfo
import com.chimbori.bugbear.populators.Crash
import com.chimbori.bugbear.populators.DeviceInfo
import com.chimbori.bugbear.populators.DisplayInfo
import com.chimbori.bugbear.populators.LogCat
import com.chimbori.bugbear.populators.OptionalInfo
import com.chimbori.bugbear.populators.ResourceConfigurationInfo
import com.chimbori.bugbear.populators.Startup
import com.chimbori.bugbear.populators.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
public data class Report(
  @PopulatedBy(UserInfo::class) @SerialName("REPORT_ID") var reportId: String? = null,
  @PopulatedBy(UserInfo::class) @SerialName("INSTALLATION_ID") var installationId: String? = null,
  @PopulatedBy(UserInfo::class) @SerialName("USER_EMAIL") var userEmail: String? = null,
  @PopulatedBy(UserInfo::class) @SerialName("USER_COMMENT") var userComment: String? = null,

  @PopulatedBy(AppPackageInfo::class) @SerialName("APP_VERSION_CODE") var appVersionCode: Long? = null,
  @PopulatedBy(AppPackageInfo::class) @SerialName("APP_VERSION_NAME") var appVersionName: String? = null,
  @PopulatedBy(AppPackageInfo::class) @SerialName("PACKAGE_NAME") var packageName: String? = null,
  @PopulatedBy(AppPackageInfo::class) @SerialName("FILE_PATH") var filePath: String? = null,

  @PopulatedBy(Startup::class) @SerialName("USER_APP_START_DATE") var userAppStartDate: String? = null,

  @PopulatedBy(DeviceInfo::class) @SerialName("BRAND") var brand: String? = null,
  @PopulatedBy(DeviceInfo::class) @SerialName("PHONE_MODEL") var phoneModel: String? = null,
  @PopulatedBy(DeviceInfo::class) @SerialName("PRODUCT") var product: String? = null,
  @PopulatedBy(DeviceInfo::class) @SerialName("ANDROID_VERSION") var androidVersion: String? = null,
  @PopulatedBy(DeviceInfo::class) @SerialName("BUILD") var build: Build? = null,

  @Transient val throwable: Throwable? = null,
  @PopulatedBy(Crash::class) @SerialName("IS_SILENT") var isSilent: Boolean? = null,
  @PopulatedBy(Crash::class) @SerialName("STACK_TRACE") var stackTrace: String? = null,
  @PopulatedBy(Crash::class) @SerialName("STACK_TRACE_HASH") var stackTraceHash: String? = null,
  @PopulatedBy(Crash::class) @SerialName("USER_CRASH_DATE") var userCrashDate: String? = null,

  @PopulatedBy(LogCat::class) @SerialName("LOGCAT") var logcat: String? = null,

  @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("INITIAL_CONFIGURATION") var initialConfiguration: ResourceConfiguration? = null,
  @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("CRASH_CONFIGURATION") var crashConfiguration: ResourceConfiguration? = null,

  @PopulatedBy(DisplayInfo::class) @SerialName("DISPLAY") var display: Displays? = null,

  @PopulatedBy(OptionalInfo::class) @SerialName("CUSTOM_DATA") var customData: MutableMap<String, String> = mutableMapOf(),
  @PopulatedBy(OptionalInfo::class) @SerialName("DEVICE_FEATURES") var deviceFeatures: MutableMap<String, Boolean> = mutableMapOf(),
  @PopulatedBy(OptionalInfo::class) @SerialName("SHARED_PREFERENCES") var sharedPreferences: MutableMap<String, String> = mutableMapOf(),
) {
  @Serializable
  public data class Build(
    @PopulatedBy(AppPackageInfo::class) @SerialName("IS_DEBUGGABLE") var isDebuggable: Boolean? = null,

    @PopulatedBy(DeviceInfo::class) @SerialName("BOARD") var board: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("BOOTLOADER") var bootloader: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("BRAND") var brand: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("DEVICE") var device: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("DISPLAY") var display: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("FINGERPRINT") var fingerprint: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("HARDWARE") var hardware: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("HOST") var host: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("ID") var id: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("MANUFACTURER") var manufacturer: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("MODEL") var model: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("ODM_SKU") var odmSku: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("PRODUCT") var product: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("RADIO") var radio: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("SKU") var sku: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("SOC_MANUFACTURER") var socManufacturer: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("SOC_MODEL") var socModel: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("SUPPORTED_32_BIT_ABIS") var supported32BitAbis: List<String>? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("SUPPORTED_64_BIT_ABIS") var supported64BitAbis: List<String>? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("SUPPORTED_ABIS") var supportedAbis: List<String>? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("TAGS") var tags: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("TIME") var time: Long? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("TYPE") var type: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("USER") var user: String? = null,
    @PopulatedBy(DeviceInfo::class) @SerialName("VERSION") var version: Version? = null,
  ) {
    @Serializable
    public data class Version(
      @PopulatedBy(DeviceInfo::class) @SerialName("INCREMENTAL") var incremental: String? = null,
      @PopulatedBy(DeviceInfo::class) @SerialName("MEDIA_PERFORMANCE_CLASS") var mediaPerformanceClass: Int? = null,
      @PopulatedBy(DeviceInfo::class) @SerialName("PREVIEW_SDK_INT") var previewSdkInt: Int? = null,
      @PopulatedBy(DeviceInfo::class) @SerialName("RELEASE") var release: String? = null,
      @PopulatedBy(DeviceInfo::class) @SerialName("SDK_INT") var sdkInt: Int? = null,
      @PopulatedBy(DeviceInfo::class) @SerialName("SECURITY_PATCH") var securityPatch: String? = null,
    )
  }

  @Serializable
  public data class ResourceConfiguration(
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("colorMode") var colorMode: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("densityDpi") var densityDpi: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("fontScale") var fontScale: Float? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("fontWeightAdjustment") var fontWeightAdjustment: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("hardKeyboardHidden") var hardKeyboardHidden: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("keyboard") var keyboard: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("keyboardHidden") var keyboardHidden: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("locale") var locale: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("locales") var locales: List<String>? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("mcc") var mcc: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("mnc") var mnc: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("navigation") var navigation: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("navigationHidden") var navigationHidden: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("orientation") var orientation: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("screenHeightDp") var screenHeightDp: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("screenLayout") var screenLayout: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("screenWidthDp") var screenWidthDp: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("smallestScreenWidthDp") var smallestScreenWidthDp: Int? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("touchscreen") var touchscreen: String? = null,
    @PopulatedBy(ResourceConfigurationInfo::class) @SerialName("uiMode") var uiMode: String? = null,
  )

  @Serializable
  public data class Displays(
    @PopulatedBy(DisplayInfo::class) @SerialName("0") var x0: Display? = null,
    @PopulatedBy(DisplayInfo::class) @SerialName("1") var x1: Display? = null,
    @PopulatedBy(DisplayInfo::class) @SerialName("2") var x2: Display? = null,
    @PopulatedBy(DisplayInfo::class) @SerialName("3") var x3: Display? = null,
  ) {
    @Serializable
    public data class Display(
      @PopulatedBy(DisplayInfo::class) @SerialName("currentSizeRange") var currentSizeRange: CurrentSizeRange? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("flags") var flags: String? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("metrics") var metrics: DisplayMetrics? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("realMetrics") var realMetrics: DisplayMetrics? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("name") var name: String? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("realSize") var realSize: List<Int>? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("rectSize") var rectSize: List<Int>? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("size") var size: List<Int>? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("isValid") var isValid: Boolean? = null,

      @PopulatedBy(DisplayInfo::class) @SerialName("rotation") var rotation: String? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("orientation") var orientation: Int? = null,

      @PopulatedBy(DisplayInfo::class) @SerialName("refreshRate") var refreshRate: Float? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("height") var height: Int? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("width") var width: Int? = null,
      @PopulatedBy(DisplayInfo::class) @SerialName("pixelFormat") var pixelFormat: Int? = null,
    ) {
      @Serializable
      public data class CurrentSizeRange(
        @PopulatedBy(DisplayInfo::class) @SerialName("smallest") var smallest: List<Int>? = null,
        @PopulatedBy(DisplayInfo::class) @SerialName("largest") var largest: List<Int>? = null,
      )

      @Serializable
      public data class DisplayMetrics(
        @PopulatedBy(DisplayInfo::class) @SerialName("density") var density: Float? = null,
        @PopulatedBy(DisplayInfo::class) @SerialName("xdpi") var xdpi: Float? = null,
        @PopulatedBy(DisplayInfo::class) @SerialName("ydpi") var ydpi: Float? = null,
        @PopulatedBy(DisplayInfo::class) @SerialName("densityDpi") var densityDpi: Int? = null,
        @PopulatedBy(DisplayInfo::class) @SerialName("scaledDensity") var scaledDensity: String? = null,
        @PopulatedBy(DisplayInfo::class) @SerialName("widthPixels") var widthPixels: Int? = null,
        @PopulatedBy(DisplayInfo::class) @SerialName("heightPixels") var heightPixels: Int? = null,
      )
    }
  }
}

public fun Report.toJson(): String = Json.encodeToString(this)
