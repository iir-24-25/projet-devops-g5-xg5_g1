buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}

// Assurez-vous que tous les projets utilisent ces repos
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Pour MPAndroidChart
    }
}

plugins {
    id("com.android.application") version "8.9.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}

// Configuration commune pour tous les projets
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}