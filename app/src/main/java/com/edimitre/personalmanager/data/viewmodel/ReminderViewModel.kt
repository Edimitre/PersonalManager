package com.edimitre.personalmanager.data.viewmodel

import androidx.lifecycle.ViewModel
import com.edimitre.personalmanager.data.model.Reminder
import com.edimitre.personalmanager.data.repository.ReminderRepository
import kotlinx.coroutines.runBlocking

class ReminderViewModel(private val reminderRepository: ReminderRepository) : ViewModel() {

    var allReminders = reminderRepository.allReminders


    fun saveReminder(reminder: Reminder) {

        runBlocking {
            reminderRepository.save(reminder)

        }

    }

    fun deleteReminder(reminder: Reminder) {
        runBlocking {
            reminderRepository.delete(reminder)

        }

    }

    fun getFirstReminder(): Reminder? {

        var reminder: Reminder?

        runBlocking {
            reminder = reminderRepository.getFirstReminder()

        }
        return reminder

    }


}