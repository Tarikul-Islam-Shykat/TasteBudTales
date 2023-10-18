package com.example.foodcreation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodcreation.db.MealDatabase

class MealViewModelFacotry ( private val mealDatabase: MealDatabase) :  ViewModelProvider.Factory
{
    override fun <T:ViewModel> create(modelClass:Class<T>):T{
        return MealViewModel(mealDatabase) as T
    }

}