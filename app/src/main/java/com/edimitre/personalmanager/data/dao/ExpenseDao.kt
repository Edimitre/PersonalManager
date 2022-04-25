package com.edimitre.personalmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.personalmanager.data.model.Expense

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    // LIVEDATA
    @Query("SELECT * FROM expense_table WHERE year = :year and month = :month and date = :date")
    fun getAllExpensesByYearMonthAndDateLiveData(
        year: Int,
        month: Int,
        date: Int
    ): LiveData<List<Expense?>?>?

    @Query("SELECT * FROM expense_table WHERE year = :year and month = :month")
    fun getAllExpensesByYearAndMonthLiveData(year: Int, month: Int): LiveData<List<Expense?>?>?

    @Query("select * from expense_table where name LIKE '%' || :name || '%'")
    fun getAllExpenseListLiveDataByName(name: String): LiveData<List<Expense?>?>?


    // NON LIVE DATA
    @Query("SELECT SUM(spentValue) FROM expense_table WHERE name LIKE '%' || :name || '%'")
    suspend fun getValueOfSelectedExpenseListByName(name: String?): Int?

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year and month = :month")
    suspend fun getValueOfExpenseListByMonth(year: Int, month: Int): Int?

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year and month = :month and date = :date")
    suspend fun getValueOfExpenseListByDate(year: Int, month: Int, date: Int): Int?

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year and month = :month and date = :date")
    fun getValueOfExpenseListByDateNumber(year: Int, month: Int, date: Int): Int?

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year")
    suspend fun getValueOfExpenseListByYear(year: Int): Int?

    @Query("SELECT COUNT(*) FROM expense_table where year = :year and month = :month")
    suspend fun getSizeOfExpenseListByMonth(year: Int, month: Int): Int?

    @Query("SELECT COUNT(*) FROM expense_table where year = :year")
    suspend fun getSizeOfExpenseListByYear(year: Int): Int?

    @Query("SELECT * FROM expense_table where year =:year and month =:month ORDER BY spentValue DESC LIMIT 1")
    suspend fun getBiggestByYearAndMonth(year: Int, month: Int): Expense?

}