package com.example.attendance_mobile.home.homedosen

import com.example.attendance_mobile.data.DsnSchedule
import com.example.attendance_mobile.data.JwlPengganti
import com.example.attendance_mobile.data.Matkul
import com.example.attendance_mobile.data.response.ScheduleResponse
import com.example.attendance_mobile.home.homedosen.viewholder.ScheduleDsnViewHolder
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils
import java.util.*
import kotlin.collections.HashMap

class HomeDsnPresenter(
    private val view: HomeDsnContract.ViewContract,
    private val remoteRepository: RemoteRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : HomeDsnContract.InteractorContract {
    private var jwlPenggantiBottomSheetView : HomeDsnContract.JwlPenggantiBtmSheetViewContract? = null
    private var regulerList: List<DsnSchedule> = emptyList()
    private var altList: List<DsnSchedule> = emptyList()
    private var matkulList : List<Matkul> = emptyList()
    private var ruanganList : List<String> = emptyList()
    private var session : HashMap<Int,String> = HashMap()
    private lateinit var jwlPengganti: JwlPengganti
    private var matkulPos : Int = -1

    fun initAndAttachJwlPenggantiBottomSheet(v : HomeDsnContract.JwlPenggantiBtmSheetViewContract){
        jwlPenggantiBottomSheetView = v
        jwlPengganti = JwlPengganti("","","",-1,"")
    }

    fun onBindAltScheduleItem(position: Int, viewHolder: ScheduleDsnViewHolder){
        val schedule = altList[position]
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
                    setArrowButtonClickListener(schedule) {
                        if (schedule.jamMulaiOlehDosen != "") {
//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {

                                // }
                                view.startMhsListActivity()
                        } else {
                            view.openStartScheduleBtmSheet(schedule)
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
    fun onBindRegulerScheduleItem(position: Int, viewHolder: ScheduleDsnViewHolder) {
        val schedule = regulerList[position]
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
                    setArrowButtonClickListener(schedule) {
                        currentTime = Calendar.getInstance().time
                        if (schedule.jamMulaiOlehDosen != "") {
                            if (currentTime < TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTimeByDosen!!)
                            ) {
//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {
                                if (view.checkAllRequirement()) {
                                    //localRepository.insert(LocalAttendance("TEST1", 1, "KLKL", "XXXLL", "OPOP", "APAPA", 0, "MAC"))
                                }
                                // }
                            } else {
                                view.startMhsListActivity()
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
                    setArrowButtonClickListener(schedule) {
                        currentTime = Calendar.getInstance().time
                        if (schedule.jamMulaiOlehDosen != "") {
                            if (currentTime < TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTimeByDosen!!)
                            ) {
//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {
                                if (view.checkAllRequirement()) {
                                    //localRepository.insert(LocalAttendance("TEST1", 1, "KLKL", "XXXLL", "OPOP", "APAPA", 0, "MAC"))
                                    view.openStartScheduleBtmSheet(schedule)
                                }
                                // }
                            } else {
                                view.startMhsListActivity()
                            }
                        } else {
                            view.openStartScheduleBtmSheet(schedule)
                        }
                    }
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

    fun altSize() : Int {
        return altList.size
    }

    fun doFetchScheduleList() {
        val kelas = sharedPreferenceHelper.getSharedPreferenceString("kelas", "")
        remoteRepository.doFetchDsnScheduleList(kelas!!, "22-05-2019", this)
    }

    fun onConfirmJwlPengganti(){
        remoteRepository.doRequestJwlPengganti(jwlPengganti)
    }

    fun doFetchMatkulList(){
        val kddsn = sharedPreferenceHelper.getSharedPreferenceString("kddsn", "")!!
        remoteRepository.doFetchMatkulList(kddsn,this)
    }

    fun onSelectMatkulItem(position: Int){
        matkulPos = position
        jwlPengganti.kodeMatkul = matkulList[position].kodeMatkul
        jwlPenggantiBottomSheetView?.initKelasArrayAdapter(matkulList[position].listKelas)
    }

    fun onSelectRuanganItem(position : Int){
        jwlPengganti.kodeRuangan = ruanganList[position]
    }

    fun onSelectKelasItem(position : Int){
        jwlPengganti.kelas = matkulList[matkulPos].listKelas[position]
    }

    fun doFetchSessionList(tgl : String){
        jwlPengganti.tgl = tgl
        remoteRepository.doFetchSessionList(tgl,jwlPengganti.kelas,this)
    }

    fun doFetchRoomList(time : String){
        val selectedSession = session.filter { item -> item.value == time }.keys.firstOrNull()
        if(selectedSession != null) {
            jwlPengganti.sesi = selectedSession
            remoteRepository.doFetchJwlPenggantiRoomList(time, selectedSession, this)
        }
    }

    override fun onScheduleListResult(data: ScheduleResponse<DsnSchedule>) {
        if (data.jadwalReguler.size == 0) {
            view.handleNoRegulerScheduleFound()
        }else{
            regulerList = data.jadwalReguler
            view.apply {
                refreshRegulerList()
                onRegulerScheduleListLoaded()
            }
        }

        if(data.jadwalPengganti.size == 0) {
            view.handleNoAltScheduleFound()
        }else{
            altList = data.jadwalPengganti
            view.apply {
                refreshAltList()
                onAltScheduleListLoaded()
            }
        }
    }

    override fun onMatkulListResult(data: List<Matkul>) {
        matkulList = data
        val listOfMatkul = data.map { item -> item.namaMatkul }
        jwlPenggantiBottomSheetView?.onMatkulListReady(listOfMatkul)
    }

    override fun onSessionListAvailableResult(data: HashMap<Int, String>) {
        if(data.size == 0){
            jwlPenggantiBottomSheetView?.handleNoSessionFound()
        }else {
            session = data
            val listOfTime = data.map { item -> item.value }
            jwlPenggantiBottomSheetView?.onSessionDataReady(listOfTime)
        }
    }

    override fun onRoomListAvailableResult(data: List<String>) {
        ruanganList = data
        jwlPenggantiBottomSheetView?.onRoomDataReady(data)
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }

}