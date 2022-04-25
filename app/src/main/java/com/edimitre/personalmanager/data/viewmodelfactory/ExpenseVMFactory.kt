package com.edimitre.personalmanager.data.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.repository.ExpenseRepository
import com.edimitre.personalmanager.data.viewmodel.ExpenseViewModel

class ExpenseVMFactory(private val expenseRepository: ExpenseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(expenseRepository) as T

        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}