package com.example.attendance_mobile.data

data class Schedule(
    val namaMatkul : String,
    val jenisMatkul : Boolean,
    val kodeMatkul : String,
    val jamMulai : String,
    var jamSelesai : String,
    val kodeRuangan : String,
    val macAddress : String,
    val jamMulaiOlehDosen : String,
    val jamMatkul : ArrayList<Sesi>
)