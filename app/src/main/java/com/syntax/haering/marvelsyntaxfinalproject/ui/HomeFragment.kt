package com.syntax.haering.marvelsyntaxfinalproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import com.syntax.haering.marvelsyntaxfinalproject.HomeViewModel
import com.syntax.haering.marvelsyntaxfinalproject.adapter.HomeAdvertComicAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.HomeCharacterAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.HomeSeriesListAdapter
import com.syntax.haering.marvelsyntaxfinalproject.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val snapHelper = PagerSnapHelper()
        val homeAdapter = HomeCharacterAdapter()
        val advertAdapter = HomeAdvertComicAdapter()
        val homeSeriesAdapter = HomeSeriesListAdapter()

        binding.homeBottomRv.adapter = homeAdapter
        binding.homeAdvertComicRv.adapter = advertAdapter
        binding.homeSeriesRv.adapter = homeSeriesAdapter

        viewModel.character.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                homeAdapter.submitList(it)
            }
        }
        viewModel.advertComics.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                advertAdapter.submitComicAdverts(it)
            }
        }
        viewModel.homeSeriesList.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                homeSeriesAdapter.submitSeriesList(it)
            }
        }

        snapHelper.attachToRecyclerView(binding.homeAdvertComicRv)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}