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
import com.syntax.haering.marvelsyntaxfinalproject.adapter.DetailSeriesComicsAdapter
import com.syntax.haering.marvelsyntaxfinalproject.databinding.FragmentDetailSerieBinding
import kotlinx.coroutines.launch
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailSerieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailSerieFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentDetailSerieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private val comicAdapter = DetailSeriesComicsAdapter()

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
        _binding = FragmentDetailSerieBinding.inflate(inflater, container, false)
        val view = binding.root
        val getID = requireArguments().getInt("SerieID")
        viewModel.loadSingleSerie(getID)

        viewModel.singleSerie.observe(viewLifecycleOwner) { serie ->
            setUpUI(serie)
        }

        return view
    }

    fun setUpUI(
        serie: com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result
    ) {
        lifecycleScope.launch {
            val https = serie.thumbnail.path.replace("http", "https")
            viewModel.loadComicCollection(serie.comics.collectionURI)

            binding.detailSerieImageIv.load("$https/portrait_uncanny.${serie.thumbnail.extension}") {
                placeholder(R.drawable.marvelcomics_loading)
                error(R.drawable.error404notfound_image)
            }

            binding.detailSeriesTitleTv.text = serie.title
            binding.detailSeriesDescriptionTv.text = serie.description
        }


        binding.detailSeriesComicCollectionRv.adapter = comicAdapter

        binding.detailSerieBackBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        viewModel.librarySeriesList.observe(viewLifecycleOwner){ list ->
            if (list != null){
                viewModel.isSerieInLibrary(serie.id, list)

                viewModel.isSavedInLibrary.observe(viewLifecycleOwner){
                    when (it) {
                        true -> {
                            binding.detailSerieFavBtn.setImageResource(R.drawable.baseline_star_24)
                            binding.detailSerieFavBtn.setOnClickListener {
                                viewModel.deleteLibrarySerie(serie.id.toString())
                            }
                        }
                        else -> {
                            binding.detailSerieFavBtn.setImageResource(R.drawable.baseline_star_border_24)
                            binding.detailSerieFavBtn.setOnClickListener {
                                viewModel.addLibrarySerie(serie.id.toString(), Date())
                            }
                        }
                    }
                }
            }
        }

        viewModel.loadComicStatus.observe(viewLifecycleOwner){
            when (it){
                HomeViewModel.APIStatus.LOADING -> binding.detailSerieComicProgressBar.visibility = View.VISIBLE
                else -> binding.detailSerieComicProgressBar.visibility = View.GONE
            }
        }

        viewModel.detailComicsCollection.observe(viewLifecycleOwner){
            val sortedByIssue = it.sortedBy { it.issueNumber }.toMutableList()
            comicAdapter.submitComicsList(sortedByIssue)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.detailComicsCollection.value?.clear()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailSerieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailSerieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}