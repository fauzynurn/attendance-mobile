package com.example.attendance_mobile.home.homedosen

import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.response.ScheduleResponse
import com.example.attendance_mobile.home.homedosen.viewholder.ScheduleDsnViewHolder
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils
import java.util.*

class HomeDsnPresenter(
    private val view: HomeDsnContract.ViewContract,
    private val remoteRepository: RemoteRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : HomeDsnContract.InteractorContract {
    private var scheduleList: MutableList<JadwalDsn> = mutableListOf()

    fun setupHomescreen() {
        val name = sharedPreferenceHelper.getSharedPreferenceString("nama", "")
        view.setName(name!!)
    }

    fun onBindScheduleItem(position: Int, viewHolder: ScheduleDsnViewHolder) {
        val schedule: JadwalDsn = scheduleList[position]

        viewHolder.apply {
            setStartTime(schedule.jamMulai.substring(0, schedule.jamMulai.length - 3))
            setEndTime(schedule.jamSelesai.substring(0, schedule.jamSelesai.length - 3))
            setNamaMatkul(schedule.namaMatkul)
            setKelas(schedule.kelas)
            if (schedule.jenisMatkul) setJenisMatkul(Constants.TEORI) else setJenisMatkul(Constants.PRAKTEK)
        }
    }

    fun scheduleSize(): Int {
        return scheduleList.size
    }

    fun doFetchScheduleList() {
        val kodeDosen = sharedPreferenceHelper.getSharedPreferenceString("kddsn", "")
        remoteRepository.doFetchDsnScheduleList(
            kodeDosen!!,
            TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy"),
            this
        )
    }

    fun bindOngoingSchedule(schedule: JadwalDsn) {
        view.apply {
            setOngoingMatkulName(schedule.namaMatkul)
            if (schedule.jenisMatkul) setOngoingJenisMatkul("Teori") else setOngoingJenisMatkul("Praktek")
            setOngoingTime(
                "${schedule.jamMulai.substring(
                    0,
                    schedule.jamMulai.length - 3
                )} - ${schedule.jamSelesai.substring(0, schedule.jamMulai.length - 3)}"
            )
            setOngoingKelas(schedule.kelas)
        }
        // if (schedule.jamMulaiOlehDosen == "") setStatusMatkul("Belum dimulai") else setStatusMatkul("Sudah dimulai")
        if (schedule.jamMulaiOlehDosen == "") {
//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {
            view.setPresenceButtonClickListener(schedule) {
                if (view.checkAllRequirement()) {
                    view.openStartScheduleBtmSheet(schedule)
                }
            }
            // }
        } else {
            view.setBtnLabel("Lihat kehadiran mahasiswa")
            view.setPresenceButtonClickListener(schedule) {
                view.startMhsListActivity(schedule.kelas)
            }
        }
    }

    override fun onScheduleListResult(data: ScheduleResponse<JadwalDsn>) {
        val currentTime: Date = Calendar.getInstance().time
        scheduleList.clear()
        scheduleList.addAll(data.jadwalReguler)
        scheduleList.addAll(data.jadwalPengganti)

        if (scheduleList.isEmpty()) {
            view.handleNoDsnScheduleFound()
            view.handleNoOngoingDsnScheduleFound()
        } else {
            view.apply {
                refreshScheduleList()
                onDsnScheduleListLoaded()
            }
            val schedule: JadwalDsn? = scheduleList.find {
                currentTime >= TimeUtils.convertStringToDate(it.jamMulai)!! && currentTime < TimeUtils.convertStringToDate(
                    it.jamSelesai
                )
            }
            if (schedule == null) {
                view.handleNoOngoingDsnScheduleFound()
            } else {
                //remove ongoing schedule from list
                scheduleList = scheduleList.filter {
                    !(currentTime >= TimeUtils.convertStringToDate(it.jamMulai)!! && currentTime < TimeUtils.convertStringToDate(
                        it.jamSelesai
                    ))
                } as MutableList<JadwalDsn>
                bindOngoingSchedule(schedule)
                if(scheduleList.isEmpty()){
                    view.handleNoDsnScheduleFound()
                }
                view.apply {
                    onOngoingDsnScheduleLoaded()
                }
            }
        }
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }

}