package com.example.attendance_mobile.data

import java.io.Serializable

data class JadwalDsn(
    override val namaMatkul: String,
    override val jenisMatkul: Boolean,
    override val kodeMatkul: String,
    override var jamMulai: String,
    val kelas : String,
    override val jamMulaiOlehDosen: String,
    override var jamSelesai: String,
    override var ruangan: Ruangan
) : Jadwal(),Serializable