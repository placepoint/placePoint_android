package com.phaseII.placepoint.MultichoiceCategories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.choice_radio_item1.view.*
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.DetailFragment.setTitle



class MultiChoiceRadioAdapter(var context: Context,
                              var childList: ArrayList<ModelCategoryData>, var allCatagories: List<ModelCategoryData>) : RecyclerView.Adapter<MultiChoiceRadioAdapter.ViewHolder>() {
    val updateMain = context as notifyUpdate
    var choosedOne = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.choice_radio_item1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return childList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.serviceText.text = childList[position].name

        Glide.with(context)
                .load(childList[position].image_url)
                .apply(RequestOptions().override(60, 60))
                .into(holder.itemView.cat_image)

        var userType=Constants.getPrefs(context)!!.getString(Constants.USERTYPE,"")
        for (i in 0 until allCatagories.size) {
//            if (userType=="3") {
//                if (allCatagories[i].checked) {
//                    choosedOne = 1
//                }
//            }
            if (allCatagories[i].id == childList[position].id) {
                holder.itemView.radioImage.isChecked = allCatagories[i].checked

            }

        }

        holder.itemView.radioImage.setOnClickListener(View.OnClickListener {
            if (childList[position].name == "All") {
                if (childList[position].checked) {
                    for (i in 0 until allCatagories.size) {
                        for (j in 0 until childList.size) {
                            if (allCatagories[i].id == childList[j].id) {
                                allCatagories[i].checked = false
                                holder.itemView.radioImage.isChecked = allCatagories[i].checked
                                updateMain.notifyMainAdapter()
                            }
                        }
                    }
                } else {
                    for (i in 0 until allCatagories.size) {
                        for (j in 0 until childList.size) {
                            if (allCatagories[i].id == childList[j].id) {
                                allCatagories[i].checked = true
                                holder.itemView.radioImage.isChecked = allCatagories[i].checked
                                updateMain.notifyMainAdapter()
                            }
                        }
                    }
                }

            } else {
//                for(k in 0 until allCatagories.size){
//                    var userType= Constants.getPrefs(context)!!.getString(Constants.USERTYPE, "")
//                    if (userType=="3"){
//
//                    }
//                }
                outer@for (i in 0 until allCatagories.size) {

                    if (allCatagories[i].id == childList[position].id) {
                        if (childList[position].parent_category != "0") {
                            if (allCatagories[i].checked) {
                                choosedOne = 0
                                allCatagories[i].checked = false
                                updateMain.notifyMainAdapter()
                                holder.itemView.radioImage.isChecked = allCatagories[i].checked
                            } else {
                                val userType = Constants.getPrefs(context)!!.getString(Constants.USERTYPE, "")
//                                if (userType == "3") {
//                                    if (choosedOne == 1) {
//                                        Constants.subscriptionDialog(context)
//                                        holder.itemView.radioImage.isChecked=false
//                                        return@OnClickListener
//                                        break@outer
//                                    }
//                                }
                                choosedOne = 1
                                allCatagories[i].checked = true
                                updateMain.notifyMainAdapter()
                                holder.itemView.radioImage.isChecked = allCatagories[i].checked
                            }
                        }
                    }


                }
            }


        })
        holder.itemView.setOnClickListener {
            for (i in 0 until allCatagories.size) {

                if (allCatagories[i].id == childList[position].id) {
                    if (childList[position].parent_category != "0") {
                        if (allCatagories[i].checked) {
                            choosedOne = 0
                            allCatagories[i].checked = false
                            updateMain.notifyMainAdapter()
                            holder.itemView.radioImage.isChecked = allCatagories[i].checked
                        } else {
                            val userType = Constants.getPrefs(context)!!.getString(Constants.USERTYPE, "")
//                            if (userType == "3") {
//                                if (choosedOne == 1) {
//                                    Constants.subscriptionDialog(context)
//                                    return@setOnClickListener
//                                }
//                            }
                            choosedOne = 1
                            allCatagories[i].checked = true
                            updateMain.notifyMainAdapter()
            allCatagories[position].checked
                          holder.itemView.radioImage.isChecked = allCatagories[position].checked
                        }
                    }
                }

            }
        }
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface notifyUpdate {
        fun notifyMainAdapter()
    }


}