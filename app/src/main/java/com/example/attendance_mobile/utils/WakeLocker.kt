package com.example.attendance_mobile.utils

import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.PowerManager


class WakeLocker {

    companion object {
        private var wakeLock: PowerManager.WakeLock? = null
        fun acquire(ctx: Context) {
            if (wakeLock != null) wakeLock!!.release()

            val pm = ctx.getSystemService(POWER_SERVICE) as PowerManager
            wakeLock = pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or
                        PowerManager.ACQUIRE_CAUSES_WAKEUP or
                        PowerManager.ON_AFTER_RELEASE, "beaconranging:tag"
            )
            wakeLock!!.acquire()
        }

        fun release() {
            if (wakeLock != null)
                wakeLock!!.release()
            wakeLock = null
        }
    }

}