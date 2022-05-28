package br.com.labanca.androidproject04.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.labanca.androidproject04.persistence.Product
import br.com.labanca.androidproject04.persistence.ProductRepository

private const val TAG = "ProductListViewModel"

class ProductListViewModel: ViewModel() {

    private lateinit var _products: MutableLiveData<List<Product>>

    val products: LiveData<List<Product>>
        get() = _products

    init {
        getProducts()
    }

    fun refreshProducts() {
        getProducts()
    }

    private fun getProducts() {
        _products = ProductRepository.getProducts()
    }

    override fun onCleared() {
        super.onCleared()
    }

}