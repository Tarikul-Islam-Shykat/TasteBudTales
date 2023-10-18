package com.example.foodcreation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodcreation.Model.Category
import com.example.foodcreation.databinding.CategoryItemSampleBinding

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.CateroryViewHolder>() {

    private  var cateogires_List = ArrayList<Category>()
    var onItemClick: ((Category) -> Unit)? = null

    fun setCategoryList(cateogires_List: List<Category>){
        this.cateogires_List =  cateogires_List as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner  class CateroryViewHolder(val binding: CategoryItemSampleBinding ) : RecyclerView.ViewHolder(binding.root) // viewholder class


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateroryViewHolder {
        return  CateroryViewHolder(CategoryItemSampleBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CateroryViewHolder, position: Int) {
        Glide.with(holder.itemView).load(cateogires_List[position].strCategoryThumb).into(holder.binding.tvImageCateogry)
        holder.binding.tvtCategoryName.text = cateogires_List[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(cateogires_List[position])
        }
    }

    override fun getItemCount(): Int {
        return cateogires_List.size
    }
}