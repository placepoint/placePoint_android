package com.phaseII.placepoint.Categories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.phaseII.placepoint.AlphabetiComparator
import com.phaseII.placepoint.BusEvents.ShowHomeButton
import com.phaseII.placepoint.BusEvents.ShowMainLayout
import com.phaseII.placepoint.BusEvents.SwitchToMainCategory
import com.phaseII.placepoint.Categories.SubCategories.SubCategoriesActivity
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.inner_category_item.view.*
import java.util.*

class CategoriesAdapter(var context: Context, var main: ArrayList<ModelCategoryData>,
                        var parentList: ArrayList<ModelCategoryData>,
                        var froms: String,
                        var sentTownId: String,
                        var townName: String) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.inner_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.catName.text = parentList[position].name
        Picasso.with(context)
                .load(parentList[position].image_url)
                .centerCrop()
                .placeholder(R.mipmap.placeholder)
                .resize(300, 200)
                .into(holder.itemView.catImage)

        holder.itemView.setOnClickListener {

           var  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("town",Constants.getPrefs(context)!!.getString(Constants.TOWN_NAME,""))
            bundle.putString("Category",parentList[position].name)
            mFirebaseAnalytics.logEvent("Selected_Category", bundle)
            if (froms == "cat") {
                val intent = Intent(context, SubCategoriesActivity::class.java)
                intent.putExtra("catId", parentList[position].id)
                intent.putExtra("name", parentList[position].name)
                intent.putExtra("List", main)
                intent.putExtra("froms", froms)
                intent.putExtra("townId", sentTownId)
                intent.putExtra("townName", townName)
                intent.putExtra("model", parentList[position].image_url)
                context.startActivity(intent)
            } else {

                var inSubCategory = Constants.getPrefs(context)!!.getString("subcategory", "0");
                if (inSubCategory == "0") {
                    val stringBuilder = StringBuilder("")
                    var prefix = ""
                    for (i in 0 until parentList.size) {
                        if (i != 0) {
                            stringBuilder.append(prefix)
                            prefix = ","
                            stringBuilder.append(parentList[i].id)
                        }
                    }
                    val maincat = String(stringBuilder)
                    val name = "All " + Constants.getPrefs(context)!!.getString("MainCatName", "0")

                    if (position == 0 && parentList[position].name != "Taxis") {
                        Constants.getPrefs(context)!!.edit().putString("MainCatName", name).apply()
                    }else{
                        Constants.getPrefs(context)!!.edit().putString("MainCatName", parentList[position].name).apply()

                    }
                    Constants.getPrefs(context)!!.edit().putString("subcategory", "1").apply()
                    Constants.getPrefs(context)!!.edit().putString("MainCatName", parentList[position].name).apply()
                    Constants.getPrefs(context)!!.edit().putString("position", position.toString()).apply()
                    Constants.getBus().post(ShowHomeButton("value"))
                    val childList1: ArrayList<ModelCategoryData> = arrayListOf()
                    for (i in 0 until main.size) {
                        if (parentList[position].id == main[i].parent_category) {
                            childList1.add(main[i])
                        }
                    }
                    if (childList1.size > 0) {
                        // noData.visibility = View.GONE
                        Collections.sort(childList1, AlphabetiComparator())
                        if (parentList[position].name != "Taxi") {
                            var model = ModelCategoryData()
                            model.name = "All"
                            model.image_url = parentList[position].image_url
                            childList1.add(0, model)
                        }
                        parentList = childList1
                        notifyDataSetChanged()
                    }

                } else {
                     Constants.getPrefs(context)!!.edit().putString("subcategory", "2").apply()
                    val stringBuilder = StringBuilder("")
                    var prefix = ""
                    for (i in 0 until parentList.size) {
                        if (i != 0) {
                            stringBuilder.append(prefix)
                            prefix = ","
                            stringBuilder.append(parentList[i].id)
                        }
                    }
                    val maincat = String(stringBuilder)
                    val name = "All " + Constants.getPrefs(context)!!.getString("MainCatName", "0")

                    if (position == 0 && parentList[position].name != "Taxis") {
                        Constants.getPrefs(context)!!.edit().putString("firstTime", "sub").apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_NAMEO, name).apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDS, maincat).apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_ID, sentTownId).apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_NAME, townName).apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDSUB, maincat).apply()

                        // (context as Activity).finish()
                    } else {
                        Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDS, parentList[position].id).apply()
                        Constants.getPrefs(context)!!.edit().putString("firstTime", "sub").apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_NAMEO, parentList[position].name).apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDSUB, parentList[position].id).apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_ID, sentTownId).apply()
                        Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_NAME, townName).apply()
                        //   (context as Activity).finish()


                    }

                    Constants.getPrefs(context)!!.edit().putString("showHomeBackButton", "yes").apply()
                    Constants.getBus().post(SwitchToMainCategory("value"))
                    val value=Constants.getPrefs(context)!!.getString("cc","0").toInt()
                    val dd=value+1
                    Constants.getPrefs(context)!!.edit().putString("cc", dd.toString()).apply()

                }

            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}