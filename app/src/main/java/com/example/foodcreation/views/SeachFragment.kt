package com.example.foodcreation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodcreation.MainActivity
import com.example.foodcreation.R
import com.example.foodcreation.ViewModel.HomeFragmentViewModel
import com.example.foodcreation.adapters.FavouritesMealAdapter
import com.example.foodcreation.databinding.FragmentSeachBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SeachFragment : Fragment() {

    private lateinit var binding: FragmentSeachBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var searchRecViewAdapter: FavouritesMealAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding  = FragmentSeachBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_seachFragment_to_homeFragment)
            }

        })

        obserSearchMealsLiveData()

        // using coroutine for search
        var searchJob: Job? = null
        binding.edSearchBox.addTextChangedListener { searchQuery ->
            if(searchQuery!!.isEmpty()){
                binding.searchProgressBar.visibility = View.VISIBLE
                binding.rvSearchedMeal.visibility = View.GONE
            }
            else
            {
                binding.rvSearchedMeal.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.VISIBLE
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(1000)
                    binding.searchProgressBar.visibility = View.INVISIBLE
                    viewModel.searchMeals(searchQuery.toString())
                }
            }

        }

    }

    private fun prepareRecyclerView() {
        searchRecViewAdapter = FavouritesMealAdapter()
        binding.rvSearchedMeal.apply {
            layoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL, false)
            adapter =  searchRecViewAdapter
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }


    private fun obserSearchMealsLiveData() {
        viewModel.observeSeachMealsLiveData().observe(viewLifecycleOwner, Observer{ mealList ->
            searchRecViewAdapter.differ.submitList(mealList)
        })
    }



}