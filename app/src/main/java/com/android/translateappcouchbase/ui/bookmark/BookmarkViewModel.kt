package com.android.translateappcouchbase.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.translateappcouchbase.data.DataRepository
import com.android.translateappcouchbase.data.model.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: DataRepository): ViewModel() {

    private val _bookmarks = MutableLiveData<List<WordEntity>>()
    val bookmarks: LiveData<List<WordEntity>> = _bookmarks

    fun loadBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getAllWords()
            _bookmarks.postValue(data)
        }
    }

    fun searchBookmarks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val allBookmarks = repository.getAllWords()
            val filtered = allBookmarks.filter {
                it.word.contains(query, ignoreCase = true)
            }
            _bookmarks.postValue(filtered)
        }
    }
}
