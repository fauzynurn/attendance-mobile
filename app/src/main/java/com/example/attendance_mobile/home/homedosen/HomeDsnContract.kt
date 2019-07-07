package com.example.attendance_mobile.home.homedosen

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.response.ScheduleResponse

interface HomeDsnContract {
    interface ViewContract : BaseView<HomeDsnPresenter> {
        fun startBeaconActivity(kodeRuangan : String, macAddress : String)
        fun startMhsListActivity(kelas : String)
        fun onDsnScheduleListLoaded()
        fun onOngoingDsnScheduleLoaded()
        fun setName(name : String)
        fun showSnackBar(message: String)
        fun showDialog(title: String, message: String)
        fun refreshScheduleList()
        fun checkAllRequirement() : Boolean
        fun handleNoDsnScheduleFound()
        fun handleNoOngoingDsnScheduleFound()
        fun openStartScheduleBtmSheet(dsnSchedule: JadwalDsn)

        fun setOngoingMatkulName(name : String)
        fun setOngoingJenisMatkul(jenisMatkul : String)
        fun setOngoingTime(startTime : String)
        fun setOngoingKelas(kelasString : String)
        fun setBtnLabel(label : String)
        fun setPresenceButtonClickListener(item : JadwalDsn, clickListener: (JadwalDsn) -> Unit)
    }

    interface InteractorContract{
        fun onScheduleListResult(data : ScheduleResponse<JadwalDsn>)
        fun onFail(error : String?)
    }

    interface ItemViewContract{
        fun setKelas(kelas : String)
        fun setStartTime(startTime : String)
        fun setEndTime(endTime : String)
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
    }
}