package com.example.attendance_mobile.detailpersession

import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.KehadiranPerSesi
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils

class DetailPerSessionPresenter(
    private val view: DetailPerSessionContract.ViewContract,
    private val remoteRepository: RemoteRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : DetailPerSessionContract.InteractorContract {
    private var list: List<KehadiranPerSesi> = emptyList()

    fun onActivityStart(schedule : JadwalMhs) {
        view.apply {
            setNamaMatkul(schedule.namaMatkul)
            if(schedule.dosen.size > 1) {
                setDosen(schedule.dosen[0])
                setMoreClickListener(schedule.dosen)
            }else{
                hideMoreButton()
            }
            setJamMatkul(schedule.jamMulai.substring(0, schedule.jamMulai.length - 3),schedule.jamSelesai.substring(0, schedule.jamSelesai.length - 3))
        }
        onAttendanceSessionListSuccess(schedule.jamMatkul)
    }

    fun size(): Int {
        return list.size
    }

    fun onBindItem(holder: DetailPerSessionAdapter.SessionViewHolder, position: Int) {
        val item = list.get(position)
        val startTime = TimeUtils.convertStringToDate(item.jamMulai)
        holder.apply {
            setSessionNum(item.sesi)
            if(TimeUtils.getCurrentDate() > startTime){
                if(item.status){
                    setMarker(Constants.HADIR)
                }else{
                    setMarker(Constants.TIDAK_HADIR)
                }
            }else{
                setMarker(Constants.TANPA_KETERANGAN)
            }
            setSessionTime(item.jamMulai.substring(0, item.jamMulai.length - 3),item.jamSelesai.substring(0, item.jamSelesai.length - 3))
        }
    }

    override fun onAttendanceSessionListSuccess(list : List<KehadiranPerSesi>){
        this.list = list
        view.apply {
            refreshList()
        }
    }

    fun onBackPressed(){
        view.startHomeMhs()
    }

//    override fun onAttendanceSessionListFail(message : String){
//
//    }
}