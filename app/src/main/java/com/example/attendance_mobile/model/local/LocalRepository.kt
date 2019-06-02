package com.example.attendance_mobile.model.local

import com.example.attendance_mobile.data.Attendance
import com.example.attendance_mobile.data.Schedule
import com.example.attendance_mobile.utils.Converters
import com.example.attendance_mobile.utils.TimeUtils
import io.realm.Realm


class LocalRepository(val realm : Realm) {
    fun getPairSession(doInBackgroundThread : Boolean): Pair<Attendance?, Attendance?> {
        var list = mutableListOf<Attendance>()
        list = if(doInBackgroundThread) {
            val backgroundRealm = Realm.getDefaultInstance()
            backgroundRealm?.where(Attendance::class.java)
                ?.equalTo("hasBeenExecuted", 0.toInt())
                ?.limit(2)
                ?.sort("sesi")
                ?.findAll()?.toMutableList()!!
        }else{
            realm.where(Attendance::class.java)
                ?.equalTo("hasBeenExecuted", 0.toInt())
                ?.limit(2)
                ?.findAll()?.toMutableList()!!
        }
        return when (list.size) {
            1 -> {
                Pair(list[0], null)
            }
            0 -> {
                Pair(null, null)
            }
            else -> {
                Pair(list.get(0), list.get(1))
            }
        }
    }

    fun getListOfUnsentAttendance(): List<Attendance> {
        return realm.where(Attendance::class.java)
            ?.equalTo("hasBeenExecuted", 0.toInt())
            ?.sort("sesi")
            ?.findAll()?.toList()!!
    }

    fun releaseSchedule(doInBackgroundThread : Boolean) {
        val backgroundRealm = Realm.getDefaultInstance()
        val data = backgroundRealm.where(Attendance::class.java)
            ?.equalTo("hasBeenExecuted", 0.toInt())
            ?.sort("sesi")
            ?.findAll()
        if(!doInBackgroundThread) {
            realm.executeTransaction {
                data?.deleteFirstFromRealm()
            }
        }else{
            backgroundRealm.executeTransaction {
                data?.deleteFirstFromRealm()
            }
        }

    }

    fun insert(attendance: Attendance) {
        realm.executeTransaction { it.insert(attendance) }
    }

    //Asumsi hanya berjalan pada background thread
    fun updateLocalAttendance(attendance: Attendance) {
        val backgroundRealm = Realm.getDefaultInstance()
        val newAttendance = backgroundRealm.where(Attendance::class.java)
            ?.equalTo("hasBeenExecuted", 0.toInt())
            ?.sort("sesi")
            ?.equalTo("sesi", attendance.sesi)?.findFirst()
        backgroundRealm.executeTransaction {
            newAttendance?.hasBeenExecuted = 1
        }
    }

    fun saveBulkSchedule(data: Schedule) {
        val kodeMatkul = data.kodeMatkul
        val macAddress = data.macAddress
        val list = mutableListOf<Attendance>()
        for (subItem in data.jamMatkul) {
            val (sesi, jamMulai, jamSelesai) = subItem
            val attendance = Attendance(
                Converters.convertAttributesToSingleString(
                    sesi.toString(),
                    kodeMatkul,
                    TimeUtils.getCurrentTimeInString()
                )
                , sesi, kodeMatkul, TimeUtils.getCurrentTimeInString(), jamMulai, jamSelesai, 0, macAddress
            )
            list.add(attendance)
        }
        realm.executeTransaction {
            it.insert(list)
        }
    }
//
//    fun deleteAllRows(){
//        Single.fromCallable { roomInstance?.clearAllTables() }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
//    }

}