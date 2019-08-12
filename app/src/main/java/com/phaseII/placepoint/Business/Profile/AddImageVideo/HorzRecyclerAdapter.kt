package com.phaseII.placepoint.Business.Profile.AddImageVideo

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.horz_recycler_item.view.*
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.graphics.BitmapFactory
import android.support.v4.content.CursorLoader
import com.phaseII.placepoint.R
import android.media.ThumbnailUtils
import android.media.ExifInterface




class HorzRecyclerAdapter(var context: Context, val list: ArrayList<ModelImageVideo>) :
        RecyclerView.Adapter<HorzRecyclerAdapter.ViewHolder>() {
    var mCallback: OnHeadlineSelectedListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        try {
            mCallback = context as OnHeadlineSelectedListener
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ViewHolder(inflater.inflate(R.layout.horz_recycler_item, parent, false))
    }

    interface OnHeadlineSelectedListener {
        fun preloadListUpdate(position: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (context != null) {
            try {
                if (list[position].type.equals("video")) {
                    try {
                        var bitmap2:Bitmap?=null
                        try {
                            bitmap2 = getVideoThumbnail(context, list[position].uri)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                      //  holder.itemView.videoImage.visibility=View.VISIBLE
                        if (bitmap2==null){
                            try {
                                if (list[position].oldUri.isEmpty()){
                                    Glide.with(context)
                                            .load(R.mipmap.video_icon)
                                            .apply(RequestOptions()
                                                    .centerCrop()
                                                    .placeholder(R.mipmap.placeholder))
                                            .into(holder.itemView.mImage)
                                    bitmap2 = getThumbnailBitmap( list[position].uri,context)
                                }else {
                                    Glide.with(context)
                                            .load(R.mipmap.video_icon)
                                            .apply(RequestOptions()
                                                    .centerCrop()
                                                    .placeholder(R.mipmap.placeholder))
                                            .into(holder.itemView.mImage)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }else{
                            //holder.itemView.mImage.setImageBitmap(bitmap2)
                            Glide.with(context)
                                    .load(R.mipmap.video_icon)
                                    .apply(RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.mipmap.placeholder))
                                    .into(holder.itemView.mImage)

                        }
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }

                } else if (list[position].type.contains("pdf")||list[position].oldUri.contains("pdf")) {
                    holder.itemView.mImage.setImageResource(R.drawable.ic_pdf)
                    holder.itemView.videoImage.visibility=View.GONE
                } else if (list[position].type.equals("old")) {
                    if (list[position].oldUri.contains("mp4")||list[position].oldUri.contains(".mov")||list[position].oldUri.contains("mp3")||list[position].oldUri.contains("wma")){
                       // holder.itemView.videoImage.visibility=View.VISIBLE
                        try {
                            Glide.with(context)
                                    .load(R.mipmap.video_icon)
                                    .apply(RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.mipmap.placeholder))
                                    .into(holder.itemView.mImage)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                       // holder.itemView.videoImage.visibility=View.GONE
                        try {
                            Glide.with(context)
                                    .load(list[position].oldUri)
                                    .apply(RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.mipmap.placeholder))
                                    .into(holder.itemView.mImage)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                } else {
                    if (list[position].uri.toString().contains("file://")){
                        try {
                            Glide.with(context)
                                    .load(list[position].uri)
                                    .apply(RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.mipmap.placeholder))
                                    .into(holder.itemView.mImage)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                        holder.itemView.mImage.setImageURI(list[position].uri)
                    }
                    holder.itemView.videoImage.visibility=View.GONE

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }



        holder.itemView.mCancel.setOnClickListener {
            //            if (type == "old") {
//                try {
//                    preLoadImages.removeAt(position)
//                    type == "old"
//                    notifyItemRemoved(position)
//                    notifyItemRangeChanged(position, preLoadImages.size)
            //                list.remove(list[position])
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            } else {
//                try {
//                    croppedImages.remove(list[position])
//                    type == "new"
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
            list.remove(list[position])
            notifyDataSetChanged()
//            }


        }
    }


    fun getThumbnailBitmap(uri: Uri, context: Context): Bitmap {
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        // This method was deprecated in API level 11
        // Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        val cursorLoader = CursorLoader(context, uri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        cursor.moveToFirst()
        val imageId = cursor.getLong(column_index)
        //cursor.close();

        return MediaStore.Images.Thumbnails.getThumbnail(
                context.getContentResolver(), imageId,
                MediaStore.Images.Thumbnails.MINI_KIND,
                null as BitmapFactory.Options?)
    }

    @Throws(IllegalArgumentException::class, SecurityException::class)
    private fun getVideoThumbnail(context: Context, uri: Uri): Bitmap {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        return retriever.frameAtTime
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface UpdateImageList {
        fun updateHorizontalImageList(position: Int)
    }
}
