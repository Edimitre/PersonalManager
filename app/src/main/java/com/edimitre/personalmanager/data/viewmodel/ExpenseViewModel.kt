package com.edimitre.personalmanager.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.edimitre.personalmanager.data.model.Expense
import com.edimitre.personalmanager.data.repository.ExpenseRepository
import kotlinx.coroutines.runBlocking

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    var thisMonthExpenses = expenseRepository.thisMonthExpenses


    fun saveExpense(expense: Expense) {
        runBlocking {
            expenseRepository.save(expense)
        }
    }

    fun deleteExpense(expense: Expense) {

        runBlocking {
            expenseRepository.delete(expense)
        }

    }


    fun getAllExpensesByYearMonthAndDateLiveData(
        year: Int,
        month: Int,
        date: Int
    ): LiveData<List<Expense?>?>? {

        return expenseRepository.getAllExpensesByYearMonthAndDateLiveData(year, month, date)

    }

    fun getValueOfSelectedExpenseListByName(name: String?): Int? {

        var value: Int?
        runBlocking {
            value = expenseRepository.getValueOfSelectedExpenseListByName(name)!!
        }
        return value
    }

    fun getValueOfExpenseListByMonth(year: Int, month: Int): Int? {

        var value: Int?
        runBlocking {
            value = expenseRepository.getValueOfExpenseListByMonth(year, month)
        }
        return value
    }

    fun getValueOfExpenseListByDate(year: Int, month: Int, date: Int): Int? {


        return runBlocking { expenseRepository.getValueOfExpenseListByDate(year, month, date) }
    }

    fun getValueOfExpenseListByYear(year: Int): Int? {


        return runBlocking { expenseRepository.getValueOfExpenseListByYear(year) }
    }

    fun getSizeOfExpenseListByMonth(year: Int, month: Int): Int? {


        return runBlocking { expenseRepository.getSizeOfExpenseListByMonth(year, month) }
    }

    fun getSizeOfExpenseListByYear(year: Int): Int? {


        return runBlocking { expenseRepository.getSizeOfExpenseListByYear(year) }
    }

    fun getBiggestByYearAndMonth(year: Int, month: Int): Expense? {

        return runBlocking { expenseRepository.getBiggestByYearAndMonth(year, month) }
    }

}