package com.edimitre.personalmanager.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.edimitre.personalmanager.data.dao.ExpenseDao
import com.edimitre.personalmanager.data.model.Expense
import com.edimitre.personalmanager.data.utils.TimeUtils

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    var thisMonthExpenses = expenseDao.getAllExpensesByYearAndMonthLiveData(
        TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()
    )


    fun getAllExpensesByYearMonthAndDateLiveData(
        year: Int,
        month: Int,
        date: Int
    ): LiveData<List<Expense?>?>? {

        return expenseDao.getAllExpensesByYearMonthAndDateLiveData(year, month, date)

    }

    suspend fun save(expense: Expense) {
        expenseDao.save(expense)
        Log.e("PersonalManager", "shpenzimi me vlere : ${expense.spentValue} U ruajt")
    }

    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
        Log.e("PersonalManager", "shpenzimi me vlere : ${expense.spentValue} U fshi")
    }


    suspend fun getValueOfSelectedExpenseListByName(name: String?): Int? {

        return expenseDao.getValueOfSelectedExpenseListByName(name)
    }

    suspend fun getValueOfExpenseListByMonth(year: Int, month: Int): Int? {

        return expenseDao.getValueOfExpenseListByMonth(year, month)
    }

    suspend fun getValueOfExpenseListByDate(year: Int, month: Int, date: Int): Int? {

        return expenseDao.getValueOfExpenseListByDate(year, month, date)
    }

    suspend fun getValueOfExpenseListByYear(year: Int): Int? {

        return expenseDao.getValueOfExpenseListByYear(year)
    }

    suspend fun getSizeOfExpenseListByMonth(year: Int, month: Int): Int? {

        return expenseDao.getSizeOfExpenseListByMonth(year, month)
    }

    suspend fun getSizeOfExpenseListByYear(year: Int): Int? {

        return expenseDao.getSizeOfExpenseListByYear(year)
    }

    suspend fun getBiggestByYearAndMonth(year: Int, month: Int): Expense? {

        return expenseDao.getBiggestByYearAndMonth(year, month)
    }


}