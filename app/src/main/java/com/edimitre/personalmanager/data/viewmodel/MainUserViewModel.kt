package com.edimitre.personalmanager.data.viewmodel

import androidx.lifecycle.ViewModel
import com.edimitre.personalmanager.data.model.Expense
import com.edimitre.personalmanager.data.model.MainUser
import com.edimitre.personalmanager.data.repository.MainUserRepository
import kotlinx.coroutines.runBlocking

class MainUserViewModel(private val mainUserRepository: MainUserRepository) : ViewModel() {


    fun saveMainUser(mainUser: MainUser) {


        runBlocking {


            mainUserRepository.save(mainUser)

        }
    }

    fun getMainUser(): MainUser? {


        return runBlocking { mainUserRepository.getMainUser() }

    }

    fun spentMoney(value: Double) {

        runBlocking {

            val mainUser = getMainUser()

            if (mainUser != null) {

                mainUser.spentMoney(value)
                mainUserRepository.save(mainUser)
            }

        }


    }

    fun addMoney(value: Double) {

        runBlocking {

            val mainUser = getMainUser()

            if (mainUser != null) {

                mainUser.addMoney(value)
                mainUserRepository.save(mainUser)
            }

        }


    }

    fun removeExpense(expense: Expense) {
        runBlocking {

            val mainUser = getMainUser()

            if (mainUser != null) {

                mainUser.addMoney(expense.spentValue)
                mainUserRepository.save(mainUser)
            }

        }
    }

}