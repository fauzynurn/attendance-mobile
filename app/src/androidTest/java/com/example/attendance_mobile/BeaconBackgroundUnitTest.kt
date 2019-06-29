package com.example.attendance_mobile

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ServiceTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.attendance_mobile.model.service.BeaconBackgroundService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BeaconBackgroundUnitTest {

    @get:Rule
    val serviceRule = ServiceTestRule()

    @Before
    fun init(){

    }

    @Test
    fun service_test(){
        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), BeaconBackgroundService::class.java)
        val service = BeaconBackgroundService()
        intent.apply {
            putExtra("macAddress","XXXX")
            action = "INIT_RANGING"
        }
        ApplicationProvider.getApplicationContext<Context>().startService(intent)
            assertEquals("some changes",service.someString)
    }
}