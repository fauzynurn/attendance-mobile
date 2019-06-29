package com.example.attendance_mobile.data.response

data class BaseResponse(
    var status : String,
    var message : String,
    var data : HashMap<String,String>
)