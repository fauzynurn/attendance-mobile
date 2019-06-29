package com.example.attendance_mobile.home.homedosen

import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.Matkul
import com.example.attendance_mobile.data.request.JwlPenggantiRequest
import com.example.attendance_mobile.data.response.*
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
    private var jwlPenggantiBottomSheetView: HomeDsnContract.JwlPenggantiBtmSheetViewContract? = null
    private var regulerList: List<JadwalDsn> = emptyList()
    private var altList: List<JadwalDsn> = emptyList()
    private var matkulList: List<Matkul> = emptyList()
    private var ruanganList: List<String> = emptyList()
    private lateinit var session: List<String>
    private lateinit var jwlPengganti: JwlPenggantiRequest
    private var matkulPos: Int = -1

    fun setupHomescreen(){
        val name = sharedPreferenceHelper.getSharedPreferenceString("nama","")
        view.setName(name!!)
    }

    fun initAndAttachJwlPenggantiBottomSheet(v: HomeDsnContract.JwlPenggantiBtmSheetViewContract) {
        jwlPenggantiBottomSheetView = v
        jwlPengganti = JwlPenggantiRequest("", "", "", "", "", "","")
    }

    fun onBindScheduleItem(position: Int, viewHolder: ScheduleDsnViewHolder, type: Int) {
        val schedule: JadwalDsn = if (type == 1) {
            regulerList[position]
        } else {
            altList[position]
        }
        var currentTime: Date = Calendar.getInstance().time
        val startTime: Date = TimeUtils.convertStringToDate(schedule.jamMulai)!!
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
                    highlightItem()
                    setArrowButtonClickListener(schedule) {
                        currentTime = Calendar.getInstance().time
                        if (schedule.jamMulaiOlehDosen == "") {
//                            if (!permissionManager.isAutoTimeActive()) {
//                                view.showDialog("Pencatatan Presensi Gagal", Constants.AUTO_TIME_DISABLED_MESSAGE)
//                            } else {
                            if (view.checkAllRequirement()) {
                                view.openStartScheduleBtmSheet(schedule)
                            }
                            // }
                        } else {
                            view.startMhsListActivity()
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

    fun doFetchScheduleList() {
        val kodeDosen = sharedPreferenceHelper.getSharedPreferenceString("kddsn","")
        remoteRepository.doFetchDsnScheduleList(kodeDosen!!, TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy"), this)
    }

    fun onConfirmJwlPengganti() {
        remoteRepository.doRequestJwlPengganti(jwlPengganti,this)
    }

    fun doFetchMatkulList() {
        val kddsn = sharedPreferenceHelper.getSharedPreferenceString("kddsn", "")!!
        remoteRepository.doFetchMatkulList(kddsn, this)
    }

    fun onSelectMatkulItem(position: Int) {
        matkulPos = position
        jwlPengganti.namaMatkul = matkulList[position].namaMatkul
        jwlPengganti.jenisMatkul = matkulList[position].jenisMatkul.toString()
        jwlPenggantiBottomSheetView?.initKelasArrayAdapter(matkulList[position].listKelas)
    }

    fun onSelectRuanganItem(position: Int) {
        jwlPengganti.kdRuangan = ruanganList[position]
    }

    fun onSelectKelasItem(position: Int) {
        jwlPengganti.kdKelas = matkulList[matkulPos].listKelas[position]
    }

    fun onSelectTimeItem(time : String){
        jwlPengganti.jamMulai = "$time:00"
        remoteRepository.doFetchJwlPenggantiRoomList(jwlPengganti.tglPengganti, jwlPengganti.jamMulai,jwlPengganti.kdKelas,jwlPengganti.namaMatkul,jwlPengganti.jenisMatkul, this)
    }

    fun onSelectOriginTime(tgl : String){
        jwlPengganti.tglKuliah = tgl
    }
    fun doFetchSessionList(tgl: String) {
        jwlPengganti.tglPengganti = tgl
        remoteRepository.doFetchSessionList(tgl, jwlPengganti.kdKelas, jwlPengganti.namaMatkul,jwlPengganti.jenisMatkul,this)
    }

    override fun onScheduleListResult(data: ScheduleResponse<JadwalDsn>) {
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

    override fun onMatkulListResult(data: MatkulListResponse) {
        matkulList = data.listMatkul
        val listOfMatkul = data.listMatkul.map { item ->
            if(item.jenisMatkul) "${item.namaMatkul} (T)" else "${item.namaMatkul} (P)"}
        jwlPenggantiBottomSheetView?.onMatkulListReady(listOfMatkul)
    }

    override fun onSessionListAvailableResult(data: TimeAvailableResponse) {
        if (data.jamKosong.isEmpty()) {
            jwlPenggantiBottomSheetView?.handleNoSessionFound()
        } else {
            session = data.jamKosong
            jwlPenggantiBottomSheetView?.onSessionDataReady(session)
        }
    }

    override fun onRoomListAvailableResult(data: RoomAvailableResponse) {
        ruanganList = data.ruanganKosong
        jwlPenggantiBottomSheetView?.onRoomDataReady(ruanganList)
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }

    override fun onJwlPenggantiCreated(response : BaseResponse) {
        if(response.status == "404") view.showSnackBar("Tanggal perkuliahan tidak valid") else view.showSnackBar("Jadwal pengganti berhasil dibuat")
    }

}