package com.edimitre.personalmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.personalmanager.data.model.MonthlyIncome
import com.edimitre.personalmanager.data.model.MonthlyIncomeType


@Dao
interface MonthlyIncomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType)

    @Delete
    suspend fun deleteMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType)

    @Query("SELECT * FROM monthly_income_type_table")
    suspend fun getAllMonthlyIncomeTypes(): List<MonthlyIncomeType>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMonthlyIncome(monthlyIncome: MonthlyIncome?)

    @Query("SELECT * FROM monthly_income_table WHERE id = :id")
    suspend fun getMonthlyIncomeById(id: Int): MonthlyIncome?

    // shfaq shpenzimet sipas muajit dhe dates te caktuar
    @Query("SELECT * FROM monthly_income_table WHERE year = :year and month = :month")
    fun getAllMonthlyIncomesByYearAndMonthLiveData(
        year: Int,
        month: Int
    ): LiveData<List<MonthlyIncome?>?>?

    @Query("SELECT * FROM monthly_income_table WHERE year = :year and month = :month and day = :date")
    fun getAllMonthlyIncomesByYearMonthAndDateLiveData(
        year: Int,
        month: Int,
        date: Int
    ): LiveData<List<MonthlyIncome?>?>?

    @Query("select * from monthly_income_table where name LIKE '%' || :name || '%'")
    fun getAllMonthlyIncomeLiveDataByName(name: String?): LiveData<List<MonthlyIncome?>?>?

    @Query("SELECT SUM(value) FROM monthly_income_table WHERE name LIKE '%' || :name || '%'")
    suspend fun getValueOfIncomesByName(name: String?): Int?

    // mblidh shpenzimet e nje muaji te caktuar
    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year and month = :month")
    suspend fun getValueOfIncomesByMonth(year: Int, month: Int): Int?

    // mblidh shpenzimet e nje muaji te caktuar
    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year and month = :month")
    fun getValueOfIncomesByMonthNumber(year: Int, month: Int): Int?

    @Query("SELECT COUNT(*) FROM monthly_income_table where year = :year and month = :month")
    suspend fun getNrOfIncomesByMonth(year: Int, month: Int): Int?

    // mblidh shpenzimet e nje muaji te caktuar
    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year")
    suspend fun getValueOfIncomesByYear(year: Int): Int?

    @Query("SELECT COUNT(*) FROM monthly_income_table where year = :year")
    suspend fun getNrOfIncomesByYear(year: Int): Int?

    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year and month = :month and day = :date")
    suspend fun getValueOfIncomesByDate(year: Int, month: Int, date: Int): Int?

    @Query("SELECT * FROM monthly_income_table ORDER BY value DESC LIMIT 1")
    fun getBiggestMonthlyIncome(): LiveData<MonthlyIncome?>

    @Delete
    suspend fun deleteMonthlyIncome(monthlyIncome: MonthlyIncome?)

}