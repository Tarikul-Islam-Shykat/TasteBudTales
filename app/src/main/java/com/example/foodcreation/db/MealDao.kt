package com.example.foodcreation.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodcreation.Model.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal:Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getALlMeals(): LiveData<List<Meal>>

}