package com.phaseII.placepoint.Business.MyPosts.FlashDetail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FlashDetailAdapter(private var context: Context, private val list: ArrayList<ModelFDetail>) : RecyclerView.Adapter<FlashDetailAdapter.ViewHolder>() {

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
        holder.phoneTextFlash.text = modelData.phone_no
        holder.nameTextFlash.text = modelData.name
        holder.dateTextFlash.text = parseDateToddMMyyyy2(modelData.created_at)
        holder.phoneTextFlash.setOnClickListener{
            if (!modelData.phone_no.trim().isEmpty()){
                showDialog(modelData.phone_no)
            }
        }
        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.white))

            holder.checkBoxFlash.visibility=View.INVISIBLE

    }

    private fun showDialog(phoneNo: String) {

        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle("Make a call")
        dialog.setMessage(phoneNo)
        dialog.setPositiveButton("Call", DialogInterface.OnClickListener { dialog, id ->
            //            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.data = Uri.parse(phoneNo)
//            startActivity(callIntent)
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNo")
            if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return@OnClickListener
            }

            context.startActivity(callIntent)
        })
                .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                    //Action for "Cancel".
                })

        val alert = dialog.create()
        alert.show()
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
        var phoneTextFlash: TextView = itemView.findViewById(R.id.phoneTextFlash)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
        var cardLay: LinearLayout = itemView.findViewById(R.id.cardLay)
    }

}

