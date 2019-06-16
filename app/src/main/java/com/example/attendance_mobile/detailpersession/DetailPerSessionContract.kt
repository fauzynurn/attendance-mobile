package com.example.attendance_mobile.detailpersession

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.KehadiranPerSesi

interface DetailPerSessionContract{
    interface ViewContract : BaseView<DetailPerSessionPresenter> {
        fun onAttendanceSessionListLoaded()
        fun refreshList()
        fun setNamaMatkul(nama : String)
        fun setJamMatkul(jamMulai : String, jamSelesai : String)
        fun setDosen(dosen : String)
        fun hideMoreButton()
        fun startHomeMhs()
        fun setMoreClickListener(list : List<String>)
    }


    interface InteractorContract{
        fun onAttendanceSessionListSuccess(list : List<KehadiranPerSesi>)
    }

    interface ItemViewContract{
        fun setSessionNum(session : Int)
        fun setMarker(status : Int)
        fun setSessionTime(startTime : String, endTime : String)
    }
}