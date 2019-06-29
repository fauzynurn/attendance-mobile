package com.example.attendance_mobile.detailsummary

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.DetailAkumulasiKehadiran

interface DetailSummaryContract {
    interface ViewContract : BaseView<DetailSummaryPresenter>{
        fun showSnackBar(message : String)
        fun refreshList()
        fun onSummaryListLoaded()
    }
    interface InteractorContract{
        fun onSummaryListResult(data : ArrayList<DetailAkumulasiKehadiran>)
        fun onFail(error : String?)
    }
    interface ItemViewContract{
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
        fun setJmlhHadir(jmlhHadir : String)
        fun setJmlhTdkHadir(jmlhTdkHadir : String)
    }
}