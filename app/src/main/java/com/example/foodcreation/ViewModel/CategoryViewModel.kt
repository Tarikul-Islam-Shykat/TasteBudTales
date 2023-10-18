package com.example.foodcreation.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodcreation.Model.CategoryMeal
import com.example.foodcreation.Model.mealsByCategoryList
import com.example.foodcreation.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel: ViewModel() {
    val mealsLiveData = MutableLiveData<List<CategoryMeal>>()

    fun getMealsByCateogory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<mealsByCategoryList> {

            override fun onResponse(call: Call<mealsByCategoryList>, response: Response<mealsByCategoryList>) {
                response.body()?.let { mealList ->
                    mealsLiveData.postValue(mealList.meals)
                    //Log.d("catview", mealsLiveData.toString())
                }
            }
            override fun onFailure(call: Call<mealsByCategoryList>, t: Throwable) {
            }

        })
    }

    fun  observeMealLiveData():LiveData<List<CategoryMeal>>{
        return  mealsLiveData
    }




}