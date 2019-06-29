package com.example.attendance_mobile

import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JsonUnitTest {

    @Test
    fun json_test_should_get_propriate_string(){
        val jsonObject = JSONObject()
        jsonObject.apply {
            put("name","Henry Lau")
            put("status","PRESENT")
        }
        Assert.assertEquals("PRESET", jsonObject.getString("status"))
    }
}