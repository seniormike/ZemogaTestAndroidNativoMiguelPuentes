package com.example.mobiletestzemogamiguelpuentes.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mobiletestzemogamiguelpuentes.R
import com.example.mobiletestzemogamiguelpuentes.model.db.DaoDatabase
import com.example.mobiletestzemogamiguelpuentes.services.ApiServices
import com.example.mobiletestzemogamiguelpuentes.tools.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppPosts : Application() {

    companion object {
        /**
         * Api Retrofit services reference initialized.
         */
        val apiServices: ApiServices = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiServices::class.java)

        /**
         * Database access session initialized.
         */
        @Volatile
        private var instance: DaoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context, DaoDatabase::class.java, context.resources.getString(R.string.db_name)
        ).build()
    }
}