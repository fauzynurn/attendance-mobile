package com.example.attendance_mobile.home.homedosen.mhslist

import com.example.attendance_mobile.data.KehadiranMhs

interface MhsListContract {
    interface ViewContract{
        fun onMhsListLoaded()
        fun handleEmptyMhsList()
        fun showSnackBar(message : String)
        fun setJmlhHadir(jml : Int)
        fun setJmlTdkHadir(jml : Int)
        fun refreshList()
    }
    interface InteractorContract{
        fun onMhsListResult(data : List<KehadiranMhs>)
        fun onFail(message : String)
    }
    interface ItemViewContract{
        fun setNama(nama : String)
        fun setHadir()
        fun setTidakHadir()
    }
}