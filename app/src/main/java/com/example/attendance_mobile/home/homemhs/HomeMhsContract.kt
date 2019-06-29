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
        fun onRegulerScheduleListLoaded()
        fun onAltScheduleListLoaded()
        fun setName(name : String)
        fun showSnackBar(message: String)
        fun showDialog(title: String, message: String)
        fun refreshRegulerList()
        fun refreshAltList()
        fun checkAllRequirement() : Boolean
        fun handleNoRegulerScheduleFound()
        fun handleNoAltScheduleFound()
    }

    interface InteractorContract{
        fun onScheduleListResult(data : ScheduleResponse<JadwalMhs>)
        fun onSummaryResult(summaryData : HashMap<String,String>)
        fun onFail(error : String?)
    }

    interface ItemViewContract{
        fun highlightItem()
        fun setStartTime(startTime : String)
        fun setEndTime(endTime : String)
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
        fun setStatusMatkul(statusMatkul : String)
        fun setStatusMatkulColor(colorCode : String)
        fun setPresenceButtonClickListener(item : JadwalMhs, clickListener: (JadwalMhs) -> Unit)
        fun hidePresenceButton()
    }
}