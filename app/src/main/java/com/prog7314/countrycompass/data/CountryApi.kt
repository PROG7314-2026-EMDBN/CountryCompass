package com.prog7314.countrycompass.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val COUNTRY_FIELDS =
    "names.common,names.official,codes.alpha_3,capitals,region,population,borders"

interface CountryApi {
    @GET("countries/v5/names.common")
    suspend fun searchByName(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("response_fields") responseFields: String = COUNTRY_FIELDS
    ): CountryEnvelope

    @GET("countries/v5/codes.alpha_3/{code}")
    suspend fun getByCode(
        @Path("code") code: String,
        @Query("response_fields") responseFields: String = COUNTRY_FIELDS
    ): CountryEnvelope
}
