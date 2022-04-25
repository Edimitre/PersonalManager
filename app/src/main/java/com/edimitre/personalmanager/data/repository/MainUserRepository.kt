package com.edimitre.personalmanager.data.repository

import android.util.Log
import com.edimitre.personalmanager.data.dao.MainUserDao
import com.edimitre.personalmanager.data.model.MainUser

class MainUserRepository(private val userDao: MainUserDao) {


    suspend fun save(mainUser: MainUser) {

        userDao.save(mainUser)
        Log.e("PersonalManager", "user me emer : ${mainUser.name} U ruajt")

    }

    suspend fun delete(mainUser: MainUser) {

        userDao.delete(mainUser)
        Log.e("PersonalManager", "user me emer : ${mainUser.name} U fshi")

    }

    suspend fun getMainUser(): MainUser? {

        return userDao.getMainUser()

    }

}