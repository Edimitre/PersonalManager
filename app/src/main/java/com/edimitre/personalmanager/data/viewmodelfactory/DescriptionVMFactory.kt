package com.edimitre.personalmanager.data.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.repository.DescriptionRepository
import com.edimitre.personalmanager.data.viewmodel.DescriptionViewModel

class DescriptionVMFactory(private val descriptionRepository: DescriptionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(DescriptionViewModel::class.java)) {
            return DescriptionViewModel(descriptionRepository) as T

        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}