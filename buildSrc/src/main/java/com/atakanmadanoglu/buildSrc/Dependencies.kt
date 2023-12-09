package com.atakanmadanoglu.buildSrc

object Dependencies {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
    const val appCompat = "androidx.appcompat:appcompat:1.6.1"
    const val material = "com.google.android.material:material:1.8.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.0"
    const val activityCompose = "androidx.activity:activity-compose:1.6.1"

    // Hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"

    // Compose
    const val composeUi = "androidx.compose.ui:ui:${Versions.composeVersion}"
    const val composeMaterial3 = "androidx.compose.material3:material3:1.1.0-alpha08"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}"
    const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.navComposeVersion}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.composeVersion}"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.composeVersion}"
    const val composeLifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-compose:2.6.0"

    // Room
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"

    // DataStore
    const val dataStorePreferences = "androidx.datastore:datastore-preferences:1.0.0"


    // Testing
    const val jUnit4 = "junit:junit:4.13.2"
    const val kotlinCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}"
    const val truth = "com.google.truth:truth:1.1.3"
    const val androidXTestCore = "androidx.test:core:${Versions.androidXTestVersion}"
    const val androidXTestRunner = "androidx.test:runner:${Versions.testRunnerVersion}"
    const val androidxTestRules = "androidx.test:rules:${Versions.testRulesVersion}"
    const val composeUiTestJUnit4 = "androidx.compose.ui:ui-test-junit4:${Versions.composeVersion}"
    // For instrumentation tests
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Versions.hiltVersion}"
    // Mocking
    const val mockK = "io.mockk:mockk:${Versions.mockkVersion}"
    // Assertions
    const val androidXExtJUnit = "androidx.test.ext:junit:${Versions.testJunitVersion}"
    const val androidXExtTruth = "androidx.test.ext:truth:${Versions.truthVersion}"
    // Espresso dependencies
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espressoVersion}"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espressoVersion}"
    const val espressoAccessibility = "androidx.test.espresso:espresso-accessibility:${Versions.espressoVersion}"
    const val espressoWeb = "androidx.test.espresso:espresso-web:${Versions.espressoVersion}"
    const val espressoIdling = "androidx.test.espresso.idling:idling-concurrent:${Versions.espressoVersion}"
    // Arc_core
    const val archCoreTesting = "androidx.arch.core:core-testing:${Versions.arcCoreTestingVersion}"
}