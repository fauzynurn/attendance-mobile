package com.example.attendance_mobile.data

import java.io.Serializable

data class JadwalMhs(
    override val namaMatkul: String,
    override val jenisMatkul: Boolean,
    override val kodeMatkul: String,
    val dosen: List<String>,
    override var jamMulai: String,
    override var jamSelesai: String,
    override var ruangan: Ruangan,
    override val jamMulaiOlehDosen: String,
    val listSesi: List<KehadiranPerSesi>
) : Jadwal(), Serializable