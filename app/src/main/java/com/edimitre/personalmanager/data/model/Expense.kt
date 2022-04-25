package com.edimitre.personalmanager.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class Expense(

    @PrimaryKey(autoGenerate = true)
    var expense_id: Int,


    var year: Int,
    var month: Int,
    var date: Int,
    var hour: Int,
    var minute: Int,
    var productList: List<Product>,
    var spentValue: Double,
    @Embedded
    var description: Description


)
