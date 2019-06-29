package com.example.attendance_mobile.home.homedosen

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.response.*

interface HomeDsnContract {
    interface ViewContract : BaseView<HomeDsnPresenter> {
        fun startBeaconActivity(kodeRuangan : String, macAddress : String)
        fun startMhsListActivity()
        fun onRegulerScheduleListLoaded()
        fun setName(name : String)
        fun onAltScheduleListLoaded()
        fun showSnackBar(message: String)
        fun showDialog(title: String, message: String)
        fun refreshRegulerList()
        fun refreshAltList()
        fun startJwlPenggantiBtmSheet()
        fun checkAllRequirement() : Boolean
        fun handleNoRegulerScheduleFound()
        fun handleNoAltScheduleFound()
        fun openStartScheduleBtmSheet(dsnSchedule: JadwalDsn)
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
        fun onScheduleListResult(data : ScheduleResponse<JadwalDsn>)
        fun onMatkulListResult(data : MatkulListResponse)
        fun onSessionListAvailableResult(data : TimeAvailableResponse)
        fun onRoomListAvailableResult(data : RoomAvailableResponse)
        fun onJwlPenggantiCreated(response : BaseResponse)
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
        fun setArrowButtonClickListener(item : JadwalDsn, clickListener: (JadwalDsn) -> Unit)
        fun hidePresenceButton()
    }
}