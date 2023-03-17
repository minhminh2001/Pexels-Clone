package com.example.image.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.utils.Resource
import com.example.common.utils.ServerPath
import com.example.image.R
import com.example.image.databinding.FragmentFirstBinding
import com.example.image.ui.activity.MainActivity
import com.example.image.ui.adapter.ListImageAdapter
import com.example.image.utils.Constants
import com.example.image.utils.Constants.Companion.DEFAULT_PER_PAGE
import com.example.image.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.image.utils.EndlessRecyclerViewScrollListener
import com.example.image.utils.OnEventControlListener
import com.example.image.viewmodel.MainViewModel
import com.example.model.data.Photo
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListImageFragment : Fragment(), OnEventControlListener {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var listImageAdapter: ListImageAdapter
    private lateinit var viewModel: MainViewModel
    private var isLoading = false
    private var isLoadingFirst = false
    private var isCheckSearch = false
    private var listPhotos: MutableList<Photo> = mutableListOf()
    private var searchPhotos: MutableList<Photo> = mutableListOf()
    private var mainActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = (activity as MainActivity)
        viewModel = mainActivity!!.viewModel
        setupRecyclerView()
        var job: Job? = null
        binding.edSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        isCheckSearch = true
                        viewModel.searchImages(
                            1, editable.toString(), DEFAULT_PER_PAGE, ServerPath.token
                        )
                    } else {
                        viewModel.getListImages(
                            viewModel.imageListNewsPage, DEFAULT_PER_PAGE, ServerPath.token
                        )
                    }
                }
            }
        }
        viewModel.searchImageLiveData.observe(viewLifecycleOwner) { searchResponse ->
            when (searchResponse) {
                is Resource.Success -> {
                    mainActivity!!.hideProgressBar()
                    isLoading = false
                    searchResponse.data?.let { searchImagesResponse ->
                        if (isCheckSearch) {
                            searchPhotos = searchImagesResponse.photos
                        } else {
                            if (searchPhotos.size == 0) {
                                searchPhotos = searchImagesResponse.photos
                            } else {
                                searchPhotos.addAll(searchImagesResponse.photos)
                            }
                        }
                        listImageAdapter.differ.submitList(searchPhotos)
                    }
                }
                is Resource.Error -> {
                    mainActivity!!.hideProgressBar()
                    isLoading = false
                    searchResponse.message?.let { message ->
                        Toast.makeText(context, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    if (!isLoadingFirst) {
                        mainActivity!!.showProgressBar()
                        isLoading = true
                    }
                }
            }
        }
        viewModel.deleteImageLiveData.observe(viewLifecycleOwner) { photo ->
            try {
                if (searchPhotos.size == 0) {
                    listPhotos.remove(photo)
                } else {
                    searchPhotos.remove(photo)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "An error when delete: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
        viewModel.imageListLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    searchPhotos.clear()
                    mainActivity!!.hideProgressBar()
                    isLoading = false
                    response.data?.let { imagesResponse ->
                        if (listPhotos.size == 0) {
                            listPhotos = imagesResponse.photos
                        } else {
                            listPhotos.addAll(imagesResponse.photos)
                        }
                        listImageAdapter.differ.submitList(listPhotos.toList())
                    }
                }
                is Resource.Error -> {
                    mainActivity!!.hideProgressBar()
                    isLoading = false
                    response.message?.let { message ->
                        Toast.makeText(context, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    if (!isLoadingFirst) {
                        mainActivity!!.showProgressBar()
                        isLoading = true
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView() {
        listImageAdapter = ListImageAdapter(requireContext(), this)
        binding.rvListImage.apply {
            adapter = listImageAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            binding.rvListImage.addOnScrollListener(object : EndlessRecyclerViewScrollListener(
                layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore() {
                    isLoadingFirst = true
                    if (binding.edSearch.text.toString().trim() == "") {
                        isCheckSearch = false
                        viewModel.getListImages(
                            viewModel.imageListNewsPage, DEFAULT_PER_PAGE, ServerPath.token
                        )
                    } else {
                        viewModel.searchImages(
                            viewModel.searchNewsPage,
                            binding.edSearch.text.toString().trim(),
                            DEFAULT_PER_PAGE,
                            ServerPath.token
                        )
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        if (eventAction == Constants.ACTION_CLICK_ITEM_PHOTO) {
            val bundle = Bundle().apply {
                putParcelable("photo", data as Parcelable?)
                if (searchPhotos.size == 0) {
                    putParcelableArrayList("list", listPhotos as ArrayList<Photo>)
                } else {
                    putParcelableArrayList("list", searchPhotos as ArrayList<Photo>)
                }
            }
            findNavController().navigate(
                R.id.action_FirstFragment_to_SecondFragment, bundle
            )
        }
    }
}