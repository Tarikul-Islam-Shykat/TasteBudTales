package com.example.foodcreation.db

import android.content.Context
import androidx.room.*
import com.example.foodcreation.Model.Meal
import com.example.foodcreation.api.MealTypeConverter

@Database(
    entities = [Meal::class],
    version = 1
)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase: RoomDatabase() {

    abstract  fun mealDao(): MealDao

    companion object{
        @Volatile // any change in this thread will be visible to other threads
        var INSTANCE: MealDatabase? = null

        @Synchronized // to run in only one thread
        fun geInstance(context: Context): MealDatabase{

            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context, MealDatabase::class.java, "meal.db"
                ).fallbackToDestructiveMigration() // if there sis version change what will happen -> in this case, keep the database and update it.
                    .build()
            }

            return INSTANCE as MealDatabase
        }

    }


}