package com.edimitre.personalmanager.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.edimitre.personalmanager.data.dao.DescriptionDao
import com.edimitre.personalmanager.data.model.Description

class DescriptionRepository(private val descriptionDao: DescriptionDao) {

    var allDescriptions = descriptionDao.getAll()

    suspend fun save(description: Description) {

        descriptionDao.save(description)
        Log.e("PersonalManager", "pershkrimi me emer : ${description.name} U ruajt")

    }

    suspend fun delete(description: Description) {

        descriptionDao.delete(description)
        Log.e("PersonalManager", "pershkrimi me emer : ${description.name} U fshi")

    }

    fun getByName(name: String): LiveData<List<Description>> {

        return descriptionDao.getByName(name)
    }

    suspend fun getDescriptionList(): List<Description> {

        return descriptionDao.getAllList()
    }

}