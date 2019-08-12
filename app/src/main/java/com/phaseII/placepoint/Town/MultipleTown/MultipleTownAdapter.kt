package com.phaseII.placepoint.Town.MultipleTown

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.phaseII.placepoint.R

class MultipleTownAdapter(val context: Context, var list: List<ModelMultiTown>
) : RecyclerView.Adapter<MultipleTownAdapter.ViewHolder>() {
    val inflator = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(inflator.inflate(R.layout.multi_town_item, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.title_text.text = list[p1].townname
      p0.checkBox.isChecked = list[p1].checked
      p0.next_btn.visibility=View.GONE
        p0.itemView.setOnClickListener{
            if (list[p1].checked){
                list[p1].checked=false
                notifyDataSetChanged()
            }else{
                list[p1].checked=true
                notifyDataSetChanged()
            }
        }
    p0.checkBox.setOnClickListener{
        if (list[p1].checked){
           // p0.checkBox.isChecked=false
            list[p1].checked=false
            notifyDataSetChanged()
        }else{
            p0.checkBox.isChecked=true
            list[p1].checked=true
            notifyDataSetChanged()
        }
    }
    }

    fun getSelectedTownIds(): String {
        var id=""
        for (i in 0 until list.size){
            if (list[i].checked){
                if (id.isEmpty()){
                    id=list[i].id
                }else{
                    id=id+","+list[i].id
                }
            }
        }
        return id
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var title_text=itemView.findViewById(R.id.title_text) as TextView
        var checkBox=itemView.findViewById(R.id.checkBox) as CheckBox
        var next_btn=itemView.findViewById(R.id.next_btn) as ImageView
    }
}