package com.example.foodcreation.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcreation.Model.*

import com.example.foodcreation.api.RetrofitInstance
import com.example.foodcreation.db.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel(private val mealDatabase: MealDatabase):ViewModel() {

    private  val randomMealLiveData = MutableLiveData<Meal>()
    private val popularItemLivedata = MutableLiveData<List<CategoryMeal>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favouritesMealsLIveData = mealDatabase.mealDao().getALlMeals()
    private val bottomSheetMealLiveData = MutableLiveData<Meal>()
    private val seachedMealsLiveData = MutableLiveData<List<Meal>>()


    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) { // if retro fit gets response
                    val randomMeal: Meal =  response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }
                else {
                    return
                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) { // if retrofit dont get any response
                Log.d("home fragment", t.message.toString())
            }
        }) // make sure you use te call back from retrofit
    }


    fun getPopupularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<mealsByCategoryList>{
            override fun onResponse(call: Call<mealsByCategoryList>, response: Response<mealsByCategoryList>) {
                if(response.body() != null) {
                    popularItemLivedata.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<mealsByCategoryList>, t: Throwable) {
                Log.d("failed","falied in getPopularItems")
            }

        })
    }


    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                /*if(response.body() != null) {
                    categoryLiveData.value  = response.body()!!.categories
                }*/
                // another way
                response.body()?.let { categoryList ->
                    categoryLiveData.postValue(categoryList.categories)
                    Log.d("success", "Failed to get popular item")
                }
            }
            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("failed_c", "Failed to get popular item")
            }

        })
    }


    fun getMealById(id: String) { // for bottom sheet fragment
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeView", t.message.toString())
            }

        })

    }


    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }


    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }


    fun observeRandomMealLiveData():LiveData<Meal>{ // from home fragment we are going to observe the changes.
        return  randomMealLiveData
    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeal(searchQuery).enqueue(object : Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val mealList = response.body()?.meals
            mealList?.let {
                seachedMealsLiveData.postValue(it)
            }
        }
        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.e("Home ViewModel", t.message.toString())
        }
    })

    fun observeSeachMealsLiveData(): LiveData<List<Meal>> = seachedMealsLiveData


    fun observerPopularItemsLiveData():LiveData<List<CategoryMeal>>{
        return  popularItemLivedata
    }

    fun observerGetCategoriesItemLiveData(): LiveData<List<Category>>{
        return categoryLiveData
    }

    fun observerFavouriteMealsLiveData():LiveData<List<Meal>>{
        return favouritesMealsLIveData
    }

    fun observerBottomSHeetMeal() : LiveData<Meal> = bottomSheetMealLiveData



}