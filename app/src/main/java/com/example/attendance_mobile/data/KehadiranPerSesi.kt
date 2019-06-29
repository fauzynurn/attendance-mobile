package com.example.attendance_mobile.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class KehadiranPerSesi (
    @PrimaryKey
    var id : String = "",
    var sesi : Int = 0,
    var jamMulai : String = "",
    var jamSelesai : String = "",
    var status : Boolean = false,
    var tglKuliah : String = "",
    var kodeMatkul : String = ""
) : RealmObject(),Serializable