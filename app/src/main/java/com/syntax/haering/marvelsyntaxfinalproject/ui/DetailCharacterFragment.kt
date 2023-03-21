package com.syntax.haering.marvelsyntaxfinalproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.HomeViewModel
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.adapter.DetailCharacterComicsAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.DetailCharacterSeriesAdapter
import com.syntax.haering.marvelsyntaxfinalproject.databinding.FragmentDetailCharacterBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailCharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailCharacterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentDetailCharacterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    val seriesAdapter = DetailCharacterSeriesAdapter()
    val comicsAdapter = DetailCharacterComicsAdapter()

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
        _binding = FragmentDetailCharacterBinding.inflate(inflater, container, false)
        val view = binding.root
        val getID = requireArguments().getInt("CharacterID")

        viewModel.loadSingleCharacter(getID)

        viewModel.singleCharacter.observe(viewLifecycleOwner) { character ->
            setUpUi(character)
        }

        return view
    }

    fun setUpUi(
        character: com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result
    ) {
        val https = character.thumbnail.path.replace("http", "https")

        lifecycleScope.launch{

            viewModel.loadSerieCollection(character.series.collectionURI)
            viewModel.loadComicCollection(character.comics.collectionURI)

            binding.detailCharImageIv.load("$https/portrait_uncanny.${character.thumbnail.extension}") {
                placeholder(R.drawable.marvelcomics_loading)
                error(R.drawable.error404notfound_image)
            }

            binding.detailCharTitleTv.text = character.name
            binding.detailCharDescriptionTv.text = character.description
        }


        binding.detailSeriesRv.adapter = seriesAdapter
        binding.detailComicsRv.adapter = comicsAdapter

        binding.detailCharBackBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        viewModel.loadSerieStatus.observe(viewLifecycleOwner){
            when (it){
                HomeViewModel.APIStatus.LOADING -> {
                    binding.detailCharSeriesProgressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.detailCharSeriesProgressBar.visibility = View.GONE
                }
            }
        }

        viewModel.loadComicStatus.observe(viewLifecycleOwner){
            when (it){
                HomeViewModel.APIStatus.LOADING -> {
                    binding.detailCharComicsProgressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.detailCharComicsProgressBar.visibility = View.GONE
                }
            }
        }

        viewModel.detailSeriesCollection.observe(viewLifecycleOwner){
            val sortedByEndYear = it.sortedBy { it.endYear }.toMutableList()
            lifecycleScope.launch { seriesAdapter.submitSerieList(sortedByEndYear) }
        }

        viewModel.detailComicsCollection.observe(viewLifecycleOwner){
            val sortedByIssue = it.sortedBy { it.issueNumber }.toMutableList()
            lifecycleScope.launch { comicsAdapter.submitComicsList(sortedByIssue) }
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
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailCharacterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}