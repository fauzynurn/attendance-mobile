package com.example.attendance_mobile.utils

class Converters {
    companion object {
        fun convertAttributesToSingleString(sesi: String, kodeMatkul: String, jadwal: String): String {
            return mutableListOf(sesi,kodeMatkul,jadwal).joinToString(separator = ":")
        }
    }
}