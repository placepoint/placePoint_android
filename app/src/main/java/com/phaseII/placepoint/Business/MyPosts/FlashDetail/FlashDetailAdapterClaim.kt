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
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.headerflashdetail.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FlashDetailAdapterClaim(private var context: Context, private val list: ArrayList<ModelFDetail>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {

            return VHItem(inflater.inflate(R.layout.flashdetail_item, parent, false));
        } else if (viewType == TYPE_HEADER) {
            return VHHeader(inflater.inflate(R.layout.headerflashdetail, parent, false));
        }
        return VHItem(inflater.inflate(R.layout.flashdetail_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is VHItem) {
            var dataItem = getItem(position);
            holder.checkBoxFlash.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                if (list[position].status == "0") {
                    list[position].checked = b
                }
            }
            holder.emailTextFlash.text = list[position].email
            holder.nameTextFlash.text = list[position].name
            holder.phoneTextFlash.text = list[position].phone_no
            holder.dateTextFlash.text = parseDateToddMMyyyy2(list[position].created_at)
            holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.white))
            if (list[position].status == "0") {
                holder.checkBoxFlash.visibility = View.VISIBLE
            } else {
                holder.checkBoxFlash.visibility = View.INVISIBLE
            }
            holder.phoneTextFlash.setOnClickListener {
                if (!list[position].phone_no.trim().isEmpty()) {
                    showDialog(list[position].phone_no)
                }
            }

        } else if (holder is VHHeader) {

            holder.itemView.header.text = list[position].name

        }
    }

    private fun getItem(position: Int): ModelFDetail {
        return list[position]

//        if (position != 0) {
//            list[position - 1]
//        } else {
//            list[position]
//        }
    }

    override fun getItemViewType(position: Int): Int {

        if (list[position].status == "header") {
            return TYPE_HEADER
        }

        return TYPE_ITEM
    }

    var TYPE_HEADER: Int = 0
    var TYPE_ITEM: Int = 1
    private var inflater = LayoutInflater.from(context)

    class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBoxFlash: CheckBox = itemView.findViewById(R.id.checkBoxFlash)
        var emailTextFlash: TextView = itemView.findViewById(R.id.emailTextFlash)
        var nameTextFlash: TextView = itemView.findViewById(R.id.nameTextFlash)
        var dateTextFlash: TextView = itemView.findViewById(R.id.dateTextFlash)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
        var phoneTextFlash: TextView = itemView.findViewById(R.id.phoneTextFlash)
        var cardLay: LinearLayout = itemView.findViewById(R.id.cardLay)
    }


    class VHHeader(itemView: View) : RecyclerView.ViewHolder(itemView)

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashDetailAdapter.ViewHolder {
//        if (viewType == TYPE_ITEM) {
//
//            return  VHItem( inflater.inflate(R.layout.flashdetail_item, parent, false));
//        } else if (viewType == TYPE_HEADER) {
//            //inflate your layout and pass it to view holder
//            return  VHHeader(null);
//        }
//        return ViewHolder(inflater.inflate(R.layout.flashdetail_item, parent, false))
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }

    //    @SuppressLint("SimpleDateFormat")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        try {
//            ///val modelData = list[position]
//            holder.checkBoxFlash.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
//                if (list[position].status == "0") {
//                    list[position].checked = b
//                }
//            }
//            holder.emailTextFlash.text = list[position].email
//            holder.nameTextFlash.text = list[position].name
//            holder.phoneTextFlash.text = list[position].phone_no
//            holder.dateTextFlash.text = parseDateToddMMyyyy2(list[position].created_at)
//            holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.white))
//
//            holder.checkBoxFlash.visibility = View.VISIBLE
//            holder.phoneTextFlash.setOnClickListener {
//                if (!list[position].phone_no.trim().isEmpty()) {
//                    showDialog(list[position].phone_no)
//                }
//            }
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//    }
//
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

    //
//
    fun getSectedIds(): String {

        var ids: String = ""
        for (i in 0 until list.size) {

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

    //
    fun getSelectedItems(newText: String): ArrayList<ModelFDetail> {
        var newList=ArrayList<ModelFDetail>()
        for (i in 0 until list.size){

                if (list[i].email.contains(newText)) {
                    newList.add(list[i])
                }

        }
        return newList
    }
//
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
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var checkBoxFlash: CheckBox = itemView.findViewById(R.id.checkBoxFlash)
//        var emailTextFlash: TextView = itemView.findViewById(R.id.emailTextFlash)
//        var nameTextFlash: TextView = itemView.findViewById(R.id.nameTextFlash)
//        var dateTextFlash: TextView = itemView.findViewById(R.id.dateTextFlash)
//        var cardView: CardView = itemView.findViewById(R.id.cardView)
//        var phoneTextFlash: TextView = itemView.findViewById(R.id.phoneTextFlash)
//        var cardLay: LinearLayout = itemView.findViewById(R.id.cardLay)
//    }

}

