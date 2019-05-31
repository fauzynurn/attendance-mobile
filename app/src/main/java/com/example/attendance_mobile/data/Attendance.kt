package com.example.attendance_mobile.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(primaryKeys = ["sesi","kode_matkul","tgl_kuliah"], tableName = "attendance")
data class Attendance(
    @ColumnInfo(name = "sesi") var sesi: Int,
    @ColumnInfo(name = "kode_matkul") var kodeMatkul: String,
    @ColumnInfo(name = "jam_mulai") var jamMulai: String,
    @ColumnInfo(name = "jam_selesai") var jamSelesai: String,
    @ColumnInfo(name = "has_been_executed") var hasBeenExecuted : Int,
    @ColumnInfo(name = "mac_address") var macAddress : String,
    @ColumnInfo(name = "tgl_kuliah") var tglKuliah : String
) : Parcelable