package com.example.attendance_mobile.home.homedosen.startclass.startschedule

import com.example.attendance_mobile.data.Ruangan

interface StartScheduleBtmSheetContract {
    interface ViewContract{
        fun initRoomAdapter(data : List<String>)
        fun onRoomDataReady(data : List<String>)
        fun onTimeDataReady(data : List<String>)
    }

    interface InteractorContract{
        fun onRoomListResult(data : List<Ruangan>)
        fun onTimeListFromRoomResult(data : List<String>)
        fun onRoomListFromTimeResult(data : List<Ruangan>)
        fun onFail(message : String)
    }
}