package com.edimitre.personalmanager.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.edimitre.personalmanager.data.dao.ReminderDao
import com.edimitre.personalmanager.data.repository.ReminderRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import kotlinx.coroutines.runBlocking

class MyAlarmReceiver : BroadcastReceiver() {

    private lateinit var reminderDao: ReminderDao

    private lateinit var reminderRepository: ReminderRepository

    private lateinit var systemService: SystemService

    override fun onReceive(context: Context, intent: Intent) {

        loadObjects(context)

        runBlocking {

            setFirstReminderFalse()

            activateNextReminder()
        }
    }

    private fun loadObjects(context: Context) {

        reminderDao = MyRoomDatabase.getInstance(context).reminderDao

        reminderRepository = ReminderRepository(reminderDao)

        systemService = SystemService(context)

    }

    private suspend fun setFirstReminderFalse() {

        val reminder = reminderRepository.getFirstReminder()
        if (reminder != null) {

            systemService.notify("PersonalManager", reminder.description)

            reminder.isActive = false

            reminderRepository.save(reminder)
        }

    }

    private suspend fun activateNextReminder() {

        val reminder = reminderRepository.getFirstReminder()

        if (reminder != null) {
            systemService.cancelAllAlarms()

            systemService.setAlarm(reminder.alarmTimeInMillis)
        }

    }


}