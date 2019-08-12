package com.phaseII.placepoint.AboutBusiness.BusinessDetails.ImageVideoPDF

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.AboutBusiness.BusinessViewPager
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.pdf_adapter_view.view.*

class PDFAdapter(var context: Context, var list: ArrayList<String>) :
        RecyclerView.Adapter<PDFAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(inflater.inflate(R.layout.pdf_adapter_view, parent, false))
    }


    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].contains(".pdf")) {
            Glide.with(context)
                    .load(R.drawable.ic_pdf)
                    .apply(RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher))
                    .into(holder.itemView.image)
        }else{
            try {
                Glide.with(context)
                        .load(R.mipmap.video_icon)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher))
                        .into(holder.itemView.image)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        var input = list[position].substring(list[position].lastIndexOf('/') + 1, list[position].lastIndexOf('.'))

        try{
            holder.itemView.name.text = input.substring(0, input.indexOf("-"))
        }catch (e:Exception){
            holder.itemView.name.text = input
        }
        holder.itemView.setOnClickListener {
            if (list[position].contains(".pdf")) {
                val target = Intent(Intent.ACTION_VIEW)
                target.setDataAndType(Uri.parse(list[position]), "application/pdf")
                target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

                val intent = Intent.createChooser(target, "Open File")
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // Instruct the user to install a PDF reader here, or something
                }
            }else{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(list[position]), "video/*")
                context.startActivity(intent)
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

