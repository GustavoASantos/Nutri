package com.gustavoas.nutri.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gustavoas.nutri.R
import com.gustavoas.nutri.repository.SearchDatabase
import com.gustavoas.nutri.repository.SearchRepository
import com.gustavoas.nutri.viewmodel.SearchViewModel
import com.gustavoas.nutri.viewmodel.SearchViewModelFactory

class SearchFragment: Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "sortOption") {
            offset = 0
            adapter.clearItems()
            loadProfessionals()
        }
    }

    private lateinit var recyclerView: RecyclerView
    private val database by lazy { SearchDatabase.getDatabase(requireContext()) }
    private val apiCacheDao by lazy { database.apiCacheDao() }
    private val adapter: SearchAdapter by lazy { SearchAdapter(mutableListOf()) }
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this, SearchViewModelFactory(SearchRepository(apiCacheDao)))[SearchViewModel::class.java]
    }

    private var offset = 0
    private val limit = 4
    private var totalCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (savedInstanceState == null) {
            observeData()
        }

        if (viewModel.professionals.value == null) {
            recyclerView.post {
                loadProfessionals()
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (viewModel.isLoading.value == false && lastVisibleItem == adapter.itemCount.minus(1) && offset < totalCount) {
                    loadProfessionals()
                }
            }
        })

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun loadProfessionals() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val sortBy = preferences.getString("sortOption", "best_match") ?: "best_match"
        viewModel.loadProfessionals(requireContext(), limit, offset, sortBy)
        offset += limit
    }

    private fun observeData() {
        viewModel.professionals.observe(viewLifecycleOwner) { response ->
            totalCount = response.count

            adapter.addItems(response.professionals, response.offset)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                adapter.showLoading()
            } else {
                adapter.hideLoading()
            }
        }
    }
}