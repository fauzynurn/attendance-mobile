package com.example.attendance_mobile.home.homedosen.jadwalpengganti

import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.Matkul
import com.example.attendance_mobile.data.request.JwlPenggantiRequest
import com.example.attendance_mobile.data.response.*
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils

class JwlPenggantiPresenter(
    private val view: JwlPenggantiContract.ViewContract,
    private val remoteRepository: RemoteRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : JwlPenggantiContract.InteractorContract {
    private var jwlPenggantiBottomSheetView: JwlPenggantiContract.JwlPenggantiBtmSheetViewContract? = null
    private var jwlPenggantiList: MutableList<JadwalDsn> = mutableListOf()
    private var matkulList: List<Matkul> = emptyList()
    private var ruanganList: List<String> = emptyList()
    private lateinit var session: List<String>
    private lateinit var jwlPengganti: JwlPenggantiRequest
    private var matkulPos: Int = -1

    fun doRequestJadwalPenggantiList() {
        val kddosen = sharedPreferenceHelper.getSharedPreferenceString("kddsn","")
        remoteRepository.doFetchJwlPenggantiList(kddosen!!, TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy"), TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"HH:mm") + ":00",this)
    }

    fun jwlListSize(): Int {
        return jwlPenggantiList.size
    }

    fun onBindJwlListItem(position: Int, holder: JwlPenggantiViewHolder) {
        val schedule: JadwalDsn = jwlPenggantiList[position]
        holder.apply {
            setStartTime(schedule.jamMulai.substring(0, schedule.jamMulai.length - 3))
            setEndTime(schedule.jamSelesai.substring(0, schedule.jamSelesai.length - 3))
            setNamaMatkul(schedule.namaMatkul)
            setKelas(schedule.kelas)
            setTglDibuat(schedule.tglJwlPengganti!!.tglKuliah)
            setTglPengganti(schedule.tglJwlPengganti.tglPengganti)
            if (schedule.jenisMatkul) setJenisMatkul(Constants.TEORI) else setJenisMatkul(Constants.PRAKTEK)
        }

        holder.setDeleteBtnClickListener(schedule) {
            holder.showLoading()
            remoteRepository.doRequestDeleteJwlPengganti(
                schedule.kelas,
                schedule.jenisMatkul.toString(),
                schedule.namaMatkul,
                schedule.tglJwlPengganti!!.tglKuliah,
                { response ->
                holder.hideLoading()
                view.showSnackBar(response.message)
                jwlPenggantiList.remove(schedule)
                if(jwlPenggantiList.isEmpty()){
                    view.handleNoJwlPenggantiFound()
                }
                view.refreshList()
            }, { response ->
                holder.hideLoading()
                view.showSnackBar(response)
            })
        }

    }

    fun initAndAttachJwlPenggantiBottomSheet(v: JwlPenggantiContract.JwlPenggantiBtmSheetViewContract) {
        jwlPenggantiBottomSheetView = v
        jwlPengganti = JwlPenggantiRequest("", "", "", "", "", "", "")
    }

    fun onConfirmJwlPengganti() {
        remoteRepository.doRequestJwlPengganti(jwlPengganti, this)
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

    fun onSelectTimeItem(time: String) {
        jwlPengganti.jamMulai = "$time:00"
        remoteRepository.doFetchJwlPenggantiRoomList(
            jwlPengganti.tglPengganti,
            jwlPengganti.tglKuliah,
            jwlPengganti.jamMulai,
            jwlPengganti.kdKelas,
            jwlPengganti.namaMatkul,
            jwlPengganti.jenisMatkul,
            this
        )
    }

    fun onSelectOriginTime(tgl: String) {
        jwlPengganti.tglKuliah = tgl
    }

    fun doFetchSessionList(tgl: String) {
        jwlPengganti.tglPengganti = tgl
        remoteRepository.doFetchSessionList(
            jwlPengganti.tglPengganti,
            jwlPengganti.tglKuliah,
            jwlPengganti.kdKelas,
            jwlPengganti.namaMatkul,
            jwlPengganti.jenisMatkul,
            this
        )
    }

    override fun onMatkulListResult(data: MatkulListResponse) {
        matkulList = data.listMatkul
        val listOfMatkul = data.listMatkul.map { item ->
            if (item.jenisMatkul) "${item.namaMatkul} (T)" else "${item.namaMatkul} (P)"
        }
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

    override fun onJwlPenggantiCreated(response: BaseResponse) {
        if (response.status == "404") view.showSnackBar("Tanggal perkuliahan tidak valid") else view.showSnackBar("Jadwal pengganti berhasil dibuat")
        view.reloadList()
        doRequestJadwalPenggantiList()
    }

    override fun onJwlPenggantiListResult(data: JwlPenggantiListResponse) {
        view.hideLoading()
        if (data.listJadwal.isEmpty()) {
            view.handleNoJwlPenggantiFound()
        } else {
            jwlPenggantiList = data.listJadwal
            view.refreshList()
        }
    }

    override fun onFail(error: String?) {
        view.hideLoading()
        view.showSnackBar("Error occured: $error")
    }

    fun onBackPressed(){
        view.startHomeDsn()
    }
}
