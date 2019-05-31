package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.Schedule
import com.example.attendance_mobile.data.ScheduleResponse

interface HomeMhsContract {
    interface ViewContract : BaseView<HomeMhsPresenter> {
        fun startBeaconActivity(kodeRuangan : String, macAddress : String)
        fun onSummaryDataLoaded(data : HashMap<String,Int>)
        fun onScheduleListLoaded()
        fun showSnackBar(message: String)
        fun showDialog(title: String, message: String)
        fun refreshList()
        fun checkAllRequirement() : Boolean
        fun handleNoScheduleFound()
    }

    interface InteractorContract{
        fun onScheduleListResult(data : ScheduleResponse)
        fun onSummaryResult(summaryData : HashMap<String,Int>)
        fun onFail(error : String?)
    }

    interface ItemViewContract{
        fun setStartTime(startTime : String)
        fun setEndTime(endTime : String)
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
        fun setStatusMatkul(statusMatkul : String)
        fun setStatusMatkulColor(colorCode : String)
        fun setPresenceButtonClickListener(item : Schedule, clickListener: (Schedule) -> Unit)
        fun hidePresenceButton()
    }
}