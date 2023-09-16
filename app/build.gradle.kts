plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("kotlin-dsl")
}

android {
    namespace = "com.atakanmadanoglu.notesapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.atakanmadanoglu.notesapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 3
        versionName = "1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
        vectorDrawables {
            useSupportLibrary = true
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
            correctErrorTypes = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeVersion = "1.4.0-alpha04"
    val hiltVersion = "2.44.2"
    val roomVersion = "2.5.0"
    val espressoVersion = "3.5.1"
    val testJunitVersion = "1.1.5"
    val testRunnerVersion = "1.5.2"
    val mockkVersion = "1.13.2"
    val arcCoreTestingVersion = "2.2.0"
    val coroutinesVersion = "1.6.4"
    val navComposeVersion = "2.5.3"
    val androidXTestVersion = "1.5.0"
    val truthVersion = "1.5.0"
    val testRulesVersion = "1.5.0"
    val kotlin_version = "1.8.10"

    implementation ("androidx.core:core-ktx:1.9.0")
    implementation( "androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.8.0")
    implementation( "androidx.constraintlayout:constraintlayout:2.1.4")
    implementation( "androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    implementation( "androidx.activity:activity-compose:1.6.1")

    // Hilt
    implementation( "com.google.dagger:hilt-android:$hiltVersion")
    implementation( "androidx.core:core-ktx:1.9.0")
    kapt( "com.google.dagger:hilt-compiler:$hiltVersion")
    implementation( "androidx.hilt:hilt-navigation-compose:1.0.0")

    // Compose
    implementation( "androidx.compose.ui:ui:$composeVersion")
    implementation( "androidx.compose.material3:material3:1.1.0-alpha08")
    implementation( "androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation( "androidx.navigation:navigation-compose:$navComposeVersion")
    debugImplementation( "androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation( "androidx.compose.ui:ui-test-manifest:$composeVersion")

    // Room
    implementation( "androidx.room:room-ktx:$roomVersion")
    implementation( "androidx.room:room-runtime:$roomVersion")
    annotationProcessor( "androidx.room:room-compiler:$roomVersion")
    kapt( "androidx.room:room-compiler:$roomVersion")

    // DataStore
    implementation( "androidx.datastore:datastore-preferences:1.0.0")

    implementation( "androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

    // Testing
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation( "com.google.truth:truth:1.1.3")
    androidTestImplementation( "androidx.test:core:$androidXTestVersion")
    androidTestImplementation( "androidx.test:runner:$testRunnerVersion")
    androidTestImplementation( "androidx.test:rules:$testRulesVersion")
    androidTestImplementation( "androidx.compose.ui:ui-test-junit4:$composeVersion")
    // For instrumentation tests
    androidTestImplementation( "com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest( "com.google.dagger:hilt-compiler:$hiltVersion")
    testImplementation( "com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest( "com.google.dagger:hilt-compiler:$hiltVersion")
    // Mocking
    testImplementation( "io.mockk:mockk:$mockkVersion")
    // Assertions
    androidTestImplementation( "androidx.test.ext:junit:$testJunitVersion")
    androidTestImplementation( "androidx.test.ext:truth:$truthVersion")
    // Espresso dependencies
    androidTestImplementation( "androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-contrib:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-intents:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-accessibility:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-web:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso.idling:idling-concurrent:$espressoVersion")
    // Arc_core
    testImplementation( "androidx.arch.core:core-testing:$arcCoreTestingVersion")
    androidTestImplementation( "androidx.arch.core:core-testing:$arcCoreTestingVersion")
}