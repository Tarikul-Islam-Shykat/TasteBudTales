package com.example.foodcreation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodcreation.Model.CategoryMeal
import com.example.foodcreation.databinding.PopularItemBinding

class mostPopularItemAdapter(): RecyclerView.Adapter<mostPopularItemAdapter.popularIteViewHolder>(){

    private var mealList_ = ArrayList<CategoryMeal>()
    lateinit var onItemClickListener: ((CategoryMeal) -> Unit)// nothing will return

    lateinit var onLongItemClickListener:((CategoryMeal) ->Unit )  // for long press


    fun setMeals(mealList: ArrayList<CategoryMeal>)
    {
        this.mealList_ = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularIteViewHolder {
        return  popularIteViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: popularIteViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList_[position].strMealThumb)
            .into(holder.binding.imgPopularItems)

        holder.itemView.setOnClickListener{
            onItemClickListener.invoke(mealList_[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClickListener?.invoke(mealList_[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealList_.size
    }

    class popularIteViewHolder(val binding: PopularItemBinding):RecyclerView.ViewHolder(binding.root) // here PopularItemBinding is the xml file

}