package com.example.image.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.common.repository.HomeRepository
import com.example.image.R
import com.example.image.ViewModelProviderFactory
import com.example.image.databinding.ActivityMainBinding
import com.example.image.ui.fragment.ImageDetailFragment
import com.example.image.ui.fragment.ListImageFragment
import com.example.image.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
    }

    private fun initViewModel() {
        val repository = HomeRepository()
        val viewModelProviderFactory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]
    }

    fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}