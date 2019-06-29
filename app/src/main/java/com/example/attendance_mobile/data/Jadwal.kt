package com.example.attendance_mobile.data

import java.io.Serializable

abstract class Jadwal : Serializable {
    abstract val namaMatkul: String
    abstract val jenisMatkul: Boolean
    abstract val kodeMatkul: String
    abstract var jamMulai: String
    abstract val jamMulaiOlehDosen: String
    abstract var jamSelesai: String
    abstract var ruangan: Ruangan
}