package com.edimitre.personalmanager.data.repository

import android.util.Log
import com.edimitre.personalmanager.data.dao.ReminderDao
import com.edimitre.personalmanager.data.model.Reminder

class ReminderRepository(private val reminderDao: ReminderDao) {

    var allReminders = reminderDao.getAllRemindersLiveData()

    suspend fun save(reminder: Reminder) {

        reminderDao.saveReminder(reminder)
        Log.e("PersonalManager", "reminder me emer : ${reminder.description} U ruajt")

    }

    suspend fun delete(reminder: Reminder) {

        reminderDao.deleteReminder(reminder)

        Log.e("PersonalManager", "reminder me emer : ${reminder.description} U fshi")

    }

    suspend fun getFirstReminder(): Reminder? {

        return reminderDao.getFirstReminder()

    }

}