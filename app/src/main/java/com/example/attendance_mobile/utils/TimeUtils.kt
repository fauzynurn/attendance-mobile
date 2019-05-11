package com.example.attendance_mobile.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object{
        fun convertStringToDate(time : String) : Date{
            val arraySplit = time.split(":")
            val calendar : Calendar = Calendar.getInstance()
            calendar.apply {
                calendar.time = Date()
                set(Calendar.HOUR_OF_DAY,arraySplit[0].toInt())
                set(Calendar.MINUTE,arraySplit[1].toInt())
                set(Calendar.SECOND,0)
            }
            return calendar.time
        }

        fun addMinutesToDate(minutes : Int, date: Date) : Date{
            return Date(date.time + (minutes * 60000))
        }
    }
}