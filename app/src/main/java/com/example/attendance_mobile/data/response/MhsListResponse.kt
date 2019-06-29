package com.example.attendance_mobile.data.response

import com.example.attendance_mobile.data.Kehadiran

data class MhsListResponse (
    val jamKe : String,
    val mhsList : List<Kehadiran>
)