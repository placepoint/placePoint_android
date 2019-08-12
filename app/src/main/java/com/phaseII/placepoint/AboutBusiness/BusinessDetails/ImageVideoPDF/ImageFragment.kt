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

class ImageFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var getArray: GetArrayImage
    var imageArray:ArrayList<String> = ArrayList()
    lateinit var adapter: ImageViewAdapter
    lateinit var noImage: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_image, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)

        imageArray=getArray.getArrayImage()
        noImage = view.findViewById(R.id.noImage)
        if (imageArray.size==0){
            noImage.visibility=View.VISIBLE
        }else{
            noImage.visibility=View.GONE
        }
        adapter= ImageViewAdapter( activity!!,imageArray)
        val linear=LinearLayoutManager(activity)
        recyclerView.layoutManager=linear
        recyclerView.adapter=adapter
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getArray= context as GetArrayImage
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        getArray= activity as GetArrayImage
    }
    interface GetArrayImage{
        fun getArrayImage():ArrayList<String>
    }
}
