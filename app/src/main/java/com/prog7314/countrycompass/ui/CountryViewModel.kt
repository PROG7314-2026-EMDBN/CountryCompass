package com.prog7314.countrycompass.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prog7314.countrycompass.data.Country
import com.prog7314.countrycompass.data.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SearchState {
    data object Idle : SearchState
    data object Loading : SearchState
    data class Success(val countries: List<Country>) : SearchState
    data class Error(val message: String) : SearchState
}

sealed interface DetailState {
    data object Loading : DetailState
    data class Success(val country: Country) : DetailState
    data class Error(val message: String) : DetailState
}

class CountryViewModel : ViewModel() {
    private val repository = CountryRepository()
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()
    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()

    fun search(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            _searchState.value = try {
                SearchState.Success(repository.search(query.trim()))
            } catch (e: Exception) {
                SearchState.Error(e.message ?: "Unable to search")
            }
        }
    }

    fun loadCountry(identifier: String) {
        viewModelScope.launch {
            _detailState.value = DetailState.Loading
            _detailState.value = try {
                DetailState.Success(repository.loadCountry(identifier))
            } catch (e: Exception) {
                DetailState.Error(e.message ?: "Unable to load country")
            }
        }
    }
}
