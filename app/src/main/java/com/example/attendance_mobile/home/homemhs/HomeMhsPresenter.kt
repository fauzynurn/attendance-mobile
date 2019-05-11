package com.example.attendance_mobile.home.homemhs

import com.example.attendance_mobile.data.ScheduleMhs
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.Repository

class HomeMhsPresenter(private val view : HomeMhsContract.ViewContract,
                       private val repository: Repository,
                       private val permissionManager: PermissionManager,
                       private val sharedPreferenceHelper: SharedPreferenceHelper) : HomeMhsContract.InteractorContract {
    fun doFetchSummaryData(){
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim","")
        repository.doFetchSummary(nim!!,this)
    }

    fun onScheduleClick(scheduleItem : ScheduleMhs){
        if(!permissionManager.isAutoTimeActive()){
            view.showDialog("Pastikan waktu dan zona waktu di set otomatis. Silahkan konfigurasikan di menu Setting anda.")
        }else{
            view.showDialog("Anda diizinkan untuk melakukan penyetoran presensi.")
        }
    }
    fun doFetchScheduleList(){
        val kelas = sharedPreferenceHelper.getSharedPreferenceString("kelas","")
        repository.doFetchScheduleList(kelas!!,this)
    }

    override fun onScheduleListResult(list: ArrayList<ScheduleMhs>) {
      view.onScheduleListLoaded(list)
    }

    override fun onSummaryResult(summaryData: HashMap<String, Int>) {
        view.onSummaryDataLoaded(summaryData)
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }
}