package com.phaseII.placepoint.Business.AddPost

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.R

class ScheduleListOptionAdapter(var context:Context,var list:ArrayList<ModelSelectOption>): RecyclerView.Adapter<ScheduleListOptionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.sitem,parent,false))

    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text=list[position].name

            holder.radioButton.isChecked = list[position].checked

        holder.itemView.setOnClickListener{
            if (list[position].checked){
                for (i in 0 until list.size){
                    list[i].checked=false
                    holder.radioButton.isChecked=false
                }
                notifyDataSetChanged()
            }else{

                for (i in 0 until list.size){
                    if (i!=position){
                        list[i].checked=false
                        holder.radioButton.isChecked=false
                    }
                }
                notifyDataSetChanged()
                list[position].checked=true
                holder.radioButton.isChecked=true
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val radioButton:RadioButton=itemView.findViewById(R.id.radioButton)
        val name:TextView=itemView.findViewById(R.id.name)
    }
}