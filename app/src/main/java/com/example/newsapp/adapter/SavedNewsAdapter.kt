package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class SavedNewsAdapter : ListAdapter<Article, SavedNewsAdapter.ViewHolder>(DiffCallback()) {

    var onClickListener: ((Article) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.itemView.apply {
                Glide.with(this).load(item.urlToImage).into(ivArticle)
                tvTitle.text = item.title
                tvDescription.text = item.description
                tvSource.text = item.source.name
                tvPublishedAt.text = item.publishedAt
                setOnClickListener {
                    onClickListener?.let { it(item) }
                }
            }
        }
    }

}