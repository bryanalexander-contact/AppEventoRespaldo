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

    // Mantén una versión del compiler compatible con tu Kotlin (si usas Kotlin 1.9.24, 1.5.x compiler suele ser correcto).
    composeOptions {
        // Si tienes Kotlin 1.9.24, 1.5.14 suele funcionar; si usas otra Kotlin, ajusta según la tabla de compatibilidad.
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.compose.runtime:runtime-livedata")


    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Jetpack Compose BOM (gestiona versiones compatibles)
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // Compose UI (sin versiones explícitas, las gestiona el BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material 3 (dejar que el BOM gestione la versión)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

    // Iconos (extended): artefacto correcto (no material3-icons-extended)
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

    // Android Tests
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
