package com.example.attendance_mobile

import android.app.Application
import com.example.attendance_mobile.data.Schedule
import com.example.attendance_mobile.data.Sesi
import com.example.attendance_mobile.model.local.AttendanceDatabase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private var x: AttendanceDatabase? = null
    private var list : List<Schedule> = emptyList()
    @Before
    fun init(){
        x = AttendanceDatabase.getInstance(Application.)
        x?.clearAllTables()
        list = listOf(
            Schedule("Pengolahan Citra Digital",
        true,
        "16TKO6022",
        "07:00:00",
        "08:40:00",
        "D219",
        "C2:00:E2:00:00:6A",
        "07:05:00",
        arrayListOf(Sesi(1,"07:00:00","07:50:00"), Sesi(2,"07:50:00","08:40:00"))),
        Schedule("Pengantar Akuntansi",
            true,
            "16JTK6012",
            "08:40:00",
            "11:30:00",
            "D219",
            "C2:00:E2:00:00:6A",
            "08:45:00",
            arrayListOf(Sesi(1,"08:40:00","09:30:00"), Sesi(2,"09:30:00","10:20:00"), Sesi(3,"10:40:00","11:30:00")))
        )
        x?.attendanceDao()?.batchInsertAttendance(list)
    }
    @Test
    fun addition_isCorrect() {
        assertEquals(2,list.size)
    }
}
