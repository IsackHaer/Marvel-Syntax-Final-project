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
import com.syntax.haering.marvelsyntaxfinalproject.adapter.DetailComicsAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.DetailSeriesAdapter
import com.syntax.haering.marvelsyntaxfinalproject.adapter.SearchResultSerieAdapter
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

    val seriesAdapter = DetailSeriesAdapter()
    val comicsAdapter = DetailComicsAdapter()

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
        val getID = requireArguments().getInt("ID")

        viewModel.loadSingleCharacter(getID)

        viewModel.singleCharacter.observe(viewLifecycleOwner) { character ->
            setUpUi(character)
        }

        binding.detailCharBackBtn.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        return view
    }

    fun setUpUi(
        character: com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result
    ) {
        val https = character.thumbnail.path.replace("http", "https")

        viewModel.getCharacterSeriesList(character.series.collectionURI, character.comics.collectionURI)

        binding.detailCharImageIv.load("$https/landscape_incredible.${character.thumbnail.extension}") {
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_foreground)
        }

        binding.detailCharTitleTv.text = character.name
        binding.detailCharDescriptionTv.text = character.description

        binding.detailSeriesRv.adapter = seriesAdapter
        binding.detailComicsRv.adapter = comicsAdapter

        viewModel.detailSeriesForCharacter.observe(viewLifecycleOwner){
            lifecycleScope.launch { seriesAdapter.submitSerieList(it) }
        }

        viewModel.detailComicsForCharacter.observe(viewLifecycleOwner){
            lifecycleScope.launch { comicsAdapter.submitComicsList(it) }
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