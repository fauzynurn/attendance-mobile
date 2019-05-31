package com.example.attendance_mobile.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.attendance_mobile.data.Attendance
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface AttendanceDao {
    @Query("SELECT * from attendance WHERE kode_matkul = :kodeMatkul AND sesi IN (select sesi where has_been_executed = 0 limit 2)")
    fun getPairSession(kodeMatkul : String): List<Attendance>

    @Query("SELECT * from attendance where has_been_executed = 0")
    fun getListOfUnsentAttendance() : Maybe<List<Attendance>?>

    @Query("delete from attendance where kode_matkul = :kodeMatkul AND sesi IN (select sesi from attendance where has_been_executed = 0 limit 1)")
    fun deleteAttendance(kodeMatkul: String)

    @Update
    fun updateAttendance(attendance: Attendance)

    @Insert
    fun insertAttendance(attendance: Attendance) : Completable

    @Insert
    fun batchInsertAttendance(list : MutableList<Attendance>) : Completable
}