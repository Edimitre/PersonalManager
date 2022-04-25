package com.edimitre.personalmanager.data.repository

import android.util.Log
import com.edimitre.personalmanager.data.dao.DailyReportDao
import com.edimitre.personalmanager.data.model.DailyReport

class DailyReportRepository(private val dailyReportDao: DailyReportDao) {

    var allDailyReports = dailyReportDao.getAllDailyReportsLiveData()

    suspend fun save(dailyReport: DailyReport) {

        dailyReportDao.saveReport(dailyReport)
        Log.e("PersonalManager", "raporti me perundim : ${dailyReport.isOk} U ruajt")

    }

    suspend fun delete(dailyReport: DailyReport) {

        dailyReportDao.deleteDailyReport(dailyReport)
        Log.e("PersonalManager", "raporti me perundim : ${dailyReport.isOk} U fshi")

    }


}