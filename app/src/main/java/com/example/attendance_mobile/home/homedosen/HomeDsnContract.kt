package com.example.attendance_mobile.home.homedosen

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.DsnSchedule
import com.example.attendance_mobile.data.Matkul
import com.example.attendance_mobile.data.response.ScheduleResponse

interface HomeDsnContract {
    interface ViewContract : BaseView<HomeDsnPresenter> {
        fun startBeaconActivity(kodeRuangan : String, macAddress : String)
        fun startMhsListActivity()
        fun onRegulerScheduleListLoaded()
        fun onAltScheduleListLoaded()
        fun showSnackBar(message: String)
        fun showDialog(title: String, message: String)
        fun refreshRegulerList()
        fun refreshAltList()
        fun startJwlPenggantiBtmSheet()
        fun checkAllRequirement() : Boolean
        fun handleNoRegulerScheduleFound()
        fun handleNoAltScheduleFound()
        fun openStartScheduleBtmSheet(dsnSchedule: DsnSchedule)
    }

    interface JwlPenggantiBtmSheetViewContract {
        fun initKelasArrayAdapter(data : List<String>)
        fun onRoomDataReady(data : List<String>)
        fun onMatkulListReady(data : List<String>)
        fun onSessionDataReady(data : List<String>)
        fun handleNoSessionFound()
        fun setUpConfirmButton()
    }

    interface InteractorContract{
        fun onScheduleListResult(data : ScheduleResponse<DsnSchedule>)
        fun onMatkulListResult(data : List<Matkul>)
        fun onSessionListAvailableResult(data : HashMap<Int,String>)
        fun onRoomListAvailableResult(data : List<String>)
        fun onFail(error : String?)
    }

    interface ItemViewContract{
        fun setStartTime(startTime : String)
        fun setEndTime(endTime : String)
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
        fun setStatusMatkul(statusMatkul : String)
        fun setStatusMatkulColor(colorCode : String)
        fun setArrowButtonClickListener(item : DsnSchedule, clickListener: (DsnSchedule) -> Unit)
        fun hidePresenceButton()
    }
}