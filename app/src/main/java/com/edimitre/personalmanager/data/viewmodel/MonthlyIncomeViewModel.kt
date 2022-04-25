package com.edimitre.personalmanager.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.edimitre.personalmanager.data.model.MonthlyIncome
import com.edimitre.personalmanager.data.model.MonthlyIncomeType
import com.edimitre.personalmanager.data.repository.MonthlyIncomeRepository
import kotlinx.coroutines.runBlocking


class MonthlyIncomeViewModel(private val monthlyIncomeRepository: MonthlyIncomeRepository) :
    ViewModel() {

    // MONTHLY INCOME TYPES
    var monthlyIncomeTypesList: List<MonthlyIncomeType>? = null


    var monthlyIncomeLiveData = monthlyIncomeRepository.thisMonthlyIncome

    var value: Int? = null


    init {
        runBlocking {

            monthlyIncomeTypesList = monthlyIncomeRepository.getAllMonthlyIncomeTypes()

            if (monthlyIncomeTypesList!!.isEmpty()) {
                insertMonthlyIncomeTypes()
            }
        }

    }


    private fun insertMonthlyIncomeTypes() {

        // HARDCODED
        val monthlyIncomeTypeList: MutableList<MonthlyIncomeType> = ArrayList()
        val m1 = MonthlyIncomeType(0, "RROGA_MUJORE")
        val m2 = MonthlyIncomeType(0, "TE_ARDHURA_NGA_BIZNESI")
        val m3 = MonthlyIncomeType(0, "TE_TJERA")

        monthlyIncomeTypeList.add(m1)
        monthlyIncomeTypeList.add(m2)
        monthlyIncomeTypeList.add(m3)

        for (monthlyIncomeType in monthlyIncomeTypeList) {
            saveMonthlyIncomeType(monthlyIncomeType)

        }
    }

    private fun saveMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType) {
        runBlocking {

            monthlyIncomeRepository.saveMonthlyIncomeType(monthlyIncomeType)
        }
    }


    fun saveMonthlyIncome(monthlyIncome: MonthlyIncome) {

        runBlocking {
            monthlyIncomeRepository.save(monthlyIncome)
        }

    }

    fun deleteMonthlyIncome(monthlyIncome: MonthlyIncome) {

        runBlocking {
            monthlyIncomeRepository.delete(monthlyIncome)
        }

    }

    fun getMonthlyIncomesByName(name: String): LiveData<List<MonthlyIncome?>?>? {
        return monthlyIncomeRepository.getByName(name)
    }

    fun getValueOfIncomesByName(name: String): Int? {

        runBlocking {
            value = monthlyIncomeRepository.getValueOfIncomesByName(name)

        }

        return value
    }

    fun getValueOfIncomesByMonth(year: Int, month: Int): Int? {
        runBlocking {
            value = monthlyIncomeRepository.getValueOfIncomesByMonth(year, month)
        }

        return value
    }


}