package com.edimitre.personalmanager.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.edimitre.personalmanager.data.dao.MonthlyIncomeDao
import com.edimitre.personalmanager.data.model.MonthlyIncome
import com.edimitre.personalmanager.data.model.MonthlyIncomeType
import com.edimitre.personalmanager.data.utils.TimeUtils

class MonthlyIncomeRepository(private val monthlyIncomeDao: MonthlyIncomeDao) {

    var thisMonthlyIncome = monthlyIncomeDao.getAllMonthlyIncomesByYearAndMonthLiveData(
        TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()
    )


    suspend fun saveMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType) {

        monthlyIncomeDao.saveMonthlyIncomeType(monthlyIncomeType)
        Log.e("PersonalManager", "lloji i te ardhurave : ${monthlyIncomeType.name} U ruajt")

    }

    suspend fun deleteMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType) {

        monthlyIncomeDao.deleteMonthlyIncomeType(monthlyIncomeType)
        Log.e("PersonalManager", "lloji i te ardhurave : ${monthlyIncomeType.name} U fshi")

    }

    suspend fun getAllMonthlyIncomeTypes(): List<MonthlyIncomeType> {

        return monthlyIncomeDao.getAllMonthlyIncomeTypes()
    }


    suspend fun save(monthlyIncome: MonthlyIncome) {

        monthlyIncomeDao.saveMonthlyIncome(monthlyIncome)
        Log.e(
            "PersonalManager",
            "te ardhurat me emer : ${monthlyIncome.monthlyIncomeType.name} U ruajten"
        )

    }


    suspend fun delete(monthlyIncome: MonthlyIncome) {

        monthlyIncomeDao.deleteMonthlyIncome(monthlyIncome)
        Log.e(
            "PersonalManager",
            "te ardhurat me emer : ${monthlyIncome.monthlyIncomeType.name} U fshine"
        )

    }

    fun getByName(name: String): LiveData<List<MonthlyIncome?>?>? {

        return monthlyIncomeDao.getAllMonthlyIncomeLiveDataByName(name)
    }

    fun getAllMonthlyIncomesByYearAndMonthLiveData(
        year: Int,
        month: Int
    ): LiveData<List<MonthlyIncome?>?>? {

        return monthlyIncomeDao.getAllMonthlyIncomesByYearAndMonthLiveData(year, month)
    }

    suspend fun getValueOfIncomesByName(name: String): Int? {

        return monthlyIncomeDao.getValueOfIncomesByName(name)
    }

    suspend fun getValueOfIncomesByMonth(year: Int, month: Int): Int? {
        return monthlyIncomeDao.getValueOfIncomesByMonth(year, month)
    }

    suspend fun getNrOfIncomesByMonth(year: Int, month: Int): Int? {
        return monthlyIncomeDao.getNrOfIncomesByMonth(year, month)
    }

    suspend fun getValueOfIncomesByYear(year: Int): Int? {

        return monthlyIncomeDao.getValueOfIncomesByYear(year)

    }

    suspend fun getNrOfIncomesByYear(year: Int): Int? {
        return monthlyIncomeDao.getNrOfIncomesByYear(year)
    }

    suspend fun getValueOfIncomesByDate(year: Int, month: Int, date: Int): Int? {


        return monthlyIncomeDao.getValueOfIncomesByDate(year, month, date)
    }

}