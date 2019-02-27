package com.phaseII.placepoint.Cropper

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.cropper_recycler_item.view.*


class ImageAdapter(private val context: Context, private val items: ArrayList<Uri>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var mCallback: sendDataListener? = null

    interface sendDataListener {
        fun onDataRecieve(uri: Uri, position: Int, items: ArrayList<Uri>)
        fun onDataDelete(uri: Uri, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        try {
            mCallback = context as sendDataListener
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ViewHolder(inflater.inflate(R.layout.cropper_recycler_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
                .load(items[position])
                .apply(RequestOptions()
                        .placeholder(R.mipmap.placeholder))
                .into(holder.itemView.image)
        holder.itemView.cancel.setOnClickListener {
            items.remove(items[position])
            notifyDataSetChanged()
            if (items.size > 0) {
                mCallback!!.onDataDelete(items[0],position)
            }
        }


        holder.itemView.image.setOnClickListener {
            mCallback!!.onDataRecieve(items[position], position, items)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
