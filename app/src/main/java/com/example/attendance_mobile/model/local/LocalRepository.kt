package com.example.attendance_mobile.model.local

import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.KehadiranPerSesi
import com.example.attendance_mobile.utils.Converters
import com.example.attendance_mobile.utils.TimeUtils
import io.realm.Realm


class LocalRepository {
    fun getPairSession(): Pair<KehadiranPerSesi?, KehadiranPerSesi?> {
            val realm = Realm.getDefaultInstance()
            val list = realm.where(KehadiranPerSesi::class.java)
                .equalTo("status", false)
                .sort("sesi")
                .limit(2)
                .findAll()?.toList()!!
        return when (list.size) {
            1 -> {
                Pair(list[0], null)
            }
            0 -> {
                Pair(null, null)
            }
            else -> {
                Pair(list[0], list[1])
            }
        }
    }

    fun getListOfUnsentAttendance(): List<KehadiranPerSesi>? {
        val realm = Realm.getDefaultInstance()
        return realm.where(KehadiranPerSesi::class.java)
            ?.equalTo("status", true)
            ?.sort("sesi")
            ?.findAll()?.toList()
    }

//    fun releaseItemFromQueue() {
//        val realm = Realm.getDefaultInstance()
//        val data = realm.where(LocalAttendance::class.java)
//            ?.equalTo("hasBeenExecuted", 0.toInt())
//            ?.sort("sesi")
//            ?.findFirst()
//        realm.executeTransaction {
//            data?.deleteFromRealm()
//        }
//    }

//    fun getItemFromQueue(callback : (LocalAttendance?) -> Unit) {
//        val realm = Realm.getDefaultInstance()
//        val data = realm.where(LocalAttendance::class.java)
//            ?.equalTo("hasBeenExecuted", 1.toInt())
//            ?.sort("sesi")
//            ?.findFirst()
//        realm.executeTransaction {
//            callback(data)
//        }
//    }

    fun releaseAllExecutedItem() {
        val realm = Realm.getDefaultInstance()
        val data = realm.where(KehadiranPerSesi::class.java)
            .equalTo("status", true)
            .findAll()
        realm.executeTransaction {
            data?.deleteAllFromRealm()
        }
    }

    //for testing purpose
    fun getAllExecutedItem() : List<KehadiranPerSesi>?{
        val realm = Realm.getDefaultInstance()
        return realm.where(KehadiranPerSesi::class.java)
            ?.equalTo("status", true)
            ?.sort("sesi")
            ?.findAll()?.toList()
    }

//    fun insert(attendance: LocalAttendance) {
//        realm.executeTransaction { it.insert(attendance) }
//    }

    fun updateItemFromQueue() {
        val realm = Realm.getDefaultInstance()
        val newAttendance = realm.where(KehadiranPerSesi::class.java)
            .equalTo("status", false)
            .sort("sesi")
            .findFirst()
        realm.executeTransaction {
            newAttendance?.status = true
        }
    }

    fun saveBulkSchedule(data: JadwalMhs) {
        val realm = Realm.getDefaultInstance()
        val kodeMatkul = data.kodeMatkul
        val list = mutableListOf<KehadiranPerSesi>()
        val currentDate = TimeUtils.getCurrentDate()
        for (subItem in data.listSesi) {
            val scheduleDate = TimeUtils.convertStringToDate(subItem.jamSelesai)
            if (currentDate < scheduleDate)  {
                subItem.tglKuliah = TimeUtils.getDateInString(TimeUtils.getCurrentDate(),"dd-MM-yyyy")
                subItem.id = Converters.convertAttributesToSingleString(subItem.sesi.toString(),subItem.kodeMatkul,subItem.tglKuliah)
                subItem.kodeMatkul = kodeMatkul
                list.add(subItem)
            }
        }
        realm.executeTransaction {
            it.insert(list)
        }
    }

    fun deleteAllRows() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(KehadiranPerSesi::class.java)
        }
    }

}