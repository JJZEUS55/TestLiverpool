package com.example.baseandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.baseandroid.MainActivity
import com.example.baseandroid.R
import com.example.baseandroid.databinding.SearchFragmentBinding
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private val adapter by lazy {
        SearchAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SearchFragmentBinding>(inflater, R.layout.search_fragment, container, false)
        binding.searchViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.passSearch(object : SearchDataPass {
            override fun search(searchWords: String) {
                adapter.numPage = 1
                adapter.wordSearched = searchWords
                viewModel.getSearch(adapter.wordSearched, 1)
            }
        })
        rv_items.adapter = adapter
        rv_items.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.numPage++
                    viewModel.getSearch(adapter.wordSearched, adapter.numPage.toLong())
                }
            }
        })
//        viewModel.getSearch("computadora", 1)
    }

    private fun subscribeLiveData() {
        viewModel.liveDataSearchItems.observe(this, Observer {
                adapter.submitList(it)
        })

        viewModel.liveDataError.observe(this, Observer {
            alertNoMoreProducts(it)
        })

        viewModel.liveDataSearching.observe(this, Observer {
           if(it) {
               progress_bar.visibility = View.VISIBLE
           } else {
               progress_bar.visibility = View.GONE
           }
        })
    }

    private fun alertNoMoreProducts(errorCode: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error $errorCode")
            .setMessage(getString(R.string.label_no_more_products))
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

}