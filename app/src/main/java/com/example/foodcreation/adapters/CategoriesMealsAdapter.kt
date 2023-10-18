package com.example.foodcreation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodcreation.Model.CategoryMeal
import com.example.foodcreation.Model.MealList
import com.example.foodcreation.Model.mealsByCategoryList
import com.example.foodcreation.databinding.MealItemCateogrySampleBinding

class CategoriesMealsAdapter: RecyclerView.Adapter<CategoriesMealsAdapter.CategoryMealsViewHolder>() {

    private  var mealsList = ArrayList<CategoryMeal>()

    fun setMealList(mealList: List<CategoryMeal>){
        mealsList.clear() // Clear the previous data
        mealsList.addAll(mealList) // Add the new data
        notifyDataSetChanged() // Notify the adapter of the data change
    }

    inner  class CategoryMealsViewHolder(val binding: MealItemCateogrySampleBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return  CategoryMealsViewHolder(MealItemCateogrySampleBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        Log.d("catAdapter2", mealsList.size.toString())
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

}