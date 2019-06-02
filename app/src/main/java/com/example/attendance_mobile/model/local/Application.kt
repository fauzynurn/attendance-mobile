package com.example.attendance_mobile.model.local

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration



class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
            .name("attendance.realm")
            .inMemory()
            .build())
    }
}