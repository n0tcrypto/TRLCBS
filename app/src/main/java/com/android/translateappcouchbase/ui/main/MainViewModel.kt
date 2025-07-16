package com.android.translateappcouchbase.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.android.translateappcouchbase.data.DataRepository
import com.android.translateappcouchbase.data.model.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    private val _allWords = MutableLiveData<List<WordEntity>>()
    private val _searchQuery = MutableLiveData<String>("") // reactive input

    val words: LiveData<List<WordEntity>> = MediatorLiveData<List<WordEntity>>().apply {
        addSource(_allWords) { list -> value = filterList(list, _searchQuery.value) }
        addSource(_searchQuery) { query -> value = filterList(_allWords.value, query) }
    }

    fun loadWordsFromJson(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.loadWordsFromAsset(context)
            _allWords.postValue(data)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.postValue(query)
    }

    private fun filterList(source: List<WordEntity>?, query: String?): List<WordEntity> {
        if (source.isNullOrEmpty()) return emptyList()
        if (query.isNullOrBlank()) return source
        return source.filter {
            it.word.contains(query!!, ignoreCase = true)
        }
    }
}

