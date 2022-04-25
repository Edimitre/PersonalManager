package com.edimitre.personalmanager.data.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.repository.DailyReportRepository
import com.edimitre.personalmanager.data.viewmodel.DailyReportsViewModel

class DailyReportsVMFactory(private val dailyReportRepository: DailyReportRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(DailyReportsViewModel::class.java)) {
            return DailyReportsViewModel(dailyReportRepository) as T

        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}