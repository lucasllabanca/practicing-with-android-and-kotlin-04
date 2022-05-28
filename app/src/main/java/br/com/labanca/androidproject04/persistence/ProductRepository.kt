package br.com.labanca.androidproject04.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "ProductRepository"

private const val COLLECTION = "products"
private const val FIELD_USER_ID = "userId"
private const val FIELD_NAME = "name"
private const val FIELD_DESCRIPTION = "description"
private const val FIELD_CODE = "code"
private const val FIELD_PRICE = "price"

//object is singleton, unique instance in the app
object ProductRepository {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun saveProduct(product: Product): String { //return the firestore document id as String
        val document = if (product.id != null) {
            firebaseFirestore.collection(COLLECTION).document(product.id!!)
        } else {
            product.userId = firebaseAuth.currentUser!!.uid
            firebaseFirestore.collection(COLLECTION).document() //creates new collecion with new document id
        }
        document.set(product)
        return document.id
    }

    fun deleteProduct(productId: String) {
        val document = firebaseFirestore.collection(COLLECTION).document(productId)
        document.delete()
    }

    fun getProducts(): MutableLiveData<List<Product>> { //async call
        val liveProducts = MutableLiveData<List<Product>>()

        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_USER_ID, firebaseAuth.currentUser!!.uid)
            .orderBy(FIELD_NAME, Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException -> //query result is returned with querySnapshot
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener //return only from the scope of the method addSna...
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val products = ArrayList<Product>()
                    querySnapshot.forEach {
                        val product = it.toObject<Product>()
                        product.id = it.id //this is needed because its not a field, its de id of the document
                        products.add(product)
                    }
                    liveProducts.postValue(products)
                } else {
                    Log.d(TAG, "No product has been found")
                }
            }

        return liveProducts //this is returned right away, then listens to new products being posted with liveProducts.postValue(products)
    }

    fun getProductByCode(code: String): MutableLiveData<Product> {
        val liveProduct: MutableLiveData<Product> = MutableLiveData()

        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_CODE, code) //filters the product by code
            .whereEqualTo(FIELD_USER_ID, firebaseAuth.currentUser!!.uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val products = ArrayList<Product>()
                    querySnapshot.forEach {
                        val product = it.toObject<Product>()
                        product.id = it.id
                        products.add(product)
                    }
                    liveProduct.postValue(products[0])
                } else {
                    Log.d(TAG, "No product has been found")
                }
            }

        return liveProduct
    }
}