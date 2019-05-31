package com.example.attendance_mobile.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object{
        fun convertStringToDate(time : String) : Date?{
            if(time != "") {
                val arraySplit = time.split(":")
                val calendar: Calendar = Calendar.getInstance()
                calendar.apply {
                    calendar.time = Date()
                    set(Calendar.HOUR_OF_DAY, arraySplit[0].toInt())
                    set(Calendar.MINUTE, arraySplit[1].toInt())
                    set(Calendar.SECOND, 0)
                }
                return calendar.time
            }else{
                return null
            }
        }

        fun addMinutesToDate(minutes : Int, date: Date) : Date{
            return Date(date.time + (minutes * 60000))
        }

        fun formatCurentDateToString() : String {
            val date : Date = Calendar.getInstance().time
            val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
            return format.format(date)
        }

        fun getCurrentDate() : Int{
            return Integer.parseInt(SimpleDateFormat("ddHHmmss", Locale.US).format(Date()))
        }

        fun getCurrentTimeInString() : String{
            return Calendar.getInstance().time.toString()
        }

        fun getDiff(date1 : Date,date2 : Date) : Long{
            val diff = date2.time - date1.time
            return diff/(60 * 1000)
        }
    }
}