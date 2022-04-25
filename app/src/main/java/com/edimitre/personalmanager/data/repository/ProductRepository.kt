package com.edimitre.personalmanager.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.edimitre.personalmanager.data.dao.ProductDao
import com.edimitre.personalmanager.data.model.Product

class ProductRepository(private val productDao: ProductDao) {

    var allProducts = productDao.getAll()


    suspend fun save(product: Product) {

        productDao.save(product)
        Log.e("PersonalManager", "produkti me emer : ${product.name} U ruajt")

    }

    suspend fun delete(product: Product) {

        productDao.delete(product)
        Log.e("PersonalManager", "produkti me emer : ${product.name} U fshi")

    }

    fun getByName(name: String): LiveData<List<Product>> {

        return productDao.getByName(name)
    }

}