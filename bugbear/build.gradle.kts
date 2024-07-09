import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "com.chimbori.bugbear"
  compileSdk = 34
  defaultConfig {
    minSdk = 28
    buildConfigField("String", "VERSION_NAME", "\"${findProperty("VERSION_NAME")}\"")
  }
  compileOptions {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    buildConfig = true
  }
}

kotlin {
  explicitApi = Strict
}

dependencies {
  api(libs.kotlinx.serialization.json)
  implementation(libs.androidx.work.runtime.ktx)
  implementation(libs.processphoenix)
}
