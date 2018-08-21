package com.phaseII.placepoint.Categories

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phaseII.placepoint.Categories.SubCategories.SubCategoriesActivity
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.inner_category_item.view.*

class CategoriesAdapter(var context: Context, var main: ArrayList<ModelCategoryData>,
                        var parentList: ArrayList<ModelCategoryData>,
                        var froms: String,
                        var sentTownId: String,
                        var townName: String): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View=LayoutInflater.from(context).inflate(R.layout.inner_category_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  parentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.catName.text=parentList[position].name
        Picasso.with(context)
                .load(parentList[position].image_url)
                .centerCrop()
                .placeholder(R.mipmap.placeholder)
                .resize(300,200)
                .into(holder.itemView.catImage)
//        Glide.with(context)
//                .load(parentList[position].image_url)
//
//                .into(holder.itemView.catImage)
        holder.itemView.setOnClickListener{

            val intent= Intent(context,SubCategoriesActivity::class.java)
            intent.putExtra("catId",parentList[position].id)
            intent.putExtra("name",parentList[position].name)
            intent.putExtra("List",main)
            intent.putExtra("froms",froms)
            intent.putExtra("townId",sentTownId)
            intent.putExtra("townName",townName)
            intent.putExtra("model",parentList[position].image_url)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view)


}