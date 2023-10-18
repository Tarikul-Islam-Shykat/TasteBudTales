package com.example.foodcreation.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.foodcreation.Model.Meal
import com.example.foodcreation.R
import com.example.foodcreation.ViewModel.MealViewModel
import com.example.foodcreation.ViewModel.MealViewModelFacotry
import com.example.foodcreation.databinding.ActivityMealBinding
import com.example.foodcreation.db.MealDatabase

class MealActivity : AppCompatActivity() {

    private  lateinit var  binding_meal : ActivityMealBinding
    // variable
    private  lateinit var mealName : String
    private  lateinit var mealThumb : String
    private  lateinit var mealID : String
    private  lateinit var youtubeLink:  String
    // MVVM
    private  lateinit var mealMVVM: MealViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_meal = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding_meal.root)

        //mealMVVM = ViewModelProviders.of(this)[MealViewModel::class.java]
        val mealDatabase = MealDatabase.geInstance(this)
        val viewModelFacotry = MealViewModelFacotry(mealDatabase)
        mealMVVM = ViewModelProvider(this, viewModelFacotry).get(MealViewModel::class.java)


        getInformation() // get information from  previous activity
        setInformation()  // set the info in this activity
        getLoadingCase() // show the progress bar
        mealMVVM.getMealDetails(mealID) /// get data from api
        observerMealDetailLiveData() // see the changes in the data
        onYoutubeImageClick() // if the youtube button is clicked
        onFavouriteButtonClick() // if the favourite button is clicked

    }

    private fun onFavouriteButtonClick() {
        binding_meal.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMVVM.insertMeal(it)
                Toast.makeText(this,"Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding_meal.btnYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal? = null // this for saving in the room database
    private fun observerMealDetailLiveData() {
        mealMVVM.observerMealDetailsLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponseCase() // got the data, remove the progress bar
                val meal = t
                mealToSave = meal // this for saving in the room database
                binding_meal.tvCategory.text = "Category : ${meal!!.strCategory}"
                binding_meal.tvArea.text = "Area: ${meal.strArea}"
                binding_meal.tvDescritption.text = meal.strInstructions
                youtubeLink  = meal.strYoutube!!
            }

        })
    }

    private fun setInformation() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding_meal.imgMealDetail)

        binding_meal.collapsingToolbar.title = mealName
        // setting color
        binding_meal.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding_meal.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

    }

    private fun getInformation() {
       val intent_ = intent
        mealID = intent_.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent_.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent_.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private  fun getLoadingCase(){
        binding_meal.progressBar.visibility = View.VISIBLE
        binding_meal.btnAddToFav.visibility = View.INVISIBLE
        binding_meal.tvInstruction.visibility = View.INVISIBLE
        binding_meal.tvCategory.visibility = View.INVISIBLE
        binding_meal.tvDescritption.visibility = View.INVISIBLE
        binding_meal.btnYoutube.visibility = View.INVISIBLE
    }

    private  fun onResponseCase(){
        binding_meal.progressBar.visibility = View.INVISIBLE
        binding_meal.btnAddToFav.visibility = View.VISIBLE
        binding_meal.tvInstruction.visibility = View.VISIBLE
        binding_meal.tvCategory.visibility = View.VISIBLE
        binding_meal.tvDescritption.visibility = View.VISIBLE
        binding_meal.btnYoutube.visibility = View.VISIBLE
    }




}