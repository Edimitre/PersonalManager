package com.edimitre.personalmanager.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.personalmanager.data.model.Description
import com.edimitre.personalmanager.data.repository.DescriptionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DescriptionViewModel(private val descriptionRepository: DescriptionRepository) : ViewModel() {


    var allDescriptions = descriptionRepository.allDescriptions


    var descList: List<Description>? = null


    fun getAllDescriptionList(): List<Description>? {

        val thread = Thread(Runnable {
            descList = descriptionRepository.getDescriptionList()
        })
        thread.start()
        thread.join()
        return descList
    }

    fun saveDescription(description: Description): Job = viewModelScope.launch {
        descriptionRepository.save(description)
    }

    fun deleteDescription(description: Description): Job = viewModelScope.launch {
        descriptionRepository.delete(description)
    }

    fun getDescriptionByName(name: String): LiveData<List<Description>> {
        return descriptionRepository.getByName(name)
    }

}