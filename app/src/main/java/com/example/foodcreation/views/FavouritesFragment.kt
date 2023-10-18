package com.example.foodcreation.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcreation.MainActivity
import com.example.foodcreation.R
import com.example.foodcreation.ViewModel.HomeFragmentViewModel
import com.example.foodcreation.adapters.FavouritesMealAdapter
import com.example.foodcreation.databinding.FragmentFavouritesBinding
import com.google.android.material.snackbar.Snackbar

class FavouritesFragment : Fragment() {

    private lateinit var  binding: FragmentFavouritesBinding
    private lateinit var  viewModel: HomeFragmentViewModel
    private lateinit var favouritesMealAdapter: FavouritesMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_favouritesFragment_to_homeFragment)
            }

        })

        prepareRecyclerView()
        observeFavourites()
        onItemClick()


        // on swipe a food item
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean  = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val  position = viewHolder.adapterPosition
                viewModel.deleteMeal(favouritesMealAdapter.differ.currentList[position])
                Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo", View.OnClickListener {
                        viewModel.insertMeal(favouritesMealAdapter.differ.currentList[position])
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavouties)
    }



    private fun prepareRecyclerView() {
        favouritesMealAdapter  = FavouritesMealAdapter()
        binding.rvFavouties.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter  = favouritesMealAdapter
        }
    }

    private fun observeFavourites(){
        viewModel.observerFavouriteMealsLiveData().observe(requireActivity(), Observer { meals ->
            favouritesMealAdapter.differ.submitList(meals)
        })
    }

    private fun onItemClick() {
        favouritesMealAdapter.onItemClickListener = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal) // id and data
            intent.putExtra(HomeFragment.MEAL_NAME,meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

}