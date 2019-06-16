package com.example.attendance_mobile.model

interface BaseBeaconInteractor {
    fun onBeaconFound()
    fun onTimeout()
}