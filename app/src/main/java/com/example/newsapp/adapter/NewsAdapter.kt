package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    val diff = AsyncListDiffer(this, ArticleDiffCallback())

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            // from api articles doesn't have, so use URL as ID, but by single source of truth
            // we get articles only from database ???
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview, parent, false)
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = diff.currentList[position]
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

    /*private */var onClickListener: ((Article) -> Unit)? = null

//    fun setOnClickListener(listener: (Article) -> Unit) {
//        onClickListener = listener
//    }
}