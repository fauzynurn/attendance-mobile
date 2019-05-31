package com.example.attendance_mobile.data

data class ScheduleResponse(
    val jadwalReguler : ArrayList<Schedule>,
    val jadwalPengganti : ArrayList<Schedule>
)