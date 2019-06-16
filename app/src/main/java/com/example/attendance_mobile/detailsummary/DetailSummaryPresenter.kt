package com.example.attendance_mobile.detailsummary

import com.example.attendance_mobile.data.DetailSummary
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants

class DetailSummaryPresenter(val view : DetailSummaryContract.ViewContract,
                             val sharedPreferenceHelper: SharedPreferenceHelper,
                             val remoteRepository: RemoteRepository):DetailSummaryContract.InteractorContract{

    private var list: ArrayList<DetailSummary> = ArrayList()

    fun doFetchDetailSummaryList(){
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim", "")
        remoteRepository.doFetchDetailSummary(nim!!,this)
    }

    override fun onFail(error: String?) {
        view.showSnackBar("Error occured: $error")
    }

    fun onBindItem(position: Int, viewHolder: DetailSummaryAdapter.DetailSummaryViewHolder) {
        val item : DetailSummary = list[position]
        viewHolder.apply {
            setJmlhHadir(item.jumlahHadir)
            setJmlhTdkHadir(item.jumlahTdkHadir)
            setNamaMatkul(item.namaMatkul)
        }
        if (item.jenisMatkul) viewHolder.setJenisMatkul(Constants.TEORI) else viewHolder.setJenisMatkul(Constants.PRAKTEK)
    }

    fun size(): Int {
        return list.size
    }

    override fun onSummaryListResult(data: ArrayList<DetailSummary>) {
        list = data
        view.apply {
            refreshList()
            onSummaryListLoaded()
        }
    }

}