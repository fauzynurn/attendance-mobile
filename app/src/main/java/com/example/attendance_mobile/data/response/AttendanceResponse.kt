package com.example.attendance_mobile.data.response

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class AttendanceResponse (
    var nim : String = "",
    var kodeMatkul: String = "",
    var tglKuliah : String = ""
) : RealmObject()