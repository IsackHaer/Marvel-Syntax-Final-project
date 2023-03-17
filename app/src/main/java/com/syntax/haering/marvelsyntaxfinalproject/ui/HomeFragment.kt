package com.syntax.haering.marvelsyntaxfinalproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
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

        binding.homeAdvertComicRv.adapter = advertAdapter
        binding.homeSeriesRv.adapter = homeSeriesAdapter
        binding.homeBottomRv.adapter = homeAdapter
        snapHelper.attachToRecyclerView(binding.homeAdvertComicRv)


        viewModel.apiStatus.observe(viewLifecycleOwner){
            when (it) {
                HomeViewModel.APIStatus.LOADING -> binding.homeMarvelGifCv.visibility = View.VISIBLE
                HomeViewModel.APIStatus.DONE -> binding.homeMarvelGifCv.visibility = View.GONE
                else -> {
                    binding.homeMarvelGifCv.visibility = View.GONE
                }
            }
        }

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


        binding.homeIronmanCv.setOnClickListener { navigateToCharacter(1009368) }
        binding.homeSpidermanCv.setOnClickListener { navigateToCharacter(1009610) }
        binding.homeThorCv.setOnClickListener { navigateToCharacter(1009664) }
        binding.homeCapAmericaCv.setOnClickListener { navigateToCharacter(1009220) }
        binding.homeBlackwidowCv.setOnClickListener { navigateToCharacter(1017109) }
        binding.homeBlackpantherCv.setOnClickListener { navigateToCharacter(1009187) }
        binding.homeAntmanCv.setOnClickListener { navigateToCharacter(1010802) }
        binding.homeHulkCv.setOnClickListener { navigateToCharacter(1009351) }

        binding.homeDrDoomCv.setOnClickListener { navigateToCharacter(1010324) }
        binding.homeVenomCv.setOnClickListener { navigateToCharacter(1009663) }
        binding.homeGalacticusCv.setOnClickListener { navigateToCharacter(1009312) }
        binding.homeLokiCv.setOnClickListener { navigateToCharacter(1009407) }
        binding.homeMagnetoCv.setOnClickListener { navigateToCharacter(1009417) }
        binding.homeUltronCv.setOnClickListener { navigateToCharacter(1009685) }
        binding.homeBullseyeCv.setOnClickListener { navigateToCharacter(1009212) }
        binding.homeThanosCv.setOnClickListener { navigateToCharacter(1009652) }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToCharacter(id: Int){
        Navigation.findNavController(binding.root).navigate(HomeFragmentDirections.actionHomeFragmentToDetailCharacterFragment(id))
    }
}