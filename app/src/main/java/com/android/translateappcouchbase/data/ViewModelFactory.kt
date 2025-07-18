package com.android.translateappcouchbase.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.translateappcouchbase.data.di.Injection
import com.android.translateappcouchbase.ui.bookmark.BookmarkViewModel
import com.android.translateappcouchbase.ui.detail.DetailViewModel
import com.android.translateappcouchbase.ui.main.MainViewModel

class ViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(repository) as T
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> BookmarkViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).also {
                    instance = it
                }
            }
    }
}
