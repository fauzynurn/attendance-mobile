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
    private var scheduleList: MutableList<JadwalMhs> = mutableListOf()
//    private var altList: List<JadwalMhs> = emptyList()

    fun setupHomescreen() {
        val name = sharedPreferenceHelper.getSharedPreferenceString("nama", "")
        view.setName(name!!)
//        view.setName("Fauzi Nur Noviansyah")
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
        remoteRepository.doFetchSummary(nim!!, TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy"),TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"HH:mm") + ":00",this)
    }

    fun onBindScheduleItem(position: Int, viewHolder: ScheduleMhsViewHolder) {
        val schedule: JadwalMhs = scheduleList[position]

        viewHolder.apply {
            setStartTime(schedule.jamMulai.substring(0, schedule.jamMulai.length - 3))
            setEndTime(schedule.jamSelesai.substring(0, schedule.jamSelesai.length - 3))
            setNamaMatkul(schedule.namaMatkul)
        }

        if (schedule.jenisMatkul) viewHolder.setJenisMatkul(Constants.TEORI) else viewHolder.setJenisMatkul(Constants.PRAKTEK)
    }

    fun scheduleSize(): Int {
        return scheduleList.size
    }

    //    private fun onScheduleClick(scheduleItem : JadwalMhs){
//        if(!permissionManager.isAutoTimeActive()){
//            view.showDialog(Constants.AUTO_TIME_DISABLED_MESSAGE)
//        }else{
//            view.startBeaconActivity(scheduleItem.kodeRuangan)
//        }
//    }
    fun doFetchScheduleList() {
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim", "")
        remoteRepository.doFetchMhsScheduleList(nim!!, TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy"), this)
    }

    fun bindOngoingSchedule(schedule: JadwalMhs) {
        val startTimeByDosen: Date? = TimeUtils.convertStringToDate(schedule.jamMulaiOlehDosen)
        view.apply {
            setOngoingMatkulName(schedule.namaMatkul)
            if (schedule.jenisMatkul) setOngoingJenisMatkul("Teori") else setOngoingJenisMatkul("Praktek")
            setOngoingTime(
                "${schedule.jamMulai.substring(
                    0,
                    schedule.jamMulai.length - 3
                )} - ${schedule.jamSelesai.substring(0, schedule.jamMulai.length - 3)}"
            )
        }
        if (schedule.jamMulaiOlehDosen != "") {
            val currentTime = Calendar.getInstance().time

//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {
            // }
            if (!schedule.listSesi.any { sesi -> sesi.status }) {
                if (currentTime < TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTimeByDosen!!)) {
                    view.setPresenceButtonClickListener(schedule) {
                        if (view.checkAllRequirement()) {
                            //localRepository.insert(LocalAttendance("TEST1", 1, "KLKL", "XXXLL", "OPOP", "APAPA", 0, "MAC"))
                            localRepository.saveBulkSchedule(schedule)
                            view.startBeaconActivity(schedule.ruangan)
                        }
                    }
                } else {
                    view.setPresenceButtonClickListener(schedule) {
                        view.showDialog(
                            "Waktu pencatatan kehadiran sudah habis",
                            "Silahkan menunggu sesi berikutnya"
                        )
                    }
                }
            } else {
                view.setBtnLabel("Lihat kehadiran")
                view.setPresenceButtonClickListener(schedule) {
                    view.startDetailPerSessionActivity(schedule)
                }
            }
        } else {
            view.setPresenceButtonClickListener(schedule) {
                view.showDialog(
                    "Kelas belum dimulai oleh dosen",
                    "Silahkan hubungi dosen yang bersangkutan untuk memulai kelas."
                )
            }
        }
    }

    override fun onScheduleListResult(data: ScheduleResponse<JadwalMhs>) {
        val currentTime: Date = Calendar.getInstance().time
        scheduleList.addAll(data.jadwalReguler)
        scheduleList.addAll(data.jadwalPengganti)

        if (scheduleList.isEmpty()) {
            view.handleNoMhsScheduleFound()
            view.handleNoOngoingMhsScheduleFound()
        } else {
            view.apply {
                refreshScheduleList()
                onMhsScheduleListLoaded()
            }
            val schedule: JadwalMhs? = scheduleList.find {
                currentTime >= TimeUtils.convertStringToDate(it.jamMulai)!! && currentTime < TimeUtils.convertStringToDate(
                    it.jamSelesai
                )
            }
            if (schedule == null) {
                view.handleNoOngoingMhsScheduleFound()
            } else {
                //remove ongoing schedule from list
                scheduleList = scheduleList.filter {
                    !(currentTime >= TimeUtils.convertStringToDate(it.jamMulai)!! && currentTime < TimeUtils.convertStringToDate(
                        it.jamSelesai
                    ))
                } as MutableList<JadwalMhs>
                bindOngoingSchedule(schedule)
                if(scheduleList.isEmpty()){
                    view.handleNoMhsScheduleFound()
                }
                view.apply {
                    onOngoingMhsScheduleLoaded()
                }
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