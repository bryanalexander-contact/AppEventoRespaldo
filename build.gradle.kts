// build.gradle.kts (raíz del proyecto)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.ksp) apply false
}

// ✅ No se necesitan repositorios aquí
// Gradle ya los toma de settings.gradle.kts
