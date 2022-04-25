package com.edimitre.personalmanager.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_income_table")
data class MonthlyIncome(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val value: Double,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,

    @Embedded
    val monthlyIncomeType: MonthlyIncomeType

)
