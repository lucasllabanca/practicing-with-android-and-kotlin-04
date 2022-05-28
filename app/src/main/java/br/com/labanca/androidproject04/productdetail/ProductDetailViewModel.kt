package br.com.labanca.androidproject04.productdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.labanca.androidproject04.persistence.Product
import br.com.labanca.androidproject04.persistence.ProductRepository

private const val TAG = "ProductDetailViewModel"

class ProductDetailViewModel(private val code: String?): ViewModel() {

    private lateinit var product: MutableLiveData<Product> //its created in the repo

    init {
        if (code != null) {
            getProduct(code)
        } else {
            product = MutableLiveData<Product>()
            product.value = Product()
        }
    }

    private fun getProduct(productCode: String) {
        product = ProductRepository.getProductByCode(productCode)
    }

    //when leaves the activity, saves the product. no save button needed
    override fun onCleared() {
        if (product.value != null && product.value!!.code != null && product.value!!.code!!.isNotBlank()) {
            ProductRepository.saveProduct(product.value!!)
        }
        super.onCleared()
    }

}