package com.example.attendance_mobile.home.homedosen.mhslist

import com.example.attendance_mobile.data.Kehadiran
import com.example.attendance_mobile.data.response.MhsListResponse
import com.example.attendance_mobile.home.homedosen.viewholder.MhsListViewHolder
import com.example.attendance_mobile.model.remote.RemoteRepository

class MhsListPresenter(val view : MhsListContract.ViewContract, val remoteRepository: RemoteRepository) : MhsListContract.InteractorContract {
    private var mhsList: List<Kehadiran> = emptyList()

    fun doFetchMhsList(){
        remoteRepository.doFetchMhsList("27-06-2019","16:00:00","09:30:00","3B","false","16TKO6043",this)
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