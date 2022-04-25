package com.edimitre.personalmanager.data.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.repository.MainUserRepository
import com.edimitre.personalmanager.data.viewmodel.MainUserViewModel

class MainUserVMFactory(private val mainUserRepository: MainUserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(MainUserViewModel::class.java)) {
            return MainUserViewModel(mainUserRepository) as T

        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}