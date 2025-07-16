package com.android.translateappcouchbase.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.translateappcouchbase.R
import com.android.translateappcouchbase.data.ViewModelFactory
import com.android.translateappcouchbase.data.adapter.WordAdapter
import com.android.translateappcouchbase.databinding.ActivityMainBinding
import com.android.translateappcouchbase.ui.bookmark.BookmarkActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val wordAdapter by lazy { WordAdapter() }

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.words.value.isNullOrEmpty()) {
            viewModel.loadWordsFromJson(this)
        }


        setupRecyclerView()


        viewModel.words.observe(this) { wordList ->
            wordAdapter.submitList(wordList)
        }



        binding.myToolbar.btnBookmark.setOnClickListener {
            val intent = Intent(this, BookmarkActivity::class.java)
            startActivity(intent)
        }

        binding.searchEditText.visibility = View.GONE

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



    }

    private fun setupRecyclerView() {
        binding.rvWord.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = wordAdapter
        }
    }
}