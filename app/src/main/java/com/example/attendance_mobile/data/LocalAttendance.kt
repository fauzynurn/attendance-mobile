package com.example.attendance_mobile.data

import com.example.attendance_mobile.data.response.AttendanceResponse
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class LocalAttendance(
    var sesi: Int = 0,
    var attendanceResponse: AttendanceResponse? = null,
    var jamMulai: String = "",
    var jamSelesai: String = "",
    var hasBeenExecuted : Int = 0,
    var macAddress : String =""
) : RealmObject()