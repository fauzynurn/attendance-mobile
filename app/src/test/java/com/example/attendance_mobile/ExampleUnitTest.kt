package com.example.attendance_mobile

import com.example.attendance_mobile.utils.TimeUtils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val startDate = TimeUtils.convertStringToDate("07:00:00")!!
        val endDate = TimeUtils.convertStringToDate("07:05:00")!!
        assertEquals(9,TimeUtils.getDiff(startDate,endDate))
    }
}
