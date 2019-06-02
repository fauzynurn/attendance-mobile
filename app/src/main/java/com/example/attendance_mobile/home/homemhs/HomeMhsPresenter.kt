package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.data.Schedule
import com.example.attendance_mobile.data.ScheduleResponse
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils
import java.util.*
import kotlin.collections.ArrayList

class HomeMhsPresenter(
    private val view: HomeMhsContract.ViewContract,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : HomeMhsContract.InteractorContract {
    private var list: ArrayList<Schedule> = ArrayList()

    fun doFetchSummaryData() {
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim", "")
//        localRepository.insert(
//            Attendance(
//                2,
//                "z",
//                "b",
//                "c",
//                0,
//                "d",
//                "v"
//            )
//        )
//        localRepository.saveBulkSchedule(
//            Schedule(
//                "Pengolahan Citra Digital",
//                true,
//                "16TKO6022",
//                "07:00:00",
//                "08:40:00",
//                "D219",
//                "C2:00:E2:00:00:6A",
//                "07:05:00",
//                arrayListOf(Sesi(1, "07:00:00", "07:50:00"), Sesi(2, "07:50:00", "08:40:00"))
//            )
//        )
        remoteRepository.doFetchSummary(nim!!, this)
    }

    fun onBindScheduleItem(position: Int, viewHolder: ScheduleAdapter.ScheduleViewHolder) {
        val schedule: Schedule = list[position]
        var currentTime: Date = Calendar.getInstance().time
        val startTime: Date = TimeUtils.convertStringToDate(schedule.jamMulai)!!
        val startTimeByDosen: Date? = TimeUtils.convertStringToDate(schedule.jamMulaiOlehDosen)
        val endTime: Date = TimeUtils.convertStringToDate(schedule.jamSelesai)!!

        viewHolder.apply {
            setStartTime(schedule.jamMulai.substring(0, schedule.jamMulai.length - 3))
            setEndTime(schedule.jamSelesai.substring(0, schedule.jamSelesai.length - 3))
            setNamaMatkul(schedule.namaMatkul)
        }

        if (schedule.jenisMatkul) viewHolder.setJenisMatkul(Constants.TEORI) else viewHolder.setJenisMatkul(Constants.PRAKTEK)

        when {
            currentTime >= startTime && currentTime < endTime -> {
                viewHolder.apply {
                    setStatusMatkul("Sedang berjalan")
                    setStatusMatkulColor("#efdd3e")
                    setPresenceButtonClickListener(schedule) {
                        currentTime = Calendar.getInstance().time
                        if (schedule.jamMulaiOlehDosen != "") {
                            if (currentTime < TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTimeByDosen!!)) {
//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {
                                if (view.checkAllRequirement()) {
                                    //localRepository.insert(Attendance("TEST1", 1, "KLKL", "XXXLL", "OPOP", "APAPA", 0, "MAC"))
                                    localRepository.saveBulkSchedule(schedule)
                                    view.startBeaconActivity(schedule.kodeRuangan, schedule.macAddress)
                                }
                                // }
                            } else {
                                view.showDialog(
                                    "Masa berlaku untuk melakukan presensi kehadiran telah berakhir",
                                    "Proses pencatatan kehadiran tidak dapat dilakukan."
                                )
                            }
                        } else {
                            view.showDialog(
                                "Kelas belum dimulai oleh dosen",
                                "Silahkan hubungi dosen yang bersangkutan untuk memulai kelas."
                            )
                        }
                    }
                }
            }
            currentTime > endTime -> {
                viewHolder.apply {
                    setStatusMatkul("Sudah selesai")
                    setStatusMatkulColor("#ef463e")
                    hidePresenceButton()
                }
            }
            currentTime < startTime -> {
                viewHolder.apply {
                    setStatusMatkul("Belum dimulai")
                    setStatusMatkulColor("#bcbcbc")
                    hidePresenceButton()
                }
            }
        }
    }

//    fun mergeSchedule(list: ArrayList<Schedule>): ArrayList<Schedule> {
//        val newList: java.util.ArrayList<Schedule> = java.util.ArrayList()
//        list.forEach { item ->
//            if (!newList.any { x -> x.namaMatkul == item.namaMatkul }) {
//                newList.add(item)
//            } else {
//                newList.find { x -> x.namaMatkul == item.namaMatkul }!!.jamSelesai = item.jamSelesai
//            }
//        }
//        return newList
//    }

    fun size(): Int {
        return list.size
    }

    //    private fun onScheduleClick(scheduleItem : Schedule){
//        if(!permissionManager.isAutoTimeActive()){
//            view.showDialog(Constants.AUTO_TIME_DISABLED_MESSAGE)
//        }else{
//            view.startBeaconActivity(scheduleItem.kodeRuangan)
//        }
//    }
    fun doFetchScheduleList() {
        val kelas = sharedPreferenceHelper.getSharedPreferenceString("kelas", "")
        remoteRepository.doFetchScheduleList(kelas!!, "22-05-2019", this)
    }

    override fun onScheduleListResult(data: ScheduleResponse) {
        if (data.jadwalReguler.size == 0) {
            view.handleNoScheduleFound()
        } else {
            list = data.jadwalReguler
            view.apply {
                refreshList()
                onScheduleListLoaded()
            }
        }
    }

    override fun onSummaryResult(summaryData: HashMap<String, Int>) {
        view.onSummaryDataLoaded(summaryData)
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }
}