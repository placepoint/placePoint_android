package com.phaseII.placepoint.Register

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.category_itemcheck.view.*


class MultipleCatagoryAdapter(private var context: Context, var list: List<ModelCategoryData>, var from: String) : RecyclerView.Adapter<MultipleCatagoryAdapter.ViewHolder>() {

    private var inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.category_itemcheck, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelData = list[position]
        holder.itemView.checkBox.isChecked = modelData.checked
        holder.itemView.catName.text = modelData.name
        holder.itemView.mainLay.setOnClickListener {

            if (holder.itemView.checkBox.isChecked) {
                holder.itemView.checkBox.isChecked = false
                list[position].checked = false
            } else {
                holder.itemView.checkBox.isChecked = true
                list[position].checked = true
            }
        }

        holder.itemView.checkBox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->

            holder.itemView.checkBox.isChecked = b
            list[position].checked = b
        }


    }

    fun getSelected(): String {
        var items: String = ""
        for (i in 0 until list.size) {
            if (list[i].checked) {
                items = if (items.isEmpty()) {
                    list[i].id
                } else {
                    items + "," + list[i].id
                }
            }

        }
        return items
    }

    fun getFirstSelectedName(): String {
        var savedCatergory=""
        for (i in 0 until list.size) {
            if (list[i].checked) {
                if (savedCatergory.isEmpty()){
                    savedCatergory=list[i].name
                }else{
                    savedCatergory=savedCatergory+", "+list[i].name
                }

            }
        }
        return savedCatergory
    }


    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)


}

