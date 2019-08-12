package com.phaseII.placepoint.AboutBusiness

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.bus_horz_recycler_item.view.*
import java.util.ArrayList
import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.ImageVideoPDF.ImagePDFVideoViewActivity


class BusinessHorzRecyclerAdapter(val context: Context, val list: ArrayList<String>) :
        RecyclerView.Adapter<BusinessHorzRecyclerAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(inflater.inflate(R.layout.bus_horz_recycler_item, parent, false))
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (context != null) {
            try {
                if (list[position].contains("pdf")){
                    holder.itemView.videoImageh.visibility=View.GONE
                    Glide.with(context)
                            .load(R.drawable.ic_pdf)
                            .apply(RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.mipmap.ic_launcher))
                            .into(holder.itemView.mImage)

                }else {
                    if (list[position].contains("mp4")||list[position].contains("mp3")
                            ||list[position].contains("wma") ||list[position].contains(".mov")){
                        Glide.with(context)
                                .load(R.mipmap.video_icon)
                                .apply(RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.mipmap.ic_launcher))
                                .into(holder.itemView.mImage)
                    }else{
                        Glide.with(context)
                                .load(list[position])
                                .apply(RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.mipmap.ic_launcher))
                                .into(holder.itemView.mImage)
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        holder.itemView.setOnClickListener {
          context.startActivity(Intent(context, ImagePDFVideoViewActivity::class.java).putExtra("array",list))

//            val intent=Intent(context,BusinessViewPager::class.java)
//            val args = Bundle()
//            args.putStringArrayList("list", list )
//            intent.putExtra("BUN", args)
//            intent.putExtra("Position", position)
//            context.startActivity(intent)
//            if (list[position].contains("pdf")){
//                val target = Intent(Intent.ACTION_VIEW)
//                target.setDataAndType(Uri.parse(list[position]), "application/pdf")
//                target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//
//                val intent = Intent.createChooser(target, "Open File")
//                try {
//                    context.startActivity(intent)
//                } catch (e: ActivityNotFoundException) {
//                    // Instruct the user to install a PDF reader here, or something
//                }
//
//            }
//            if (list[position].contains("mp4")||list[position].contains("mp3")||list[position].contains("wma")){
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.setDataAndType(Uri.parse(list[position]), "video/*")
//                context.startActivity(intent)
//            }
//         if (list[position].contains("png")||list[position].contains("jpg")||list[position].contains("jpeg'")){
//             //context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(list[position])))
//           var newList= ArrayList<String>()
//             newList.add(list[position])
//            val intent=Intent(context,BusinessViewPager::class.java)
//            val args = Bundle()
//            args.putStringArrayList("list", newList )
//            intent.putExtra("BUN", args)
//            intent.putExtra("Position", position)
//            context.startActivity(intent)
//            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
