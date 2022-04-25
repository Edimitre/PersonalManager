package com.edimitre.personalmanager.data.roomdb

import androidx.room.TypeConverter
import com.edimitre.personalmanager.data.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyRoomDbConverters {

    @TypeConverter
    fun convertStringToList(value: String): List<Product> {
        val listType = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun convertListToString(list: List<Product>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}