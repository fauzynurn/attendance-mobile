package com.example.attendance_mobile.utils

class Constants{
    companion object {
        const val FINGERPRINT_NOT_SUPPORTED_TITLE = "Pemindai sidik jari tidak ditemukan"
        const val FINGERPRINT_NOT_SUPPORTED_MESSAGE = "Untuk dapat menggunakan aplikasi diperlukan pemindai sidik jari"
        const val FINGERPRINT_DATA_NOT_FOUND_TITLE = "Tidak ada sidik jari yang terdaftar pada perangkat ini"
        const val FINGERPRINT_DATA_NOT_FOUND_MESSAGE = "Silahkan daftarkan sidik jari anda terlebih dahulu pada pengaturan perangkat ini"
        const val LOGIN_ERROR = "Proses login tidak dapat dilakukan"
        const val DOSEN_INVALID_FORM = "Kode dosen tidak boleh kosong"
        const val MHS_INVALID_FORM = "NIM tidak boleh kosong"
        const val STILL_ACTIVE_MESSAGE = "Anda tercatat sedang menggunakan device lain. Silahkan hubungi tata usaha untuk melakukan reset"
        const val STILL_ACTIVE_STATUS = "User is active"
        const val NOT_VERIFIED_MESSAGE = "Anda tidak dikenali atau data yang dimasukkan salah"
        const val TEORI = "Teori"
        const val ON_BEACON_FOUND = "ON_BEACON_FOUND"
        const val START_ALARM = "START_ALARM"
        const val ON_TIMEOUT = "ON_TIMEOUT"
        const val AUTO_TIME_DISABLED_MESSAGE = "Pastikan waktu dan zona waktu di set otomatis. Silahkan konfigurasikan di menu Setting anda."
        const val PRAKTEK = "Praktek"
        const val REGION = "CB10023F-A318-3394-4199-A8730C7C1AEC"
        const val NOT_YET_ACTIVE_STATUS = "User is not active"
        const val FINGERPRINT_NOT_RECOGNIZED_MESSAGE = "Sidik jari tidak dikenali"
        const val IMEI_MATCH = "Imei match"
        const val DOSEN = 2
        const val ON_BEACON_ERROR = "ON_BEACON_ERROR"
        const val LATE_LIMIT = 5
        const val MAHASISWA = 1
        const val PERMISSION_DENIED = "Terdapat masalah dalam perizinan aplikasi dari sistem"
        const val MAHASISWA_SUBTITLE = "Silahkan masukkan NIM anda"
        const val DOSEN_SUBTITLE = "Silahkan masukkan kode dosen anda"
        const val START_BACKGROUND_RANGING = "START_BEACON_RANGING"
        const val BEACON_BACKGROUND_RANGING_TIMEOUT = "BEACON_BACKGROUND_RANGING_TIMEOUT"
        const val HADIR = 1
        const val TIDAK_HADIR = 0
        const val TANPA_KETERANGAN = 2
    }
}