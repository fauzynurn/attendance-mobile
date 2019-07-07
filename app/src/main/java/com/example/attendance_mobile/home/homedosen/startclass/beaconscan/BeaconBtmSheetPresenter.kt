package com.example.attendance_mobile.home.homedosen.startclass.beaconscan

import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.model.service.BeaconService
import com.example.attendance_mobile.utils.TimeUtils

class BeaconBtmSheetPresenter(val view : BeaconBtmSheetContract.ViewContract,
                              val remoteRepository: RemoteRepository,
                              val jadwalDsn: JadwalDsn) : BeaconBtmSheetContract.BeaconContract{

    fun startRanging(macAddress : String){
        PermissionManager.switchBluetoothState()
        view.registerReceiver(BeaconService.BeaconReceiver(this))
        view.startService(macAddress)
    }

    fun cancelBeaconRanging(){
        view.closeBtmSheet()
        view.stopService()
    }

    override fun onBeaconFound() {
        view.closeBtmSheet()
        PermissionManager.switchBluetoothState()
        view.stopService()

        remoteRepository.startClass(TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy"),
            TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "HH:mm") + ":00",
            jadwalDsn.kodeMatkul
            ,jadwalDsn.kelas,
            jadwalDsn.jenisMatkul,
            jadwalDsn.ruangan.kodeRuangan,
            {
                if(it.status == "200") view.reloadList() else view.showSnackBarOnParent(it.status + " : Failed for some reason.")
            },{
                view.showSnackBarOnParent(it)
            })
    }

    override fun onTimeout() {
        view.closeBtmSheet()
    }
}