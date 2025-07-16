package com.android.translateappcouchbase.data.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.translateappcouchbase.data.model.WordEntity
import com.android.translateappcouchbase.databinding.ItemWordListBinding
import com.android.translateappcouchbase.ui.detail.DetailActivity

class WordAdapter: ListAdapter<WordEntity, WordAdapter.ViewHolder>(DIFF_CALLBACK) {


    class ViewHolder(private val binding: ItemWordListBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data:WordEntity) {
            binding.apply {
                tvWordName.text = data.word

                itemView.setOnClickListener {
                    val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                    intentDetail.putExtra(DetailActivity.EXTRA_EVENT, data)
                    itemView.context.startActivity(intentDetail)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<WordEntity> =
            object : DiffUtil.ItemCallback<WordEntity>() {


                override fun areItemsTheSame(oldItem: WordEntity, storyItem: WordEntity): Boolean {
                    return oldItem.word == storyItem.word
                }


                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: WordEntity, storyItem: WordEntity): Boolean {
                    return oldItem == storyItem
                }
            }
    }
}