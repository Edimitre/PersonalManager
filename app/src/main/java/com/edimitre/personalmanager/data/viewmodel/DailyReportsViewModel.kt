package com.edimitre.personalmanager.data.viewmodel

import androidx.lifecycle.ViewModel
import com.edimitre.personalmanager.data.model.DailyReport
import com.edimitre.personalmanager.data.repository.DailyReportRepository
import kotlinx.coroutines.runBlocking

class DailyReportsViewModel(private val dailyReportRepository: DailyReportRepository) :
    ViewModel() {


    var allDailyReports = dailyReportRepository.allDailyReports

    fun saveDailyReport(dailyReport: DailyReport) {

        runBlocking {
            dailyReportRepository.save(dailyReport)
        }
    }


    fun deleteDailyReport(dailyReport: DailyReport) {
        runBlocking {
            dailyReportRepository.delete(dailyReport)
        }
    }

}