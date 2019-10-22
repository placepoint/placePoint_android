package com.phaseII.placepoint.Home.LiveFeeds

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.ImageVideoPDF.PDFAdapter
import com.phaseII.placepoint.AboutBusiness.BusinessViewPager
import com.phaseII.placepoint.AboutBusiness.MenuViewPager
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.menu_adapter_view.view.*
import kotlinx.android.synthetic.main.pdf_adapter_view.view.*
import kotlinx.android.synthetic.main.pdf_adapter_view.view.image

class MenuAdapter(var context: Context, var list: ArrayList<String>) :
        RecyclerView.Adapter<PDFAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PDFAdapter.ViewHolder {

        return PDFAdapter.ViewHolder(inflater.inflate(R.layout.menu_adapter_view, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PDFAdapter.ViewHolder, position: Int) {


        if (list[position].contains(".pdf")) {
            Glide.with(context)
                    .load(R.drawable.ic_pdf_large)
                    .apply(RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher))
                    .into(holder.itemView.image)
        } else  {
            try {
                Glide.with(context)
                        .load(list[position])
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher))
                        .into(holder.itemView.image)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        var input = list[position].substring(list[position].lastIndexOf('/') + 1, list[position].lastIndexOf('.'))
//
//        try{
//            holder.itemView.name.text = input.substring(0, input.indexOf("-"))
//        }catch (e:Exception){
//            holder.itemView.name.text = input
//        }
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
            } else  {
                val intent = Intent(context, MenuViewPager::class.java)
                val args = Bundle()
                args.putStringArrayList("list", list)
                intent.putExtra("BUN", args)
                intent.putExtra("Position", position)
                context.startActivity(intent)
            }

        }


    }

}
