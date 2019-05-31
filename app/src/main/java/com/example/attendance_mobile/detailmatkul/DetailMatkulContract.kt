package com.example.attendance_mobile.detailmatkul

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.DetailSummary

interface DetailMatkulContract {
    interface ViewContract : BaseView<DetailMatkulPresenter>{
        fun showSnackBar(message : String)
        fun refreshList()
        fun onSummaryListLoaded()
    }
    interface InteractorContract{
        fun onSummaryListResult(data : ArrayList<DetailSummary>)
        fun onFail(error : String?)
    }
    interface ItemViewContract{
        fun setNamaMatkul(namaMatkul : String)
        fun setJenisMatkul(jenisMatkul : String)
        fun setJmlhHadir(jmlhHadir : Int)
        fun setJmlhTdkHadir(jmlhTdkHadir : Int)
    }
}