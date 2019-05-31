package com.example.attendance_mobile.model.local

import android.content.Context
import android.util.Log
import com.example.attendance_mobile.data.Attendance
import com.example.attendance_mobile.data.Schedule
import com.example.attendance_mobile.utils.TimeUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class LocalRepository(context: Context) {

    private var roomInstance: AttendanceDatabase? = null
    var mDisposable = CompositeDisposable()
    init {
        roomInstance = AttendanceDatabase.getInstance(context)
    }

    fun getPairSession(kodeMatkul: String, callback : (List<Attendance?>) -> (Unit)) {
        Single.fromCallable { roomInstance?.attendanceDao()?.getPairSession(kodeMatkul) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                attendanceList -> callback(attendanceList!!)
            }.dispose()
    }

    fun getListOfUnsentAttendance(callback : (List<Attendance?>) -> Unit) {
        mDisposable.add(roomInstance?.attendanceDao()?.getListOfUnsentAttendance()!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{x -> callback(x!!)})
    }

    fun releaseSchedule(kodeMatkul: String) {
        Single.fromCallable { roomInstance?.attendanceDao()?.deleteAttendance(kodeMatkul) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun insert(attendance: Attendance) {
        mDisposable.add( roomInstance?.attendanceDao()?.insertAttendance(attendance)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({Log.i("c","complete!")},{t -> Log.i("error","error: ${t.message}")}))
    }

    fun updateSchedule(attendance: Attendance) {
        roomInstance?.attendanceDao()?.updateAttendance(attendance)
    }

    fun saveBulkSchedule(data: Schedule) {
        val list : MutableList<Attendance> = mutableListOf()
        val kodeMatkul = data.kodeMatkul
        val macAddress = data.macAddress
            for (subItem in data.jamMatkul) {
                val (sesi, jamMulai, jamSelesai) = subItem
                val attendance = Attendance(sesi,
                    kodeMatkul, jamMulai, jamSelesai, 0, macAddress, TimeUtils.getCurrentTimeInString())
                list.add(attendance)
            }
        mDisposable.add(roomInstance?.attendanceDao()?.batchInsertAttendance(list)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({Log.i("c","bulk complete!")},{t -> Log.i("error","bulk error: ${t.message}")}))
    }

    fun deleteAllRows(){
        Single.fromCallable { roomInstance?.clearAllTables() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}