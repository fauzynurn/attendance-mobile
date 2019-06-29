package com.example.attendance_mobile

import com.example.attendance_mobile.utils.TimeUtils
import org.json.JSONObject
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
        val endDate = TimeUtils.convertStringToDate("07:50:00")!!
        assertEquals(50,TimeUtils.getDiff(startDate,endDate))
    }

    @Test
    fun json_test_should_get_propriate_string(){
       val jsonObject = JSONObject()
        jsonObject.apply {
            put("name","Henry Lau")
            put("status","PRESENT")
        }
        assertEquals("PRESENT",jsonObject.getString("status"))
    }
}
