package com.edimitre.personalmanager.data.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.repository.ProductRepository
import com.edimitre.personalmanager.data.viewmodel.ProductViewModel

class ProductVMFactory(private val productRepository: ProductRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productRepository) as T

        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}