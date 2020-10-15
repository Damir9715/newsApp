package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.Network
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private var viewModel: NewsViewModel? = null
    private val newsAdapter = NewsAdapter()
    private var job: Job? = null
//    private val model: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = ArticleDatabase.getInstance(requireContext())
        val service = Network.api
        val repository = NewsRepository(database, service)
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        }

        rvBreakingNews.adapter = newsAdapter

        newsAdapter.onClickListener = {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        getBreakingNews()
    }

    private fun getBreakingNews() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel?.getBreakingNews()?.collectLatest {
                newsAdapter.submitData(it)
            }
        }
    }
}