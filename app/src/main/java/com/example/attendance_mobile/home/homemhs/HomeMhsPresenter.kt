package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.data.ScheduleMhs
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.Repository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils
import java.util.*
import kotlin.collections.ArrayList

class HomeMhsPresenter(private val view : HomeMhsContract.ViewContract,
                       private val repository: Repository,
                       private val permissionManager: PermissionManager,
                       private val sharedPreferenceHelper: SharedPreferenceHelper) : HomeMhsContract.InteractorContract {
    private var list : ArrayList<ScheduleMhs> = ArrayList()

    fun doFetchSummaryData(){
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim","")
        repository.doFetchSummary(nim!!,this)
    }

    fun onBindScheduleItem(position : Int, viewHolder: ScheduleAdapter.ScheduleViewHolder){
        val schedule : ScheduleMhs = list[position]
        var currentTime: Date = Calendar.getInstance().time
        val startTime: Date = TimeUtils.convertStringToDate(schedule.jamMulai)

        viewHolder.apply {
            setStartTime(schedule.jamMulai)
            setEndTime(schedule.jamSelesai)
            setNamaMatkul(schedule.namaMatkul)
        }

        if (schedule.jenisMatkul == 1) viewHolder.setJenisMatkul(Constants.TEORI) else viewHolder.setJenisMatkul(Constants.PRAKTEK)

        when {
            currentTime >= startTime && currentTime < TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTime) -> {
                viewHolder.apply{
                    setStatusMatkul("Sedang berjalan")
                    setStatusMatkulColor("#efdd3e")
                    setPresenceButtonClickListener(schedule) {
                        currentTime = Calendar.getInstance().time
                        if(currentTime < TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTime)){
                            if(!permissionManager.isAutoTimeActive()){
                                view.showDialog("Pencatatan Presensi Gagal",Constants.AUTO_TIME_DISABLED_MESSAGE)
                            }else{
                                view.startBeaconActivity(schedule.kodeRuangan,schedule.macAddress)
                            }
                        }else{
                            view.showDialog("Mata kuliah sudah berakhir","Proses pencatatan kehadiran tidak dapat dilakukan.")
                        }
                    }
                }
            }
            currentTime > TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTime) -> {
                viewHolder.apply{
                    setStatusMatkul("Tidak hadir")
                    setStatusMatkulColor("#ef463e")
                    hidePresenceButton()
                }
            }
            currentTime < startTime -> {
                viewHolder.apply{
                    setStatusMatkul("Belum dimulai")
                    setStatusMatkulColor("#bcbcbc")
                    hidePresenceButton()
                }
            }
        }
    }

    fun size() : Int{
        return list.size
    }

//    private fun onScheduleClick(scheduleItem : ScheduleMhs){
//        if(!permissionManager.isAutoTimeActive()){
//            view.showDialog(Constants.AUTO_TIME_DISABLED_MESSAGE)
//        }else{
//            view.startBeaconActivity(scheduleItem.kodeRuangan)
//        }
//    }
    fun doFetchScheduleList(){
        val kelas = sharedPreferenceHelper.getSharedPreferenceString("kelas","")
        repository.doFetchScheduleList(kelas!!,this)
    }

    override fun onScheduleListResult(data : ArrayList<ScheduleMhs>) {
        list = data
        view.apply {
            refreshList()
            onScheduleListLoaded()
        }
    }

    override fun onSummaryResult(summaryData: HashMap<String, Int>) {
        view.onSummaryDataLoaded(summaryData)
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }
}