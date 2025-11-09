// build.gradle.kts (raíz)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.ksp) apply false
}

// (opcional, solo si usas librerías globales)
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
    }
}
