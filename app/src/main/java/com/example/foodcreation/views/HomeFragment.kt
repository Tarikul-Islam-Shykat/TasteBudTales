package com.example.foodcreation.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodcreation.MainActivity
import com.example.foodcreation.Model.CategoryMeal
import com.example.foodcreation.Model.Meal
import com.example.foodcreation.R
import com.example.foodcreation.ViewModel.HomeFragmentViewModel
import com.example.foodcreation.adapters.CategoriesAdapter
import com.example.foodcreation.adapters.mostPopularItemAdapter
import com.example.foodcreation.databinding.FragmentHomeBinding
import com.example.foodcreation.views.bottomSheet.MealBottomSheetFragment


class HomeFragment : Fragment() {

    private lateinit var home_fragment_binding : FragmentHomeBinding
    private  lateinit var viewModel : HomeFragmentViewModel
    private lateinit var randomMeal: Meal
    private  lateinit var  popularItemAdapter: mostPopularItemAdapter
    private  lateinit var categoryAdapter: CategoriesAdapter

    companion object{
        const val  MEAL_ID = "homeFragment_idMeal"
        const val MEAL_NAME = "homeFragment_nameMeal"
        const val MEAL_THUMB = "homeFragment_thumbMeal"
        const val  CATEGORY_NAME = "from_homeFragment_to_category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //home_MVVM  = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java) // initializing the view model class
        viewModel = (activity as MainActivity).viewModel // accessing it from the main activity
        popularItemAdapter = mostPopularItemAdapter() // initialize the adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        home_fragment_binding = FragmentHomeBinding.inflate(inflater, container, false)
        return home_fragment_binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // when the view is created
        super.onViewCreated(view, savedInstanceState)
        // recyclerView
        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandoMeal()

        viewModel.getPopupularItems()
        observerPopularItemLiveData()

        prepareCategoryItemsRecyclerView()
        viewModel.getCategories()
        observerCategoriesLiveData()

        onPopularItemClick()
        onRandomMealClick()
        onCateogiesItemClick()

        onPopularLongItemClick()
        onsearchItemClick()

    }



    private fun onPopularLongItemClick() { // for long time press
        popularItemAdapter.onLongItemClickListener = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }

    }

    private fun onCateogiesItemClick() {
        categoryAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoryItemsRecyclerView() {
        categoryAdapter = CategoriesAdapter()
        home_fragment_binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
    }

    private fun observerCategoriesLiveData() {
        viewModel.observerGetCategoriesItemLiveData().observe(viewLifecycleOwner, Observer{ categories ->
                categoryAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClickListener ={ meal->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal) // id and data
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun observerPopularItemLiveData() {
        viewModel.observerPopularItemsLiveData().observe(viewLifecycleOwner) { popularMealList ->
            popularItemAdapter.setMeals(
                mealList = popularMealList as ArrayList<CategoryMeal>  // setting value in the adapter.
            )
        }
    }

    private fun preparePopularItemsRecyclerView() {
        home_fragment_binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter  = popularItemAdapter
        }
    }

    private fun onRandomMealClick() {
        home_fragment_binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal) // id and data
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandoMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(home_fragment_binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }

    private fun onsearchItemClick() {
        home_fragment_binding.imageSearchGg.setOnClickListener {
            //Toast.makeText(requireContext(), "pressing", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_seachFragment)
        }
    }




}