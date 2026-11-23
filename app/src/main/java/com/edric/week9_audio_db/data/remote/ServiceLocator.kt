package com.edric.week9_audio_db.data.remote

import com.edric.week9_audio_db.data.repository.MusicRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ServiceLocator {
    private const val API_KEY = "123"
    private const val BASE_URL = "https://www.theaudiodb.com/api/v1/json/$API_KEY/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    private val api: AudioDbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(AudioDbApiService::class.java)
    }

    val musicRepository: MusicRepository by lazy { MusicRepository(api) }
}
