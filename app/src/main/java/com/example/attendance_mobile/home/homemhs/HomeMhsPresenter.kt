package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.response.ScheduleResponse
import com.example.attendance_mobile.home.homemhs.viewholder.ScheduleMhsViewHolder
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils
import java.util.*

class HomeMhsPresenter(
    private val view: HomeMhsContract.ViewContract,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : HomeMhsContract.InteractorContract {
    private var regulerList: List<JadwalMhs> = emptyList()
    private var altList: List<JadwalMhs> = emptyList()

    fun setupHomescreen(){
        val name = sharedPreferenceHelper.getSharedPreferenceString("nama","")
        view.setName(name!!)
    }

    fun doFetchSummaryData() {
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim", "")
//        localRepository.insert(
//            LocalAttendance(
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
//            JadwalMhs(
//                "Pengolahan Citra Digital",
//                true,
//                "16TKO6022",
//                "07:00:00",
//                "08:40:00",
//                "D219",
//                "C2:00:E2:00:00:6A",
//                "07:05:00",
//                arrayListOf(Kehadiran(1, "07:00:00", "07:50:00"), Kehadiran(2, "07:50:00", "08:40:00"))
//            )
//        )
        remoteRepository.doFetchSummary(nim!!, this)
    }

    fun onBindScheduleItem(position: Int, viewHolder: ScheduleMhsViewHolder, type: Int) {
        val schedule: JadwalMhs = if (type == 1) {
            regulerList[position]
        } else {
            altList[position]
        }
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
                    if(schedule.jamMulaiOlehDosen == "") setStatusMatkul("Belum dimulai") else setStatusMatkul("Sudah dimulai")
                    setPresenceButtonClickListener(schedule) {
                        currentTime = Calendar.getInstance().time
                        if (schedule.jamMulaiOlehDosen != "") {
                            if (currentTime < TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTimeByDosen!!)
                            ) {
                                if (!schedule.listSesi.any { sesi -> sesi.status }) {
                                    if (view.checkAllRequirement()) {
                                        //localRepository.insert(LocalAttendance("TEST1", 1, "KLKL", "XXXLL", "OPOP", "APAPA", 0, "MAC"))
                                        localRepository.saveBulkSchedule(schedule)
                                        view.startBeaconActivity(schedule.ruangan)
                                    }
                                } else {
                                    view.startDetailPerSessionActivity(schedule)
                                }
//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {
                                // }
                            } else {
                                view.showDialog(
                                    "Waktu pencatatan kehadiran sudah habis",
                                    "Silahkan menunggu sesi berikutnya"
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

    fun regulerSize(): Int {
        return regulerList.size
    }

    fun altSize(): Int {
        return altList.size
    }

    //    private fun onScheduleClick(scheduleItem : JadwalMhs){
//        if(!permissionManager.isAutoTimeActive()){
//            view.showDialog(Constants.AUTO_TIME_DISABLED_MESSAGE)
//        }else{
//            view.startBeaconActivity(scheduleItem.kodeRuangan)
//        }
//    }
    fun doFetchScheduleList() {
        val kelas = sharedPreferenceHelper.getSharedPreferenceString("kelas", "")
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim", "")
        remoteRepository.doFetchMhsScheduleList(nim!!, kelas!!, "27-06-2019", this)
    }

    override fun onScheduleListResult(data: ScheduleResponse<JadwalMhs>) {
        if (data.jadwalReguler.size == 0) {
            view.handleNoRegulerScheduleFound()
        } else {
            regulerList = data.jadwalReguler
            view.apply {
                refreshRegulerList()
                onRegulerScheduleListLoaded()
            }
        }

        if (data.jadwalPengganti.size == 0) {
            view.handleNoAltScheduleFound()
        } else {
            altList = data.jadwalPengganti
            view.apply {
                refreshAltList()
                onAltScheduleListLoaded()
            }
        }
    }

    override fun onSummaryResult(summaryData: HashMap<String, String>) {
        view.onSummaryDataLoaded(summaryData)
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }
}