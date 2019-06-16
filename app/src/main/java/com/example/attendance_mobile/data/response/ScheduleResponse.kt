package com.example.attendance_mobile.data.response

data class ScheduleResponse<T>(
    val jadwalReguler : ArrayList<T>,
    val jadwalPengganti : ArrayList<T>
)