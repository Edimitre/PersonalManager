package com.edimitre.personalmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var name: String,
    var price: Double,
    var quantity: Int

)
