plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.ksp) // Necesario para Room
}

android {
    namespace = "com.example.eventoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.eventoapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {

    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

// MockK
    testImplementation("io.mockk:mockk:1.13.9")

// JUnit4 (si usas las anotaciones @Before/@After/@Test como en el archivo)
    testImplementation("junit:junit:4.13.2")

// Retrofit/OkHttp (ya deberías tener retrofit impl; si toResponseBody falla añadir)
    testImplementation("com.squareup.okhttp3:okhttp:4.10.0")

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.compose.runtime:runtime-livedata")

    // Retrofit & Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.material:material-icons-extended")

    // Coil para Compose
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ViewModel + Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    // Navegación Compose
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Android Tests (instrumented / UI)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.0")
    androidTestImplementation("io.mockk:mockk-android:1.13.9")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // -----------------------
    // Testing - Unit tests
    // -----------------------
    // JUnit 4 (legacy / some libraries)
    testImplementation("junit:junit:4.13.2")

    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // MockK para mocks
    testImplementation("io.mockk:mockk:1.13.9")

    // JUnit 5 (Jupiter) - enable if you want to run tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    // Assertions (Kotest assertions)
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // Turbine (opcional, útil para testing de Flow)
    testImplementation("app.cash.turbine:turbine:0.12.1")

    // Coroutines + MockK duplicates from original removed to avoid version conflicts.
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
