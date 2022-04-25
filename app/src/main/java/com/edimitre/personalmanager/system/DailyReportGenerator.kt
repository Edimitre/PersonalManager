package com.edimitre.personalmanager.system

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.model.DailyReport
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.utils.TimeUtils

class DailyReportGenerator : Service() {

    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val thread = Thread {


            startServiceNotification()

            Log.e("PersonalManager", "service started")


            generateReport()

        }
        thread.start()
        thread.join()

        stopSelf()
        return START_NOT_STICKY
    }


    private fun generateReport() {


        Log.e("PersonalManager", "starting report")

        val expenseDao = MyRoomDatabase.getInstance(applicationContext).expenseDao

        val monthlyIncomeDao =
            MyRoomDatabase.getInstance(applicationContext).monthlyIncomeDao

        val dailyReportDao = MyRoomDatabase.getInstance(applicationContext).dailyReportDao

        val nrOfDays = TimeUtils().getNrOfDaysOfActualMonth()


        Log.e("PersonalManager", "1")


        val monthlyIncomeValue = monthlyIncomeDao.getValueOfIncomesByMonthNumber(
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth()
        )

        Log.e("PersonalManager", "2")


        if (monthlyIncomeValue!=null && monthlyIncomeValue > 1) {


            val dailyLimit = monthlyIncomeValue / nrOfDays

            val spentValue = expenseDao.getValueOfExpenseListByDateNumber(
                TimeUtils().getCurrentYear(),
                TimeUtils().getCurrentMonth(),
                TimeUtils().getCurrentDay()
            )

            var isOk = true

            if (spentValue != null && spentValue > dailyLimit) {
                isOk = false
            }

            val dailyReport = DailyReport(
                0, TimeUtils().getCurrentYear(),
                TimeUtils().getCurrentMonth(), TimeUtils().getCurrentDay(),
                TimeUtils().getCurrentHour(), TimeUtils().getCurrentMinute(),
                dailyLimit, spentValue, isOk
            )

            dailyReportDao.saveReportFast(dailyReport)
            Log.e("PersonalManager", "daily report generated : ${dailyReport.isOk}")
        }
    }


    private fun startServiceNotification() {
        val serviceNotification: Notification = NotificationCompat.Builder(
            applicationContext, "PersonalManagerNotificationChannel"
        )
            .setContentTitle("PersonalManager")
            .setContentText("Po gjeneroj raportin ditor")
            .setSmallIcon(R.drawable.report)
            .build()
        startForeground(1, serviceNotification)
    }

}