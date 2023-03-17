package com.example.image.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.image.R
import com.example.image.databinding.FragmentSecondBinding
import com.example.image.ui.activity.MainActivity
import com.example.image.ui.adapter.SlideImageAdapter
import com.example.image.utils.OnEventControlListener
import com.example.image.viewmodel.MainViewModel
import com.example.model.data.Photo


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ImageDetailFragment : Fragment(), OnEventControlListener {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var photo: Photo? = Photo()
    private var listPhotos: ArrayList<Photo>? = null
    private var mainActivity: MainActivity? = null
    private lateinit var slideImageAdapter: SlideImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = (activity as MainActivity)
        viewModel = mainActivity!!.viewModel
        photo = arguments?.getParcelable("photo") as Photo?
        listPhotos = arguments?.getParcelableArrayList("list")
        photo?.let { viewModel.getImageDetail(it.id) }
        slideImageAdapter =
            SlideImageAdapter(requireContext(), this, viewPager2 = binding.viewPager, viewModel)
        binding.viewPager.adapter = slideImageAdapter
        slideImageAdapter.differ.submitList(listPhotos?.toList())
        for (i in 0 until listPhotos!!.count()) {
            if (listPhotos!![i].id == photo!!.id) {
                binding.viewPager.setCurrentItem(i, false)
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            private val thresholdOffset = 0.5f
            private var scrollStarted = false
            private var checkDirection: Boolean = false

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (checkDirection) {
                    if (thresholdOffset > positionOffset) {
                        if (position == 0) {
                            mainActivity!!.onBackPressed()
                        }
                    }
                    checkDirection = false;
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {

    }
}