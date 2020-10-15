package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.Network
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private var viewModel: NewsViewModel? = null
    private val newsAdapter = NewsAdapter()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = ArticleDatabase.getInstance(requireContext())
        val service = Network.api
        val repository = NewsRepository(database, service)
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        }

        rvSearchNews.adapter = newsAdapter

        newsAdapter.onClickListener = {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        val query = viewModel?.currentQueryValue
        initSearch(query)
    }

    private fun initSearch(query: String?) {

        etSearch.apply {
            query.let {
                setText(it)
                updateArticleListFromInput()
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateArticleListFromInput()
                    true
                } else false
            }
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateArticleListFromInput()
                    true
                } else false
            }
        }

        lifecycleScope.launch {
            newsAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { rvSearchNews.scrollToPosition(0) }
        }
    }

    private fun updateArticleListFromInput() {
        etSearch.text.trim().let { query ->
            if (query.isNotEmpty()) {
                job?.cancel()
                job = lifecycleScope.launch {
                    viewModel?.searchNews(query.toString())?.collectLatest {
                        newsAdapter.submitData(it)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel?.currentQueryValue = etSearch.text.trim().toString()
    }
}