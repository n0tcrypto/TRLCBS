package com.android.translateappcouchbase.ui.bookmark

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.translateappcouchbase.data.ViewModelFactory
import com.android.translateappcouchbase.data.adapter.WordAdapter
import com.android.translateappcouchbase.databinding.ActivityBookmarkBinding

class BookmarkActivity:AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkBinding

    private val viewModel:BookmarkViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val wordAdapter by lazy { WordAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvInit()

        viewModel.bookmarks.observe(this) { bookmarkedWords ->
            wordAdapter.submitList(bookmarkedWords)
        }

        viewModel.loadBookmarks() // initial load

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    viewModel.searchBookmarks(query)
                } else {
                    viewModel.loadBookmarks()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


    }

    private fun rvInit() {
        binding.rvBookmark.apply {
            layoutManager = LinearLayoutManager(this@BookmarkActivity)
            adapter = wordAdapter
        }
    }
}