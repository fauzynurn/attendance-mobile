package com.example.attendance_mobile.home.homedosen.startclass.beaconscan

import com.example.attendance_mobile.model.BaseBeaconInteractor
import com.example.attendance_mobile.model.service.BeaconService

interface BeaconBtmSheetContract {
    interface ViewContract{
        fun stopService()
        fun startService(macAddress : String)
        fun closeBtmSheet()
        fun reloadList()
        fun registerReceiver(receiver : BeaconService.BeaconReceiver<BeaconContract>)
    }

    interface BeaconContract : BaseBeaconInteractor
}
