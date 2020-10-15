package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.api.Network
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private var viewModel: NewsViewModel? = null
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = ArticleDatabase.getInstance(requireContext())
        val service = Network.api
        val repository = NewsRepository(database, service)
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        }

        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        fab.setOnClickListener {
            viewModel?.saveArticle(article)
            Snackbar.make(view, "Article was saved", Snackbar.LENGTH_SHORT).show()
        }
    }
}