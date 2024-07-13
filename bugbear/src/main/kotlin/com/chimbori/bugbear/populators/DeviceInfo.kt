package com.chimbori.bugbear.populators

import android.content.Context
import android.content.Context.DISPLAY_SERVICE
import android.content.res.Configuration.HARDKEYBOARDHIDDEN_NO
import android.content.res.Configuration.HARDKEYBOARDHIDDEN_YES
import android.content.res.Configuration.KEYBOARDHIDDEN_NO
import android.content.res.Configuration.KEYBOARDHIDDEN_YES
import android.content.res.Configuration.KEYBOARD_12KEY
import android.content.res.Configuration.KEYBOARD_NOKEYS
import android.content.res.Configuration.KEYBOARD_QWERTY
import android.content.res.Configuration.NAVIGATIONHIDDEN_NO
import android.content.res.Configuration.NAVIGATIONHIDDEN_YES
import android.content.res.Configuration.NAVIGATION_DPAD
import android.content.res.Configuration.NAVIGATION_NONAV
import android.content.res.Configuration.NAVIGATION_TRACKBALL
import android.content.res.Configuration.NAVIGATION_WHEEL
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Configuration.ORIENTATION_SQUARE
import android.content.res.Configuration.ORIENTATION_UNDEFINED
import android.content.res.Configuration.TOUCHSCREEN_FINGER
import android.content.res.Configuration.TOUCHSCREEN_NOTOUCH
import android.graphics.Point
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.util.DisplayMetrics
import android.view.Display
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_90
import com.chimbori.bugbear.Populator
import com.chimbori.bugbear.Report

internal class DeviceInfo : Populator {
  override fun populate(report: Report) = report.apply {
    brand = Build.BRAND
    phoneModel = Build.MODEL
    product = Build.PRODUCT
    androidVersion = Build.VERSION.RELEASE

    if (build == null) {
      build = Report.Build()
    }
    build?.apply {
      board = Build.BOARD
      bootloader = Build.BOOTLOADER
      fingerprint = Build.FINGERPRINT
      hardware = Build.HARDWARE

      manufacturer = Build.MANUFACTURER
      brand = Build.BRAND
      device = Build.DEVICE
      display = Build.DISPLAY

      host = Build.HOST
      id = Build.ID
      model = Build.MODEL

      if (SDK_INT >= S) {
        socModel = Build.SOC_MODEL
        socManufacturer = Build.SOC_MANUFACTURER
        sku = Build.SKU
        odmSku = Build.ODM_SKU
      }

      product = Build.PRODUCT
      radio = Build.getRadioVersion()

      tags = Build.TAGS
      time = Build.TIME
      type = Build.TYPE
      user = Build.USER

      if (version == null) {
        version = Report.Build.Version()
      }
      version?.apply {
        release = Build.VERSION.RELEASE
        sdkInt = SDK_INT
        incremental = Build.VERSION.INCREMENTAL
        securityPatch = Build.VERSION.SECURITY_PATCH
        previewSdkInt = Build.VERSION.PREVIEW_SDK_INT
        if (SDK_INT > S) {
          mediaPerformanceClass = Build.VERSION.MEDIA_PERFORMANCE_CLASS
        }
      }

      supportedAbis = Build.SUPPORTED_ABIS.toList()
      supported32BitAbis = Build.SUPPORTED_32_BIT_ABIS.toList()
      supported64BitAbis = Build.SUPPORTED_64_BIT_ABIS.toList()
    }

    return report
  }
}

internal class ResourceConfigurationInfo(private val context: Context) : Populator {
  private val configurationAtInstantiation = populateConfiguration()

  override fun populate(report: Report) = report.apply {
    report.initialConfiguration = configurationAtInstantiation
    report.crashConfiguration = populateConfiguration()
  }

  private fun populateConfiguration() = Report.ResourceConfiguration().apply {
    val conf = context.resources.configuration
    colorMode = conf.colorMode
    densityDpi = conf.densityDpi
    fontScale = conf.fontScale

    if (SDK_INT >= S) {
      fontWeightAdjustment = conf.fontWeightAdjustment
    }
    hardKeyboardHidden = when (conf.hardKeyboardHidden) {
      HARDKEYBOARDHIDDEN_NO -> "HARDKEYBOARDHIDDEN_NO"
      HARDKEYBOARDHIDDEN_YES -> "HARDKEYBOARDHIDDEN_YES"
      else -> "${conf.hardKeyboardHidden}"
    }
    keyboard = when (conf.keyboard) {
      KEYBOARD_NOKEYS -> "KEYBOARD_NOKEYS"
      KEYBOARD_QWERTY -> "KEYBOARD_QWERTY"
      KEYBOARD_12KEY -> "KEYBOARD_12KEY"
      else -> "${conf.keyboard}"
    }
    keyboardHidden = when (conf.keyboardHidden) {
      KEYBOARDHIDDEN_NO -> "KEYBOARDHIDDEN_NO"
      KEYBOARDHIDDEN_YES -> "KEYBOARDHIDDEN_YES"
      else -> "${conf.keyboardHidden}"
    }
    locale = conf.locales.get(0).toLanguageTag()
    locales = conf.locales.toLanguageTags().split(",")
    mcc = conf.mcc
    mnc = conf.mnc
    navigation = when (conf.navigation) {
      NAVIGATION_NONAV -> "NAVIGATION_NONAV"
      NAVIGATION_DPAD -> "NAVIGATION_DPAD"
      NAVIGATION_TRACKBALL -> "NAVIGATION_TRACKBALL"
      NAVIGATION_WHEEL -> "NAVIGATION_WHEEL"
      else -> "${conf.navigation}"
    }
    navigationHidden = when (conf.navigationHidden) {
      NAVIGATIONHIDDEN_NO -> "NAVIGATIONHIDDEN_NO"
      NAVIGATIONHIDDEN_YES -> "NAVIGATIONHIDDEN_YES"
      else -> "${conf.navigationHidden}"
    }
    @Suppress("DEPRECATION")
    orientation = when (conf.orientation) {
      ORIENTATION_LANDSCAPE -> "ORIENTATION_LANDSCAPE"
      ORIENTATION_PORTRAIT -> "ORIENTATION_PORTRAIT"
      ORIENTATION_SQUARE -> "ORIENTATION_SQUARE"
      ORIENTATION_UNDEFINED -> "ORIENTATION_UNDEFINED"
      else -> "${conf.orientation}"
    }
    screenWidthDp = conf.screenWidthDp
    screenHeightDp = conf.screenHeightDp
    smallestScreenWidthDp = conf.smallestScreenWidthDp
    touchscreen = when (conf.touchscreen) {
      TOUCHSCREEN_NOTOUCH -> "TOUCHSCREEN_NOTOUCH"
      TOUCHSCREEN_FINGER -> "TOUCHSCREEN_FINGER"
      else -> "${conf.touchscreen}"
    }

    // TODO: Unmask the bit masks.
    // screenLayout = "TODO"
    // uiMode = "TODO"
  }
}

internal class DisplayInfo(private val context: Context) : Populator {
  private val displays by lazy { (context.getSystemService(DISPLAY_SERVICE) as DisplayManager).displays }

  private fun populateDisplay(display: Display) = Report.Displays.Display().apply {
    name = display.name
    isValid = display.isValid

    metrics = display.getMetrics().toMetrics()
    realMetrics = display.getRealMetrics().toMetrics()
    size = display.getSize().toList()
    realSize = display.getRealSize().toList()
    rectSize = display.getRectSize().toList()
    orientation = display.rotation
    rotation = when (display.rotation) {
      ROTATION_0 -> "ROTATION_0"
      ROTATION_180 -> "ROTATION_180"
      ROTATION_270 -> "ROTATION_270"
      ROTATION_90 -> "ROTATION_90"
      else -> "${display.rotation}"
    }
    refreshRate = display.refreshRate

    if (currentSizeRange == null) {
      currentSizeRange = Report.Displays.Display.CurrentSizeRange()
    }
    currentSizeRange?.apply {
      val outSmallest = Point()
      val outLargest = Point()
      display.getCurrentSizeRange(outSmallest, outLargest)
      smallest = listOf(outSmallest.x, outSmallest.y)
      largest = listOf(outLargest.x, outLargest.y)
    }

    // TODO: Flags and bit masks

    @Suppress("DEPRECATION")
    height = display.height
    @Suppress("DEPRECATION")
    width = display.width
    @Suppress("DEPRECATION")
    pixelFormat = display.pixelFormat
  }

  override fun populate(report: Report) = report.apply {
    if (report.display == null) {
      report.display = Report.Displays()
    }
    report.display?.apply {
      displays.forEachIndexed { i, display ->
        when (i) {
          0 -> x0 = populateDisplay(display)
          1 -> x1 = populateDisplay(display)
          2 -> x2 = populateDisplay(display)
          3 -> x3 = populateDisplay(display)
        }
      }
    }
  }

  private fun DisplayMetrics.toMetrics(): Report.Displays.Display.DisplayMetrics {
    val reportMetrics = Report.Displays.Display.DisplayMetrics()
    reportMetrics.density = density
    reportMetrics.densityDpi = densityDpi
    @Suppress("DEPRECATION")
    reportMetrics.scaledDensity = "x$scaledDensity"
    reportMetrics.widthPixels = widthPixels
    reportMetrics.heightPixels = heightPixels
    reportMetrics.xdpi = xdpi
    reportMetrics.ydpi = ydpi
    return reportMetrics
  }

  @Suppress("DEPRECATION")
  companion object {
    private fun Point.toList() = listOf(x, y)
    private fun Rect.toList() = listOf(top, left, width(), height())

    private fun Display.getMetrics() = DisplayMetrics().apply { getMetrics(this) }
    private fun Display.getRealMetrics() = DisplayMetrics().apply { getRealMetrics(this) }

    private fun Display.getSize() = Point().apply { getSize(this) }
    private fun Display.getRealSize() = Point().apply { getRealSize(this) }
    private fun Display.getRectSize() = Rect().apply { getRectSize(this) }
  }
}
