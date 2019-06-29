package com.example.attendance_mobile.home.homedosen.startclass.beaconscan

import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.model.service.BeaconService

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
        view.reloadList()

        remoteRepository.startClass("27-06-2019","09:32:00",jadwalDsn.ruangan.kodeRuangan,jadwalDsn.kelas,jadwalDsn.jenisMatkul,jadwalDsn.kodeMatkul)
    }

    override fun onTimeout() {
        view.closeBtmSheet()
    }
}