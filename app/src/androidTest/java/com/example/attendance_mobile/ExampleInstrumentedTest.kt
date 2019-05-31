package com.example.attendance_mobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.attendance_mobile.data.Attendance
import com.example.attendance_mobile.data.Schedule
import com.example.attendance_mobile.data.Sesi
import com.example.attendance_mobile.model.local.AttendanceDatabase
import com.example.attendance_mobile.model.local.LocalRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var sc1 : Schedule
    private lateinit var at1 : Attendance
    private lateinit var at2 : Attendance
    private var newList = mutableListOf<Attendance>()
    private var x: AttendanceDatabase? = null
    private lateinit var localRepository: LocalRepository

    @Before
    fun init(){
        x = AttendanceDatabase.getInstance(InstrumentationRegistry.getTargetContext())
        x?.clearAllTables()
        //localRepository = LocalRepository(InstrumentationRegistry.getTargetContext())
//        at = Attendance(
//            1,
//            "a",
//            "b",
//            "c",
//            0,
//            "d",
//            "e"
//        )

        sc1 = Schedule("Pengolahan Citra Digital",
                true,
                "16TKO6022",
                "07:00:00",
                "08:40:00",
                "D219",
                "C2:00:E2:00:00:6A",
                "07:05:00",
                arrayListOf(Sesi(1,"07:00:00","07:50:00"), Sesi(2,"07:50:00","08:40:00")))
        at1 = Attendance(1,sc1.kodeMatkul,sc1.jamMulai,sc1.jamSelesai,0,"xxxx","ppp")
        at2 = Attendance(2,sc1.kodeMatkul,sc1.jamMulai,sc1.jamSelesai,0,"xxxx","ppp")
        newList.add(at1)
        newList.add(at2)
    }

    @Test
    fun addition_isCorrect() {
        x?.attendanceDao()?.batchInsertAttendance(newList)
        x?.attendanceDao()?.getListOfUnsentAttendance()?.test()?.assertValue(newList)
        localRepository.releaseSchedule("16TKO6022")
        newList.remove(at1)
        x?.attendanceDao()?.getListOfUnsentAttendance()?.test()?.assertValue(newList)
    }

}
