package com.edimitre.personalmanager.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class DailyReportsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val startService = Intent(context, DailyReportGenerator::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startService(startService)
            reScheduleService(context)
        }

    }

    private fun reScheduleService(context: Context) {
        val mySystemService = SystemService(context)
        mySystemService.scheduleDailyReportAlarm()
    }


}