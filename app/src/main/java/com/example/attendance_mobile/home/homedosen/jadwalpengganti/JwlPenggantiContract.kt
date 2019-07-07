package com.example.attendance_mobile.home.homedosen.jadwalpengganti

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.response.*

interface JwlPenggantiContract {
    interface ViewContract : BaseView<JwlPenggantiPresenter>{
        fun refreshList()
        fun handleNoJwlPenggantiFound()
        fun showSnackBar(message: String)
        fun hideLoading()
        fun reloadList()
        fun startHomeDsn()
    }
    
    interface ItemViewContract{
        fun setKelas(kelas : String)
        fun setStartTime(startTime : String)
        fun setEndTime(endTime : String)
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
        fun setTglDibuat(tgl : String)
        fun setTglPengganti(tgl : String)
        fun showLoading()
        fun hideLoading()
        fun setDeleteBtnClickListener(item : JadwalDsn, clickListener: (JadwalDsn) -> Unit)
    }

    interface InteractorContract {
        fun onJwlPenggantiListResult(data : JwlPenggantiListResponse)
        fun onFail(error : String?)
        fun onMatkulListResult(data : MatkulListResponse)
        fun onSessionListAvailableResult(data : TimeAvailableResponse)
        fun onRoomListAvailableResult(data : RoomAvailableResponse)
        fun onJwlPenggantiCreated(response : BaseResponse)
    }

    interface JwlPenggantiBtmSheetViewContract {
        fun initKelasArrayAdapter(data : List<String>)
        fun onRoomDataReady(data : List<String>)
        fun onMatkulListReady(data : List<String>)
        fun onSessionDataReady(data : List<String>)
        fun handleNoSessionFound()
        fun setUpConfirmButton()
    }
}