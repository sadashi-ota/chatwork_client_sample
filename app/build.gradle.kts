import com.android.build.gradle.internal.dsl.BuildType
import de.mannodermaus.gradle.plugins.junit5.junitPlatform

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("de.mannodermaus.android-junit5")
    id("jacoco-android")
}

Prop.loadProperties("$rootDir/properties/secrets.properties")

jacoco {
    toolVersion = "0.8.3"
}

android {
    compileSdkVersion(Deps.Versions.compileSdk)
    defaultConfig {
        applicationId = "com.sadashi.client.chatwork"
        minSdkVersion(Deps.Versions.minSdk)
        targetSdkVersion(Deps.Versions.compileSdk)
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug") {
            setCommonBuildConfig(this)
            isMinifyEnabled = false
        }
        getByName("release") {
            setCommonBuildConfig(this)
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        junitPlatform {
            filters {
                includeEngines("spek2")
            }
        }
    }
}

dependencies {
    Deps.libraries.forEach { implementation(it) }
    Deps.testLibraries.forEach { testImplementation(it) }
}


fun setCommonBuildConfig(buildType: BuildType) {
    buildType.buildConfigField("String", "LOGIN_URL", "\"${Prop.map["loginUrl"]}\"")
    buildType.buildConfigField("String", "CLIENT_ID", "\"${Prop.map["clientId"]}\"")
    buildType.buildConfigField("String", "API_DOMAIN", "\"${Prop.map["apiDomain"]}\"")
    buildType.buildConfigField("String", "AUTH_DOMAIN", "\"${Prop.map["authDomain"]}\"")
    buildType.buildConfigField("String", "AUTH_CALLBACK", "\"${Prop.map["authCallback"]}\"")
}
