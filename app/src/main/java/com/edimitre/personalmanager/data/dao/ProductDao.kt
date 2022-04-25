package com.edimitre.personalmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.personalmanager.data.model.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM product_table WHERE id =:id")
    fun getById(id: Int): LiveData<Product>

    @Query("SELECT * FROM product_table WHERE name LIKE '%' || :name || '%'")
    fun getByName(name: String): LiveData<List<Product>>

    @Query("SELECT * FROM product_table")
    suspend fun getAllList(): List<Product>

    @Query("SELECT * FROM product_table")
    fun getAll(): LiveData<List<Product>>


}