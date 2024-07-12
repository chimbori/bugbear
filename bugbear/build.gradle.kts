import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
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

publishing {
  publications {
    register<MavenPublication>("release") {
      groupId = libs.versions.publish.group.get()
      artifactId = libs.versions.publish.artifact.get()
      version = libs.versions.publish.version.get()
      afterEvaluate {
        from(components["release"])
      }
      pom {
        name.set(libs.versions.publish.name.get())
        description.set(libs.versions.publish.description.get())
        url.set(libs.versions.publish.url.get())
        licenses {
          license {
            name.set("MIT")
            url.set("https://opensource.org/licenses/MIT")
          }
        }
      }
    }
  }
  repositories {
    maven {
      name = "local"
      url = uri(project.layout.buildDirectory.dir("repo").get())
    }
  }
}

dependencies {
  api(libs.kotlinx.serialization.json)
  implementation(libs.androidx.work.runtime.ktx)
  implementation(libs.processphoenix)
}
