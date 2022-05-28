package br.com.labanca.androidproject04.persistence

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties //this allows the firestore document to have more props than mapped here
data class Product(
    @Exclude var id: String? = null, //firestore document id, not field of the document
    var userId: String? = null, //USER UID from Firebase
    var name: String? = null,
    var description: String? = null,
    var code: String? = null,
    var price: Double? = null
)
