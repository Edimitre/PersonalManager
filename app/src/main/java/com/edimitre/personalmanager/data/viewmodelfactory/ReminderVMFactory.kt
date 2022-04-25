package com.edimitre.personalmanager.data.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.repository.ReminderRepository
import com.edimitre.personalmanager.data.viewmodel.ReminderViewModel

@Suppress("UNCHECKED_CAST")
class ReminderVMFactory(private val reminderRepository: ReminderRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            return ReminderViewModel(reminderRepository) as T

        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}