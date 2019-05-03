package com.example.attendance_mobile.utils

class Constants{
    companion object {
        const val DOSEN_INVALID_FORM = "Kode dosen dan password tidak boleh kosong"
        const val MHS_INVALID_FORM = "NIM dan password tidak boleh kosong"
        const val STILL_ACTIVE_MESSAGE = "Anda tercatat sedang menggunakan device lain. Silahkan hubungi tata usaha untuk melakukan reset"
        const val STILL_ACTIVE_STATUS = "User is active"
        const val NOT_VERIFIED_MESSAGE = "Anda tidak dikenali atau data yang dimasukkan salah"
        const val NOT_YET_ACTIVE_STATUS = "User is not active"
        const val DOSEN = 2
        const val MAHASISWA = 1
        const val PERMISSION_DENIED = "Terdapat masalah dalam perizinan aplikasi dari sistem"
        const val MAHASISWA_SUBTITLE = "Silahkan masukkan NIM dan password anda"
        const val DOSEN_SUBTITLE = "Silahkan masukkan kode dosen dan password\nanda"
    }
}