package com.example.foodcreation.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodcreation.Model.CategoryMeal
import com.example.foodcreation.R
import com.example.foodcreation.ViewModel.CategoryViewModel
import com.example.foodcreation.adapters.CategoriesMealsAdapter
import com.example.foodcreation.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {
    lateinit var  binding: ActivityCategoryBinding
    lateinit var  categoryViewModel: CategoryViewModel
    lateinit var categoryMealAdapter : CategoriesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryViewModel = ViewModelProviders.of(this)[CategoryViewModel::class.java]
        categoryViewModel.getMealsByCateogory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryViewModel.observeMealLiveData().observe(this, Observer { mealList ->
            Log.d("Size", mealList.size.toString())
            binding.tvCategoryCount.text = mealList.size.toString()
            categoryMealAdapter.setMealList(mealList)
        })


    }

    private fun prepareRecyclerView() {
       categoryMealAdapter = CategoriesMealsAdapter()
        binding.rvMealsCategoriesMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }
}