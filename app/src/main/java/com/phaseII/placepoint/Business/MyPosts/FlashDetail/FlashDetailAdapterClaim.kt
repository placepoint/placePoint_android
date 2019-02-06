package com.phaseII.placepoint.Business.MyPosts.FlashDetail

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.phaseII.placepoint.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FlashDetailAdapterClaim(private var context: Context, private val list: ArrayList<ModelFDetail>) : RecyclerView.Adapter<FlashDetailAdapterClaim.ViewHolder>() {

    private var inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.flashdetail_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelData = list[position]
        holder.checkBoxFlash.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (modelData.status=="0"){
                modelData.checked = b
            }
        }
        holder.emailTextFlash.text = modelData.email
        holder.nameTextFlash.text = modelData.name
        holder.dateTextFlash.text = parseDateToddMMyyyy2(modelData.created_at)
        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.white))

            holder.checkBoxFlash.visibility= View.VISIBLE

    }

    fun getSectedIds(): String {

        var ids:String=""
        for(i in 0 until list.size){

                if (list[i].checked) {
                    if (!ids.isEmpty()) {
                        ids = ids + "," + list[i].id
                    } else {
                        ids = list[i].id
                    }

            }
        }
        return ids
    }

    fun getSelectedItems(newText: String): ArrayList<ModelFDetail> {
        var newList=ArrayList<ModelFDetail>()
        for (i in 0 until list.size){

                if (list[i].email.contains(newText)) {
                    newList.add(list[i])
                }

        }
        return newList
    }

    fun parseDateToddMMyyyy2(time: String): String? {
        val inputPattern = "yyyy-MM-dd hh:mm:ss"
        val outputPattern = "dd MM yyyy"
        //val outputPattern = "hh:mm a"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
//        val sdf = SimpleDateFormat("_HHmmss")
//        val currentDateandTime = sdf.format(Date())
        return str
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBoxFlash: CheckBox = itemView.findViewById(R.id.checkBoxFlash)
        var emailTextFlash: TextView = itemView.findViewById(R.id.emailTextFlash)
        var nameTextFlash: TextView = itemView.findViewById(R.id.nameTextFlash)
        var dateTextFlash: TextView = itemView.findViewById(R.id.dateTextFlash)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
        var cardLay: LinearLayout = itemView.findViewById(R.id.cardLay)
    }

}

