package com.example.image

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.common.repository.HomeRepository
import com.example.image.viewmodel.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelProviderFactory(
    private val app: Application,
    private val homeRepository: HomeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(homeRepository, app) as T
    }
}