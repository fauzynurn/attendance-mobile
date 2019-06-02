package com.example.attendance_mobile.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Attendance(
    @PrimaryKey
    var id : String = "",
    var sesi: Int = 0,
    var kodeMatkul: String = "",
    var tglKuliah : String = "",

    var jamMulai: String = "",
    var jamSelesai: String = "",
    var hasBeenExecuted : Int = 0,
    var macAddress : String =""
) : RealmObject()