package com.phaseII.placepoint.More


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.phaseII.placepoint.Constants

import com.phaseII.placepoint.R


class MoreFragment : Fragment(){

    lateinit var list: RecyclerView
    lateinit var toolbar: Toolbar
    private lateinit var mTitle: TextView
//  lateinit var goToHome:HomeTab
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_more, container, false)
        init(view)
        setAdApter()
        setToolBar(view)
        return view
    }

    private fun setToolBar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text="More"

    }

    private fun setAdApter() {
        val items: ArrayList<String> = ArrayList()
        if (Constants.getPrefs(activity!!)!!.getBoolean(Constants.LOGGED,false)){

            items.add("Privacy Policy")
            items.add("Terms and conditions")
            items.add("Choose Town")
            items.add("Logout")
        }else{
            items.add("Privacy Policy")
            items.add("Terms and conditions")
            items.add("Choose Town")

        }

        var adapter = MoreAdapter(this.activity!!, items)
        val linear = LinearLayoutManager(activity)
        list.layoutManager = linear
        list.adapter = adapter
    }

    private fun init(view: View) {
        list = view.findViewById(R.id.recyclerView)

    }

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        goToHome= context as HomeTab
//    }

//    override fun onAttach(activity: Activity?) {
//        super.onAttach(activity)
//        goToHome= activity as HomeTab
//    }
//    override fun comingToHome() {
//        goToHome.showHome()
//    }
//    interface HomeTab{
//        fun showHome()
//    }
}
