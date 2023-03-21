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
import com.syntax.haering.marvelsyntaxfinalproject.databinding.FragmentDetailComicBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailComicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailComicFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentDetailComicBinding? = null
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
        _binding = FragmentDetailComicBinding.inflate(inflater, container, false)
        val view = binding.root
        val getID = requireArguments().getInt("ComicID")
        viewModel.loadSingleComic(getID)


        viewModel.singleComic.observe(viewLifecycleOwner){
            setUpUI(it)
        }

        return view
    }

    fun setUpUI(
        comic: com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result
    ){
        val creatorsList: MutableList<String> = mutableListOf()

        for (i in comic.creators.items){
            creatorsList.add(i.name)
        }

        lifecycleScope.launch {
            val https = comic.thumbnail.path.replace("http", "https")
            binding.detailComicImageIv.load("$https/portrait_uncanny.${comic.thumbnail.extension}") {
                placeholder(R.drawable.marvelcomics_loading)
                error(R.drawable.error404notfound_image)
            }
            binding.detailComicTitleTv.text = comic.title
            binding.detailComicIssueNRTv.text = "Issue: ${ comic.issueNumber }"
            binding.detailComicDescriptionTv.text = comic.description
            binding.detailComicCreatorsTv.text = creatorsList.toString()
            binding.detailComicIsbnTv.text = "ISBN: ${comic.isbn}"
            binding.detailComicIssnTv.text = "ISSN: ${ comic.issn }"
            binding.detailComicPageCountTv.text = "Pages: ${ comic.pageCount }"
            binding.detailComicPriceTv.text = "Price: ${ comic.prices.first().price }\$"
        }


        binding.detailComicBackBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
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
         * @return A new instance of fragment DetailComicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailComicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}