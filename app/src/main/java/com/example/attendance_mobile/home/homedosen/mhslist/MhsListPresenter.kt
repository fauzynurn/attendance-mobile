package com.example.attendance_mobile.home.homedosen.mhslist

import com.example.attendance_mobile.data.KehadiranMhs
import com.example.attendance_mobile.home.homedosen.viewholder.MhsListViewHolder
import com.example.attendance_mobile.model.remote.RemoteRepository

class MhsListPresenter(val view : MhsListContract.ViewContract, val remoteRepository: RemoteRepository) : MhsListContract.InteractorContract {
    private var mhsList: List<KehadiranMhs> = emptyList()

    fun doFetchMhsList(){
        remoteRepository.doFetchMhsList(this)
    }

    fun size() : Int{
        return mhsList.size
    }

    fun onBindMhsItem(holder : MhsListViewHolder, position : Int){
        val mhs = mhsList[position]
        holder.setNama(mhs.namaMhs)
        if(mhs.statusHadir) holder.setHadir() else holder.setTidakHadir()
    }

    override fun onMhsListResult(data : List<KehadiranMhs>) {
        mhsList = data
        val jmlhHadir = data.count { item -> item.statusHadir }
        view.setJmlhHadir(jmlhHadir)
        view.setJmlTdkHadir(data.size - jmlhHadir)
        if(data.isEmpty()){
            view.handleEmptyMhsList()
        }else {
            view.refreshList()
            view.onMhsListLoaded()
        }
    }

    override fun onFail(message : String) {
        view.showSnackBar(message)
    }


}