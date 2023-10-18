package com.example.foodcreation.api

import com.example.foodcreation.Model.CategoryList
import com.example.foodcreation.Model.MealList
import com.example.foodcreation.Model.mealsByCategoryList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    // meal details
    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id: String) : Call<MealList>


    // list of popularitems
    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName: String) : Call<mealsByCategoryList>

    @GET("categories.php")
    fun  getCategories(): Call<CategoryList>

    // list of popularitems
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String) : Call<mealsByCategoryList>

    // searchMeal
    @GET("search.php")
    fun searchMeal(@Query("s") searchQuery: String) : Call<MealList>

}