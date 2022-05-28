package br.com.labanca.androidproject04.product

import androidx.databinding.InverseMethod
import android.util.Log

object PriceConverter {

    @InverseMethod("stringToPrice") //the textbox nows this is the inverse when writing the text to the property
    @JvmStatic
    fun priceToString(newValue: Double): String {
        return "$ " + "%.2f".format(newValue)
    }

    @JvmStatic
    fun stringToPrice(newValue: String): Double {
        var price = 0.0
        try {
            price = newValue.removePrefix("$").trim().replace(",", ".").toDouble()
        } catch (e: NumberFormatException) {
            Log.e("PriceConverter", " Failed to convert price")
        }
        return price
    }
}