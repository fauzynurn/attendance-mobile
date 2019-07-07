package com.example.attendance_mobile.home.homedosen.startclass.startschedule

import android.util.Log
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.response.RoomAvailableResponse
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.TimeUtils

class StartScheduleBtmSheetPresenter(var dsnSchedule: JadwalDsn, val view : StartScheduleBtmSheetContract.ViewContract, val remoteRepository: RemoteRepository) : StartScheduleBtmSheetContract.InteractorContract{
    lateinit var roomList : List<String>

    fun doFetchRoomList(){
        remoteRepository.doFetchStartClassRoomList(TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy"),TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy"),dsnSchedule.jamMulai,dsnSchedule.kelas,dsnSchedule.namaMatkul, dsnSchedule.jenisMatkul.toString(),this)
    }

    override fun onRoomListResult(data: RoomAvailableResponse) {
        roomList = data.ruanganKosong
        view.initRoomAdapter(data.ruanganKosong)
    }

    fun onSelectRoomItem(position : Int){
        dsnSchedule.ruangan.kodeRuangan = roomList[position]
    }

    override fun onFail(message: String) {
        Log.i("SSS","NOT IMPLEMENTED YET")
    }
}