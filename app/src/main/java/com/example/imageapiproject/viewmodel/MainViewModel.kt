package com.example.imageapiproject.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.imageapiproject.model.ImageResult
import com.example.imageapiproject.networking.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _images = MutableLiveData<List<ImageResult>>()
    val images: LiveData<List<ImageResult>> get() = _images

    private var currentQuery: String? = null
    private var currentPage = 1

    private val sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    init {
        currentQuery = sharedPreferences.getString("last_query", null)
        currentQuery?.let { searchImages(it) }
    }

    fun searchImages(query: String) {
        currentQuery = query
        currentPage = 1
        viewModelScope.launch {
            val response = RetrofitInstance.api.searchImages(query, currentPage)
            _images.postValue(response.hits)
            sharedPreferences.edit().putString("last_query", query).apply()
        }
    }

    fun loadMoreImages() {
        currentQuery?.let {
            viewModelScope.launch {
                val response = RetrofitInstance.api.searchImages(it, ++currentPage)
                _images.postValue(_images.value?.plus(response.hits))
            }
        }
    }
}
