package com.example.attendance_mobile.home.homedosen.mhslist

import com.example.attendance_mobile.data.response.MhsListResponse

interface MhsListContract {
    interface ViewContract{
        fun onMhsListLoaded()
        fun handleEmptyMhsList()
        fun setSession(session : String)
        fun showSnackBar(message : String)
        fun setJmlhHadir(jml : Int)
        fun setJmlTdkHadir(jml : Int)
        fun refreshList()
    }
    interface InteractorContract{
        fun onMhsListResult(data : MhsListResponse)
        fun onFail(message : String)
    }
    interface ItemViewContract{
        fun setNama(nama : String)
        fun setHadir()
        fun setTidakHadir()
    }
}