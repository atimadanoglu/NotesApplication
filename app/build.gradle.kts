import com.atakanmadanoglu.buildSrc.Configs
import com.atakanmadanoglu.buildSrc.Dependencies.activityCompose
import com.atakanmadanoglu.buildSrc.Dependencies.androidXExtJUnit
import com.atakanmadanoglu.buildSrc.Dependencies.androidXExtTruth
import com.atakanmadanoglu.buildSrc.Dependencies.androidXTestRunner
import com.atakanmadanoglu.buildSrc.Dependencies.androidxTestRules
import com.atakanmadanoglu.buildSrc.Dependencies.appCompat
import com.atakanmadanoglu.buildSrc.Dependencies.archCoreTesting
import com.atakanmadanoglu.buildSrc.Dependencies.composeLifecycleRuntime
import com.atakanmadanoglu.buildSrc.Dependencies.composeMaterial3
import com.atakanmadanoglu.buildSrc.Dependencies.composeNavigation
import com.atakanmadanoglu.buildSrc.Dependencies.composeUi
import com.atakanmadanoglu.buildSrc.Dependencies.composeUiTestJUnit4
import com.atakanmadanoglu.buildSrc.Dependencies.composeUiTestManifest
import com.atakanmadanoglu.buildSrc.Dependencies.composeUiTooling
import com.atakanmadanoglu.buildSrc.Dependencies.composeUiToolingPreview
import com.atakanmadanoglu.buildSrc.Dependencies.constraintLayout
import com.atakanmadanoglu.buildSrc.Dependencies.coreKtx
import com.atakanmadanoglu.buildSrc.Dependencies.dataStorePreferences
import com.atakanmadanoglu.buildSrc.Dependencies.espressoAccessibility
import com.atakanmadanoglu.buildSrc.Dependencies.espressoContrib
import com.atakanmadanoglu.buildSrc.Dependencies.espressoCore
import com.atakanmadanoglu.buildSrc.Dependencies.espressoIdling
import com.atakanmadanoglu.buildSrc.Dependencies.espressoIntents
import com.atakanmadanoglu.buildSrc.Dependencies.espressoWeb
import com.atakanmadanoglu.buildSrc.Dependencies.hiltAndroid
import com.atakanmadanoglu.buildSrc.Dependencies.hiltAndroidTesting
import com.atakanmadanoglu.buildSrc.Dependencies.hiltCompiler
import com.atakanmadanoglu.buildSrc.Dependencies.hiltNavigationCompose
import com.atakanmadanoglu.buildSrc.Dependencies.jUnit4
import com.atakanmadanoglu.buildSrc.Dependencies.kotlinCoroutinesTest
import com.atakanmadanoglu.buildSrc.Dependencies.lifecycleRuntimeKtx
import com.atakanmadanoglu.buildSrc.Dependencies.material
import com.atakanmadanoglu.buildSrc.Dependencies.mockK
import com.atakanmadanoglu.buildSrc.Dependencies.roomCompiler
import com.atakanmadanoglu.buildSrc.Dependencies.roomKtx
import com.atakanmadanoglu.buildSrc.Dependencies.roomRuntime
import com.atakanmadanoglu.buildSrc.Dependencies.truth

plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("android")
}

android {
    namespace = "com.atakanmadanoglu.notesapplication"
    compileSdk = Configs.compileSdkVersion

    defaultConfig {
        applicationId = Configs.applicationId
        minSdk = Configs.minSdkVersion
        targetSdk = Configs.targetSdkVersion
        versionCode = Configs.versionCode
        versionName = Configs.versionName
        testInstrumentationRunner = Configs.testInstrumentationRunner
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }

        release {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(coreKtx)
    implementation(appCompat)
    implementation(constraintLayout)
    implementation(lifecycleRuntimeKtx)
    implementation(material)
    implementation(activityCompose)

    // Hilt
    implementation(hiltAndroid)
    implementation(hiltNavigationCompose)
    ksp(hiltCompiler)

    // Compose
    implementation(composeUi)
    implementation(composeMaterial3)
    implementation(composeUiToolingPreview)
    implementation(composeNavigation)
    implementation(composeLifecycleRuntime)
    debugImplementation(composeUiTooling)
    debugImplementation(composeUiTestManifest)

    // Room
    implementation(roomKtx)
    implementation(roomRuntime)
    ksp(roomCompiler)

    // DataStore
    implementation(dataStorePreferences)

    // Testing
    testImplementation(jUnit4)
    testImplementation(kotlinCoroutinesTest)
    testImplementation(truth)
    androidTestImplementation(androidXTestRunner)
    androidTestImplementation(androidxTestRules)
    androidTestImplementation(composeUiTestJUnit4)

    // For instrumentation tests
    androidTestImplementation(hiltAndroidTesting)
    testImplementation(hiltAndroidTesting)
    kspTest(hiltAndroidTesting)
    // Mocking
    testImplementation(mockK)
    // Assertions
    androidTestImplementation(androidXExtJUnit)
    androidTestImplementation(androidXExtTruth)

    // Espresso dependencies
    androidTestImplementation(espressoCore)
    androidTestImplementation(espressoContrib)
    androidTestImplementation(espressoIntents)
    androidTestImplementation(espressoAccessibility)
    androidTestImplementation(espressoWeb)
    androidTestImplementation(espressoIdling)

    // Arc_core
    testImplementation(archCoreTesting)
    androidTestImplementation(archCoreTesting)
}