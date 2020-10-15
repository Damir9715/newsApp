package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.SavedNewsAdapter
import com.example.newsapp.api.Network
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    private var viewModel: NewsViewModel? = null
    private val savedNewsAdapter = SavedNewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = ArticleDatabase.getInstance(requireContext())
        val service = Network.api
        val repository = NewsRepository(database, service)
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        }

        rvSavedNews.adapter = savedNewsAdapter

        savedNewsAdapter.onClickListener = {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val article = savedNewsAdapter.currentList[position]
                viewModel?.deleteArticle(article)
                Snackbar.make(view, "Article was deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") { viewModel?.saveArticle(article) }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSavedNews)

        viewModel?.getAllArticles()?.observe(viewLifecycleOwner) { articles ->
            savedNewsAdapter.submitList(articles)
        }
    }

}