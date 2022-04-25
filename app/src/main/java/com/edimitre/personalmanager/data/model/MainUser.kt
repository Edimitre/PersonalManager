package com.edimitre.personalmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "main_user_table")
data class MainUser(

    @PrimaryKey
    var id: Int,

    var name: String,

    var totalAmountOfMoney: Double

) {

    fun spentMoney(value: Double) {

        totalAmountOfMoney -= value
    }

    fun addMoney(value: Double) {

        totalAmountOfMoney += value

    }

}
