package com.example.mobiletestzemogamiguelpuentes.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Base64
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import java.io.UnsupportedEncodingException
import java.util.*

class Utils {

    /**
     * Genera un único ID random
     */
    fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * Revisa si hay conexión a internet.
     */
    fun isNetworkAvailbale(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    /**
     * Método que convierte un string en base 64
     */
    fun convertToBase64(input: String): String {
        return try {
            val data = input.toByteArray(charset("UTF-8"))
            return Base64.encodeToString(data, Base64.NO_WRAP)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Function that prints log in console.
     * @param log String to be printed.
     */
    fun printLog(log: String) {
        Log.e("MSCAPP: ", log)
    }
}