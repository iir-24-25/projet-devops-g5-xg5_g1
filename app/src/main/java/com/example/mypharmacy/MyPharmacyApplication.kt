package com.example.mypharmacy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyPharmacyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initializations can go here if needed

        // Note: Pour les dates avec Java 8, tu peux éventuellement
        // ajouter du code d'initialisation ici si nécessaire
    }
}