package com.example.attendance_mobile.home.homedosen.startclass.startschedule

import android.util.Log
import com.example.attendance_mobile.data.DsnSchedule
import com.example.attendance_mobile.data.Ruangan
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.TimeUtils

class StartScheduleBtmSheetPresenter(var dsnSchedule: DsnSchedule, val view : StartScheduleBtmSheetContract.ViewContract,val remoteRepository: RemoteRepository) : StartScheduleBtmSheetContract.InteractorContract{
    lateinit var roomList : List<Ruangan>

    fun doFetchRoomList(){
        remoteRepository.doFetchAllRoom(this)
    }

    fun doFetchRoomFromTime(jamMulai : String){
        val currentDate = TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy")
        remoteRepository.doFetchRoomListFromTime(
            currentDate,jamMulai,this
        )
    }

    fun doFetchTimeFromRoom(position : Int){
        dsnSchedule.ruangan = roomList[position]
        val currentDate = TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy")
        remoteRepository.doFetchTimeListFromRoom(currentDate,dsnSchedule.ruangan.kodeRuangan,this)
    }

    fun onRoomChipSelected(kodeRuangan : String){
        val ruangan = roomList.find { item -> item.kodeRuangan == kodeRuangan }
        if(ruangan != null) {
            dsnSchedule.ruangan = ruangan
        }
    }

    fun onTimeChipSelected(jamMulai: String){
        dsnSchedule.jamMulai = jamMulai
    }

    override fun onRoomListResult(data: List<Ruangan>) {
        roomList = data
        view.initRoomAdapter(data.map { ruangan -> ruangan.kodeRuangan })
    }
    override fun onTimeListFromRoomResult(data: List<String>) {
        view.onTimeDataReady(data)
    }

    override fun onRoomListFromTimeResult(data: List<Ruangan>) {
        view.onRoomDataReady(data.map { ruangan -> ruangan.kodeRuangan })
    }

    override fun onFail(message: String) {
        Log.i("SSS","NOT IMPLEMENTED YET")
    }
}