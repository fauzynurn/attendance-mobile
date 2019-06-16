package com.example.attendance_mobile.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun convertStringToDate(time: String): Date? {
        return if (time != "") {
            val arraySplit = time.split(":")
            val calendar: Calendar = Calendar.getInstance()
            calendar.apply {
                calendar.time = Date()
                set(Calendar.HOUR_OF_DAY, arraySplit[0].toInt())
                set(Calendar.MINUTE, arraySplit[1].toInt())
                set(Calendar.SECOND, 0)
            }
            calendar.time
        } else {
            null
        }
    }

    fun addMinutesToDate(minutes: Int, date: Date): Date {
        return Date(date.time + (minutes * 60000))
    }

    fun getDateInString(date: Date, format: String): String {
        val formatInstance = SimpleDateFormat(format, Locale.ENGLISH)
        return formatInstance.format(date)
    }

    fun getCurrentDateInInt(): Int {
        return Integer.parseInt(SimpleDateFormat("ddHHmmss", Locale.US).format(Date()))
    }

    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun getDiff(date1: Date, date2: Date): Long {
        return (date2.time - date1.time) / (60 * 1000)
    }
}
