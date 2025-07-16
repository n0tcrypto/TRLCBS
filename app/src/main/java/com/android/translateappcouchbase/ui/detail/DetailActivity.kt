package com.android.translateappcouchbase.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.android.translateappcouchbase.R
import com.android.translateappcouchbase.data.ViewModelFactory
import com.android.translateappcouchbase.data.model.WordEntity
import com.android.translateappcouchbase.databinding.ActivityDetailBinding

class DetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var wordData: WordEntity

    private var isBookmarked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordData = intent.getParcelableExtra(EXTRA_EVENT) ?: return finish()

        binding.myToolbar.tvToolbarTitle.text = wordData.word
        binding.tvWordDefiniton.text = wordData.definition
        binding.descriptionEditText.setText(wordData.note ?: "")

        isBookmarked = viewModel.isBookmarked(wordData.word)
        setBookmarkState(isBookmarked)

        setupActions()

    }

    private fun setupActions() {
        binding.myToolbar.btnBackToolbar.setOnClickListener {
            finish()
        }

        binding.myToolbar.btnBookmark.setOnClickListener {
            isBookmarked = viewModel.toggleBookmark(wordData)
            setBookmarkState(isBookmarked)
        }

        binding.descriptionEditText.doAfterTextChanged {
            val noteText = it.toString()
            wordData = viewModel.updateNote(wordData, noteText)
        }
    }

    private fun setBookmarkState(state: Boolean) {
        val icon = if (state) R.drawable.ic_bookmark_24 else R.drawable.ic_bookmark_border_24
        binding.myToolbar.btnBookmark.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }


    companion object {
        const val EXTRA_EVENT = "extra_article"
    }

}