package com.syntax.haering.marvelsyntaxfinalproject.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.syntax.haering.marvelsyntaxfinalproject.HomeViewModel
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.adapter.SearchResultAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.SearchResultSerieAdapter
import com.syntax.haering.marvelsyntaxfinalproject.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [search.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        val searchAdapter = SearchResultAdapter()
        val searchSerieAdapter = SearchResultSerieAdapter()



        keyboardSearchOnClick(view)

        viewModel.searchCategoryBtnState.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    categoryBtnState(it)
                    binding.searchRv.adapter = searchAdapter
                    viewModel.searchedCharacter.observe(viewLifecycleOwner) { characterList ->
                        lifecycleScope.launch { searchAdapter.submitSearchList(characterList) }
                    }
                }
                false -> {
                    categoryBtnState(it)
                    binding.searchRv.adapter = searchSerieAdapter
                    viewModel.searchedSerie.observe(viewLifecycleOwner) { serieList ->
                        lifecycleScope.launch { searchSerieAdapter.submitSearchList(serieList) }
                    }
                }
            }
        }

        binding.searchCharacterBtn.setOnClickListener {
            viewModel.changeSearchCategory(true)
        }

        binding.searchSeriesBtn.setOnClickListener {
            viewModel.changeSearchCategory(false)
        }

        return view
    }

    fun categoryBtnState(it: Boolean) {
        when (it) {
            true -> {
                binding.searchCharacterBtn.setBackgroundColor(
                    getColor(
                        resources,
                        com.google.android.material.R.color.design_default_color_primary,
                        null
                    )
                )
                binding.searchCharacterBtn.setTextColor(getColor(resources, R.color.white, null))

                binding.searchSeriesBtn.setBackgroundColor(
                    getColor(
                        resources,
                        androidx.appcompat.R.color.material_grey_300,
                        null
                    )
                )
                binding.searchSeriesBtn.setTextColor(getColor(resources, R.color.black, null))

            }
            false -> {
                binding.searchCharacterBtn.setBackgroundColor(
                    getColor(
                        resources,
                        androidx.appcompat.R.color.material_grey_300,
                        null
                    )
                )
                binding.searchCharacterBtn.setTextColor(getColor(resources, R.color.black, null))

                binding.searchSeriesBtn.setBackgroundColor(
                    getColor(
                        resources,
                        com.google.android.material.R.color.design_default_color_primary,
                        null
                    )
                )
                binding.searchSeriesBtn.setTextColor(getColor(resources, R.color.white, null))
            }
        }
    }

    fun keyboardSearchOnClick(view: View) {
        binding.searchInputEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchTerm = binding.searchInputEdit.text.toString()
                if (!binding.searchInputEdit.text.isNullOrEmpty()) {
                    viewModel.searchedTerm(searchTerm, searchTerm)
                }
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
            true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment search.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}