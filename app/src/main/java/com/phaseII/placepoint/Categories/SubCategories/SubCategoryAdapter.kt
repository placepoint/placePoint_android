package com.phaseII.placepoint.Categories.SubCategories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.inner_category_item.view.*

class SubCategoryAdapter(var context: Context, var main: ArrayList<ModelCategoryData>,
                         var froms: String,
                         var nameMain: String,
                         var townName: String,
                         var savetownId: String
) : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.inner_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return main.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.catName.text = main[position].name

        Picasso.with(context)
                .load(main[position].image_url)
                .centerCrop()
                .placeholder(R.mipmap.placeholder)
                .resize(300, 200)
                .into(holder.itemView.catImage)
        holder.itemView.setOnClickListener {
            val stringBuilder = StringBuilder("")
            var prefix = ""
            for (i in 0 until main.size) {
                if (i != 0) {
                    stringBuilder.append(prefix)
                    prefix = ","
                    stringBuilder.append(main[i].id)
                }
            }
            val maincat = String(stringBuilder)
            val name = "All $nameMain"
            if (froms == "cat") {


                if (position == 0 && main[position].name != "Taxis") {

                    Constants.getPrefs(context)!!.edit().putString("firstTime", "sub").apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDSUB, maincat).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDS, maincat).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_NAMEO, name).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_ID, savetownId).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_NAME, townName).apply()
                } else {
                    Constants.getPrefs(context)!!.edit().putString("firstTime", "sub").apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDS, maincat).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDSUB, main[position].id).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_NAMEO, main[position].name).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_ID, savetownId).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_NAME, townName).apply()

                }
                val fromWhere=Constants.getPrefs(context)!!.getString("comingFrom", "")
                if (fromWhere=="more") {
                    Constants.getPrefs(context)!!.edit().putString("showHomeBackButton", "yes").apply()
                }
                val showBack = Constants.getPrefs(context!!)!!.getString("showHomeBackButton", "no")
                if (showBack == "no") {
                    val intent = Intent(context, DashBoardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    (context as Activity).finish()
                } else {
                    val intent = Intent(context, DashBoardActivity::class.java)
                    context.startActivity(intent)
                }
            } else {
                if (position == 0 && main[position].name != "Taxis") {
                    Constants.getPrefs(context)!!.edit().putString("firstTime", "sub").apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_NAMEO, name).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDS, maincat).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_ID, savetownId).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_NAME, townName).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDSUB, maincat).apply()

                    val showback = Constants.getPrefs(context!!)!!.getString("showHomeBackButton", "no")
                    if (showback == "no") {
                        (context as Activity).finish()
                    }

                } else {
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDS, maincat).apply()
                    Constants.getPrefs(context)!!.edit().putString("firstTime", "sub").apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_NAMEO, main[position].name).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.CATEGORY_IDSUB, main[position].id).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_ID, savetownId).apply()
                    Constants.getPrefs(context)!!.edit().putString(Constants.TOWN_NAME, townName).apply()
                    val showback = Constants.getPrefs(context!!)!!.getString("showHomeBackButton", "no")
                    if (showback == "no") {
                        (context as Activity).finish()
                    }

                }
            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}