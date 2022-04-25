package com.edimitre.personalmanager.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.personalmanager.data.model.Product
import com.edimitre.personalmanager.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    var allProducts = productRepository.allProducts


    fun saveProduct(product: Product): Job = viewModelScope.launch {
        productRepository.save(product)
    }

    fun deleteProduct(product: Product): Job = viewModelScope.launch {
        productRepository.delete(product)
    }

    fun getProductByName(name: String): LiveData<List<Product>> {
        return productRepository.getByName(name)
    }


}