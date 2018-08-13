package com.phaseII.placepoint.MultichoiceCategories

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.category_item.view.*


class CategoryAdapter(val context: FragmentActivity?, val list: List<ModelCategoryData>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.category_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list.get(position)
        holder.itemView.textTitle.text = model.name

        if (context != null) {
            Glide.with(context).load(model.image_url)
                    .apply(RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.placeholder))
                    .into(holder.itemView.profile_image)
        }
        holder.itemView.setOnClickListener {
            Constants.getPrefs(context!!)?.edit()?.putString(Constants.OPEN_CAT, "no")?.apply()
            Constants.getPrefs(context)?.edit()?.putString(Constants.CATEGORY, model.id)?.apply()
            Constants.getPrefs(context)?.edit()?.putString(Constants.CATEGORY_IDS, model.id)?.apply()
            Constants.getPrefs(context)?.edit()?.putString(Constants.CATEGORY_NAME, model.name)?.apply()
            val intent = Intent(context, DashBoardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
            context.finish()
        }

//        holder.itemView.textDesc.text = model.description
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
