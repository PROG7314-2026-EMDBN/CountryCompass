package com.prog7314.countrycompass.data

import com.prog7314.countrycompass.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryRepository {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val key = BuildConfig.REST_COUNTRIES_API_KEY
            require(key.isNotBlank()) {
                "REST Countries API key missing. Add REST_COUNTRIES_API_KEY to local.properties."
            }
            chain.proceed(
                chain.request().newBuilder()
                    .header("Authorization", "Bearer $key")
                    .build()
            )
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("https://api.restcountries.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CountryApi::class.java)

    suspend fun search(query: String): List<Country> =
        api.searchByName(query).data.objects

    suspend fun loadCountry(identifier: String): Country =
        api.searchByName(identifier).data.objects.first()
}
