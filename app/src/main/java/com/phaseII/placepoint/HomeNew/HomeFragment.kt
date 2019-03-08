package com.phaseII.placepoint.HomeNew

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.phaseII.placepoint.R
import org.jetbrains.anko.find





class HomeFragment: Fragment() {
    lateinit var switch:SwitchFragment
    lateinit var seeAllLayout:LinearLayout
    lateinit var seeCategoriesLayout:LinearLayout
    lateinit var updateLay:LinearLayout
    lateinit var toolbar: Toolbar
    lateinit var mText:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_newhome, container, false)
        updateLay=view.findViewById(R.id.updateLay)
        seeCategoriesLayout=view.findViewById(R.id.seeCategoriesLayout)
        seeAllLayout=view.findViewById(R.id.seeAllLayout)
        toolbar = view.findViewById(R.id.toolbar)
        mText = toolbar.findViewById(R.id.toolbar_title)
        mText.text = "Welcome to PlacePoint"
        seeAllLayout.setOnClickListener{
            switch.homeClick(1)
        }
        seeCategoriesLayout.setOnClickListener{
            switch.homeClick(2)
        }
        updateLay.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.placepoint.ie/download.php"));
            activity!!.startActivity(browserIntent);
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        switch=context as SwitchFragment
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        switch=activity as SwitchFragment
    }
    interface SwitchFragment{
        fun homeClick(value:Int)
    }
}
