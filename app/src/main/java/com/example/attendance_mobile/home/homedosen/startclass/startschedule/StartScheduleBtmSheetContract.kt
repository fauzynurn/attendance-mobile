package com.example.attendance_mobile.home.homedosen.startclass.startschedule

import com.example.attendance_mobile.data.response.RoomAvailableResponse

interface StartScheduleBtmSheetContract {
    interface ViewContract{
        fun initRoomAdapter(data : List<String>)
        fun onRoomDataReady(data : List<String>)
    }

    interface InteractorContract{
        fun onRoomListResult(data : RoomAvailableResponse)
        fun onFail(message : String)
    }
}