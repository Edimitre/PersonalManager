package com.edimitre.personalmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.personalmanager.data.model.Reminder


@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(reminder: Reminder?)

    @Query("SELECT * FROM reminder_table")
    suspend fun getAllReminders(): List<Reminder>?

    @Query("SELECT * FROM reminder_table")
    fun getAllRemindersLiveData(): LiveData<List<Reminder?>?>?

    @Delete
    suspend fun deleteReminder(reminder: Reminder?)

    @Query("SELECT * FROM reminder_table where isActive like 1 ORDER BY alarmTimeInMillis ASC LIMIT 1")
    suspend fun getFirstReminder(): Reminder?
}