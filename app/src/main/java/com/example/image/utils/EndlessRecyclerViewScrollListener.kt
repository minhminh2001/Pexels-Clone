package com.example.image.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class EndlessRecyclerViewScrollListener(var mLayoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {
    private val visibleThreshold = 5
    private var currentPage = 1
    private var previousTotalItemCount = 0
    private var loading = true

    private val startingPageIndex = 0

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = mLayoutManager.itemCount
        val lastVisibleItemPosition: Int = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold >= totalItemCount) {
            currentPage++
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()
}

