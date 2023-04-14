package com.syntax.haering.marvelsyntaxfinalproject.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.syntax.haering.marvelsyntaxfinalproject.HomeViewModel
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.adapter.HomeAdvertComicAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.HomeCharacterAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.HomeSeriesListAdapter
import com.syntax.haering.marvelsyntaxfinalproject.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    //for advertRecyclerView
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var isTimerRunning = false

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

//        binding.homeAdvertComicRv.adapter = advertAdapter
        binding.homeSeriesRv.adapter = homeSeriesAdapter
        binding.homeBottomRv.adapter = homeAdapter
        snapHelper.attachToRecyclerView(binding.homeAdvertComicRv)

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


        lifecycleScope.launch {
            viewModel.loadLibraryCharList()
            viewModel.loadLibrarySeriesList()
        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it == null) {
                Navigation.findNavController(view).navigate(R.id.signInFragment)
            }
        }

        viewModel.apiStatus.observe(viewLifecycleOwner) {
            when (it) {
                HomeViewModel.APIStatus.LOADING -> binding.homeMarvelGifCv.visibility = View.VISIBLE
                HomeViewModel.APIStatus.DONE -> binding.homeMarvelGifCv.visibility = View.GONE
                else -> {
                    binding.homeMarvelGifCv.visibility = View.GONE
                }
            }
        }

        viewModel.character.observe(viewLifecycleOwner) {
            homeAdapter.submitList(it)
        }
        viewModel.advertComics.observe(viewLifecycleOwner) {
//            advertAdapter.submitComicAdverts(it)
            setupPagination(binding.homeAdvertComicRv, advertAdapter, it)
        }

        viewModel.homeSeriesList.observe(viewLifecycleOwner) {
            homeSeriesAdapter.submitSeriesList(it)
        }

        return view
    }


    private fun setupPagination(
        recyclerView: RecyclerView,
        adapter: HomeAdvertComicAdapter,
        list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>
    ) {
        mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        recyclerView.layoutManager = mLayoutManager
        /*recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.HORIZONTAL
            )
        )*/
        recyclerView.adapter = adapter
        adapter.submitComicAdverts(list)

        recyclerView.setOnScrollChangeListener { _, scrollX, scrollY, oldScrollX, oldScrollY ->
            visibleItemCount = mLayoutManager!!.childCount
            totalItemCount = mLayoutManager!!.itemCount
            pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
        }

        //timer for advertisment automatic sliding : function is further down this page
        startTimer(recyclerView)

        //scrollListener that scrolls to the first item once reaching the last item.
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx > 0) {
                    visibleItemCount = mLayoutManager!!.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                    /*Snackbar.make(
                        requireView(),
                        "$visibleItemCount,$pastVisibleItems, $totalItemCount ",
                        Snackbar.LENGTH_SHORT
                    ).show()*/

                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        /*Snackbar.make(requireView(), "this is the last item", Snackbar.LENGTH_SHORT)
                            .show()*/
                        recyclerView.scrollToPosition(0)
                    }
                }
            }
        })

    }


    fun startTimer(recyclerView: RecyclerView){
        if (!isTimerRunning){
            isTimerRunning = true
            val timer = object : CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    if (mLayoutManager!!.findLastVisibleItemPosition() < totalItemCount) {
                        mLayoutManager!!.smoothScrollToPosition(
                            recyclerView,
                            null,
                            mLayoutManager!!.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else if (mLayoutManager!!.findLastVisibleItemPosition() >= totalItemCount) {
                        mLayoutManager!!.smoothScrollToPosition(recyclerView, null, 0)
                    }
                    isTimerRunning = false
                    startTimer(recyclerView)
                }
            }
            timer.start()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToCharacter(id: Int) {
        Navigation.findNavController(binding.root)
            .navigate(HomeFragmentDirections.actionHomeFragmentToDetailCharacterFragment(id))
    }
}