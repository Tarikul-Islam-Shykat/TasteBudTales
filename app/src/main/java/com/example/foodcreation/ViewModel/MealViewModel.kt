package com.example.foodcreation.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcreation.Model.Meal
import com.example.foodcreation.Model.MealList
import com.example.foodcreation.api.RetrofitInstance
import com.example.foodcreation.db.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: MealDatabase): ViewModel() {

    private  var mealLiveData = MutableLiveData<Meal>()

    fun getMealDetails(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!= null){
                    mealLiveData.value = response.body()!!.meals[0]
                }
                else
                    return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Meal activty",t.message.toString())
            }
        })
    }

    fun observerMealDetailsLiveData():LiveData<Meal>{
        return mealLiveData
    }

    // data base related works
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

}