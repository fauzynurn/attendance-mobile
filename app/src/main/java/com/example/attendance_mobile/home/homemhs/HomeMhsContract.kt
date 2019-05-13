package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.ScheduleMhs

interface HomeMhsContract {
    interface ViewContract : BaseView<HomeMhsPresenter> {
        fun startBeaconActivity(kodeRuangan : String, macAddress : String)
        fun onSummaryDataLoaded(data : HashMap<String,Int>)
        fun onScheduleListLoaded()
        fun showSnackBar(message: String)
        fun showDialog(title: String, message: String)
        fun refreshList()
    }

    interface InteractorContract{
        fun onScheduleListResult(data : ArrayList<ScheduleMhs>)
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
        fun setPresenceButtonClickListener(item : ScheduleMhs, clickListener: (ScheduleMhs) -> Unit)
        fun hidePresenceButton()
    }
}