package com.edimitre.personalmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.personalmanager.data.model.DailyReport

@Dao
interface DailyReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReport(dailyReport: DailyReport?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReportFast(dailyReport: DailyReport?)

    @Query("SELECT * FROM daily_report_table")
    suspend fun getAllDailyReports(): List<DailyReport?>?

    @Query("SELECT * FROM daily_report_table")
    fun getAllDailyReportsLiveData(): LiveData<List<DailyReport?>?>?

    @Delete
    suspend fun deleteDailyReport(dailyReport: DailyReport?)

}