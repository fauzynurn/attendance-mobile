package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.ScheduleMhs

interface HomeMhsContract {
    interface ViewContract : BaseView<HomeMhsPresenter> {
        fun startBeaconActivity()
        fun onSummaryDataLoaded(data : HashMap<String,Int>)
        fun onScheduleListLoaded(data : ArrayList<ScheduleMhs>)
        fun showSnackBar(message: String)
        fun showDialog(message: String)
    }

    interface InteractorContract{
        fun onScheduleListResult(list : ArrayList<ScheduleMhs>)
        fun onSummaryResult(summaryData : HashMap<String,Int>)
        fun onFail(error : String?)
    }
}