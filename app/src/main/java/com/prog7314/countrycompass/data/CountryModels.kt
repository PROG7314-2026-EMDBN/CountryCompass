package com.prog7314.countrycompass.data

import com.google.gson.annotations.SerializedName

data class CountryEnvelope(val data: CountryData = CountryData())
data class CountryData(val objects: List<Country> = emptyList())
data class CountryName(val common: String = "", val official: String = "")
data class CountryCodes(@SerializedName("alpha_3") val alpha3: String = "")
data class CountryCapital(val name: String = "")
data class Country(
    val names: CountryName = CountryName(),
    val codes: CountryCodes = CountryCodes(),
    val capitals: List<CountryCapital> = emptyList(),
    val region: String = "",
    val population: Long = 0,
    val borders: List<String> = emptyList()
)
