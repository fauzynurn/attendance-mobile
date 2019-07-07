package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.Ruangan
import com.example.attendance_mobile.data.response.ScheduleResponse

interface HomeMhsContract {
    interface ViewContract : BaseView<HomeMhsPresenter> {
        fun startBeaconActivity(ruangan: Ruangan)
        fun startDetailPerSessionActivity(schedule: JadwalMhs)
        fun onSummaryDataLoaded(data : HashMap<String,String>)
        fun onMhsScheduleListLoaded()
        fun onOngoingMhsScheduleLoaded()
        fun setName(name : String)
        fun showSnackBar(message: String)
        fun showDialog(title: String, message: String)
        fun refreshScheduleList()
        fun checkAllRequirement() : Boolean
        fun handleNoMhsScheduleFound()
        fun handleNoOngoingMhsScheduleFound()

        fun setOngoingMatkulName(name : String)
        fun setOngoingJenisMatkul(jenisMatkul : String)
        fun setOngoingTime(startTime : String)
        fun setBtnLabel(label : String)
        fun setPresenceButtonClickListener(item : JadwalMhs, clickListener: (JadwalMhs) -> Unit)
    }

    interface InteractorContract{
        fun onScheduleListResult(data : ScheduleResponse<JadwalMhs>)
        fun onSummaryResult(summaryData : HashMap<String,String>)
        fun onFail(error : String?)
    }

    interface ItemViewContract{
        fun setStartTime(startTime : String)
        fun setEndTime(endTime : String)
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
    }
}