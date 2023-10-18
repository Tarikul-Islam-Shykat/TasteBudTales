package com.example.foodcreation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.foodcreation.ViewModel.HomeFragmentViewModel
import com.example.foodcreation.ViewModel.HomeViewModelFacotry
import com.example.foodcreation.db.MealDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel:HomeFragmentViewModel by lazy { // now this will be able to get accesed by all the class and fragments.
        val mealDatabase = MealDatabase.geInstance(this)
        val homeViewModelFacotry = HomeViewModelFacotry(mealDatabase)
        ViewModelProvider(this,homeViewModelFacotry)[HomeFragmentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation  = findViewById<BottomNavigationView>(R.id.btn_nav)
        val navController = Navigation.findNavController(this, R.id.frag_host)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}
