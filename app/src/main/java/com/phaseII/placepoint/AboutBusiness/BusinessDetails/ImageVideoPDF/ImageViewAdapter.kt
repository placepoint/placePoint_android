package com.phaseII.placepoint.AboutBusiness.BusinessDetails.ImageVideoPDF

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.AboutBusiness.BusinessViewPager
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.image_view_adapter.view.*

class ImageViewAdapter(var context: Context, var list: ArrayList<String>) :
        RecyclerView.Adapter<ImageViewAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(inflater.inflate(R.layout.image_view_adapter, parent, false))
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
                .load(list[position])
                .apply(RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher))
                .into(holder.itemView.image)
        holder.itemView.setOnClickListener {
            if (list[position].contains("mp4")||list[position].contains(".mov")||list[position].contains("mp3")||list[position].contains("wma")){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(list[position]), "video/*")
                context.startActivity(intent)
            }else {
                val intent = Intent(context, BusinessViewPager::class.java)
                val args = Bundle()
                args.putStringArrayList("list", list)
                intent.putExtra("BUN", args)
                intent.putExtra("Position", position)
                context.startActivity(intent)
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}