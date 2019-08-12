package com.phaseII.placepoint.AboutBusiness.BusinessDetails.ImageVideoPDF


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.phaseII.placepoint.R


class VideoFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var getArray: GetArrayVideo
    var videoArray:ArrayList<String> = ArrayList()
    lateinit var adapter: PDFAdapter
    lateinit var noImage: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_video, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        videoArray=getArray.getArrayVideo()
        noImage = view.findViewById(R.id.noImage)
        if (videoArray.size==0){
            noImage.visibility=View.VISIBLE
        }else{
            noImage.visibility=View.GONE
        }
        adapter= PDFAdapter( activity!!,videoArray)
        val linear= LinearLayoutManager(activity)
        recyclerView.layoutManager=linear
        recyclerView.adapter=adapter
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getArray= context as GetArrayVideo
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        getArray= activity as GetArrayVideo
    }
    interface GetArrayVideo{
        fun getArrayVideo():ArrayList<String>
    }
}
