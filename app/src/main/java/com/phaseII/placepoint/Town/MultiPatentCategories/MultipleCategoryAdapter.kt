package com.phaseII.placepoint.Town.MultiPatentCategories

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.multi_town_item.view.*

class MultipleCategoryAdapter (var context: Context,
                               var parentList: ArrayList<ModelCategoryData>) : RecyclerView.Adapter<MultipleCategoryAdapter.ViewHolder>() {
    var nameEvent=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.multi_town_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.title_text.text = parentList[position].name
        holder.itemView.next_btn.visibility=View.GONE
        holder.itemView.checkBox.isChecked = parentList[position].checked
        holder.itemView.setOnClickListener{
            if (parentList[position].checked){
                parentList[position].checked=false
                notifyDataSetChanged()
            }else{
                parentList[position].checked=true
                notifyDataSetChanged()
            }
        }
       holder.itemView.checkBox.setOnClickListener{
            if (parentList[position].checked){
                parentList[position].checked=false
                notifyDataSetChanged()
            }else{
                parentList[position].checked=true
                notifyDataSetChanged()
            }
        }


    }

    fun getSelectedCatetegoriesIds(): String {
        var id=""
        for (i in 0 until parentList.size){
            if (parentList[i].checked){
                if (id.isEmpty()){
                    id=parentList[i].id
                }else{
                    id=id+","+parentList[i].id
                }
            }
        }
        return id
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}