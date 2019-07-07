package com.example.attendance_mobile.home.homedosen.mhslist

import com.example.attendance_mobile.data.Kehadiran
import com.example.attendance_mobile.data.response.MhsListResponse
import com.example.attendance_mobile.home.homedosen.viewholder.MhsListViewHolder
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.TimeUtils

class MhsListPresenter(val view : MhsListContract.ViewContract, val remoteRepository: RemoteRepository, val kelas : String) : MhsListContract.InteractorContract {
    private var mhsList: List<Kehadiran> = emptyList()

    fun doFetchMhsList(){
        remoteRepository.doFetchMhsList(TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy"),TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"HH:mm") +":00",kelas,this)
    }

    fun size() : Int{
        return mhsList.size
    }

    fun onBindMhsItem(holder : MhsListViewHolder, position : Int){
        val mhs = mhsList[position]
        holder.setNama(mhs.namaMhs)
        if(mhs.status) holder.setHadir() else holder.setTidakHadir()
    }

    override fun onMhsListResult(data : MhsListResponse) {
        mhsList = data.mhsList
        val jmlhHadir = mhsList.count { item -> item.status }
        view.setJmlhHadir(jmlhHadir)
        view.setJmlTdkHadir(mhsList.size - jmlhHadir)
        if(mhsList.isEmpty()){
            view.handleEmptyMhsList()
        }else {
            view.refreshList()
            view.onMhsListLoaded()
        }
        view.setSession(data.jamKe)
    }

    override fun onFail(message : String) {
        view.showSnackBar(message)
    }


}