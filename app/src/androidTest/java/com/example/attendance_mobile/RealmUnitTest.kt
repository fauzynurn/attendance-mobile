package com.example.attendance_mobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.KehadiranPerSesi
import com.example.attendance_mobile.data.LocalAttendance
import com.example.attendance_mobile.data.Ruangan
import com.example.attendance_mobile.model.local.LocalRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmUnitTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var localRepository: LocalRepository
    private lateinit var sd1: JadwalMhs
    @Before
    fun init() {
        localRepository = LocalRepository()
        sd1 = JadwalMhs(
            "Pengolahan Citra Digital",
            true,
            "16TKO6022",
            listOf("Nurjannah Syakrani"),
            "07:00:00",
            "07:13:00",
            Ruangan(
                "D219",
                "C2:00:E2:00:00:6A"
            ),
            "07:01:00",
            arrayListOf(
                KehadiranPerSesi(1, "07:58:00", "08:20:00", false),
                KehadiranPerSesi(2, "08:20:00", "08:40:00", false),
                KehadiranPerSesi(3, "09:00:00", "09:50:00", false)
            )
        )
        localRepository.saveBulkSchedule("161511049", sd1)
    }

    @Test
    fun get_all_local_saved_attendance_should_give_correct_data() {
        var list: List<LocalAttendance>? = listOf()

        //case 1 ( di jam 08:00 )
        list = localRepository.getListOfUnsentAttendance()
        assertEquals(3, list?.size)
        assertEquals(1, list?.first()?.sesi)
        assertEquals(2, list?.get(1)?.sesi)
        assertEquals(3, list?.get(2)?.sesi)
        localRepository.deleteAllRows()

        //case 2 ( di jam 08:25 )
        localRepository.saveBulkSchedule("161511049", sd1)
        list = localRepository.getListOfUnsentAttendance()
        assertEquals(2, list?.size)
        assertEquals(2, list!!.first().sesi)
        assertEquals(3, list[1].sesi)
    }

    @Test
    fun update_local_saved_attendance_should_update_correct_value() {
        localRepository.updateItemFromQueue("14-06-2019 15:22")
        val list = localRepository.getAllExecutedItem()
        assertEquals(1, list!!.first().sesi)
        assertEquals(1,list.first().hasBeenExecuted)
        assertEquals("14-06-2019 15:22", list.first().attendanceResponse?.tglKuliah)
    }

    @Test
    fun release_local_saved_attendance_should_delete_specific_data(){
        localRepository.updateItemFromQueue("14-06-2019 15:22")
        localRepository.updateItemFromQueue("14-06-2019 16:22")

        localRepository.releaseAllExecutedItem()
        val list = localRepository.getAllExecutedItem()
        assertEquals(0,list?.size)
    }

    @Test
    fun get_pair_attendance_should_work_as_expected() {

        //case 1
        var pair = localRepository.getPairSession()
        assertEquals(1, pair.first?.sesi)
        assertEquals(2, pair.second?.sesi)


        //case 2
        localRepository.updateItemFromQueue("14-06-2019 15:22")
        localRepository.releaseAllExecutedItem()
        pair = localRepository.getPairSession()
        assertEquals(2, pair.first?.sesi)
        assertEquals(3, pair.second?.sesi)

        //case 3
        localRepository.updateItemFromQueue("14-06-2019 16:22")
        localRepository.releaseAllExecutedItem()

        pair = localRepository.getPairSession()
        assertEquals(3, pair.first?.sesi)
        assertEquals(null, pair.second)

        //case 4
        localRepository.updateItemFromQueue("14-06-2019 17:22")
        localRepository.releaseAllExecutedItem()

        pair = localRepository.getPairSession()
        assertEquals(null, pair.first)
        assertEquals(null, pair.second)
    }


}