package com.android.translateappcouchbase.ui.detail

import androidx.lifecycle.ViewModel
import com.android.translateappcouchbase.data.DataRepository
import com.android.translateappcouchbase.data.model.WordEntity

class DetailViewModel(private val repository: DataRepository) : ViewModel() {

    fun isBookmarked(word: String): Boolean {
        return repository.isWordBookmarked(word)
    }

    fun toggleBookmark(word: WordEntity): Boolean {
        return if (repository.isWordBookmarked(word.word)) {
            repository.deleteBookmark(word.word)
            false
        } else {
            repository.saveBookmark(word)
            true
        }
    }

    fun updateNote(word: WordEntity, note: String): WordEntity {
        repository.updateNote(word.word, note)
        return word.copy(note = note)
    }
}
