package com.example.attendance_mobile.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.attendance_mobile.data.Attendance
import com.example.attendance_mobile.utils.Converters

@Database(entities = [Attendance::class], version = 1)
@TypeConverters(Converters::class)
abstract class AttendanceDatabase : RoomDatabase() {

    abstract fun attendanceDao(): AttendanceDao
    companion object {
        private var INSTANCE: AttendanceDatabase? = null
        fun getInstance(context: Context): AttendanceDatabase? {
            if (INSTANCE == null) {
                    synchronized(AttendanceDatabase::class) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            AttendanceDatabase::class.java
                            ,"attendancedata.db"
                        ).build()
                    }
            }
            return INSTANCE
        }
    }
}