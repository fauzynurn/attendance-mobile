package com.example.attendance_mobile.model.local

import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.LocalAttendance
import com.example.attendance_mobile.data.response.AttendanceResponse
import com.example.attendance_mobile.utils.TimeUtils
import io.realm.Realm


class LocalRepository {
    fun getPairSession(): Pair<LocalAttendance?, LocalAttendance?> {
            val realm = Realm.getDefaultInstance()
            val list = realm.where(LocalAttendance::class.java)
                .equalTo("hasBeenExecuted", 0.toInt())
                .limit(2)
                .sort("sesi")
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

    fun getListOfUnsentAttendance(): List<LocalAttendance>? {
        val realm = Realm.getDefaultInstance()
        return realm.where(LocalAttendance::class.java)
            ?.equalTo("hasBeenExecuted", 0.toInt())
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
        val data = realm.where(LocalAttendance::class.java)
            .equalTo("hasBeenExecuted", 1.toInt())
            .findAll()
        realm.executeTransaction {
            data?.deleteAllFromRealm()
        }
    }

    //for testing purpose
    fun getAllExecutedItem() : List<LocalAttendance>?{
        val realm = Realm.getDefaultInstance()
        return realm.where(LocalAttendance::class.java)
            ?.equalTo("hasBeenExecuted", 1.toInt())
            ?.sort("sesi")
            ?.findAll()?.toList()
    }

//    fun insert(attendance: LocalAttendance) {
//        realm.executeTransaction { it.insert(attendance) }
//    }

    fun updateItemFromQueue(tgl : String) {
        val realm = Realm.getDefaultInstance()
        val newAttendance = realm.where(LocalAttendance::class.java)
            .equalTo("hasBeenExecuted", 0.toInt())
            .sort("sesi")
            .findFirst()
        realm.executeTransaction {
            newAttendance?.hasBeenExecuted = 1
            newAttendance?.attendanceResponse?.tglKuliah = tgl
        }
    }

    fun saveBulkSchedule(nim : String, data: JadwalMhs) {
        val realm = Realm.getDefaultInstance()
        val kodeMatkul = data.kodeMatkul
        val macAddress = data.ruangan.macAddress
        val list = mutableListOf<LocalAttendance>()
        val currentDate = TimeUtils.getCurrentDate()
        for (subItem in data.jamMatkul) {
            val scheduleDate = TimeUtils.convertStringToDate(subItem.jamSelesai)
            if (currentDate < scheduleDate)  {
                val (sesi, jamMulai, jamSelesai) = subItem
                val attendance = LocalAttendance(
                    sesi,
                    AttendanceResponse(
                        nim,
                        kodeMatkul,
                        TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy HH:mm")
                    ),
                    jamMulai,
                    jamSelesai,
                    0,
                    macAddress
                )
                list.add(attendance)
            }
        }
        realm.executeTransaction {
            it.insert(list)
        }
    }

    fun deleteAllRows() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.delete(LocalAttendance::class.java)
        }
    }

}