package com.example.userdirectory.di

import android.content.Context
import androidx.room.Room
import com.example.userdirectory.data.UserRepository
import com.example.userdirectory.data.local.AppDatabase
import com.example.userdirectory.data.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceLocator {

    private var database: AppDatabase? = null
    private var repository: UserRepository? = null

    fun provideRepository(context: Context): UserRepository {

        val db = database ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "user-db"
        ).build().also { database = it }

        // Logging
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        val api = retrofit.create(ApiService::class.java)

        return repository ?: UserRepository(api, db.userDao()).also { repository = it }
    }
}
