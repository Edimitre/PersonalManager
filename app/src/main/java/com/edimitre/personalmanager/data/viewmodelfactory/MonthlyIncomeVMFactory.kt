package com.edimitre.personalmanager.data.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.repository.MonthlyIncomeRepository
import com.edimitre.personalmanager.data.viewmodel.MonthlyIncomeViewModel

class MonthlyIncomeVMFactory(private val monthlyIncomeRepository: MonthlyIncomeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(MonthlyIncomeViewModel::class.java)) {
            return MonthlyIncomeViewModel(monthlyIncomeRepository) as T

        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}