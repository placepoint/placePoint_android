package com.phaseII.placepoint.MultichoiceCategories

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.phaseII.placepoint.R


class ExpandableAdapter(var context: Context, var parentList: List<ModelCategoryData>,
                        var childList: ArrayList<ModelCategoryData>,
                        var allCatagories: List<ModelCategoryData>,
                        var highlightCat: ArrayList<String>) : BaseExpandableListAdapter() {
    @SuppressLint("ResourceAsColor")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup?): View {

            val infalInflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View=infalInflater.inflate(R.layout.list_group,null)


        val lblListHeader = view.findViewById(R.id.lblListHeader) as TextView
        val mainLayout = view.findViewById(R.id.mainLayout) as ConstraintLayout
        val image_go = view.findViewById(R.id.image_go) as ImageView
        lblListHeader.setTypeface(null, Typeface.BOLD)

        lblListHeader.text = parentList[groupPosition].name

      outer@for(i in 0 until  allCatagories.size){
            if ( parentList[groupPosition].id== allCatagories[i].parent_category){
                if (allCatagories[i].checked)
                    lblListHeader.setTextColor(Color.parseColor("#34b0f2"))

                }
        }

        image_go.isSelected = isExpanded

        return view
    }

    internal lateinit var adapter: MultiChoiceRadioAdapter
    override fun getGroup(groupPosition: Int): Any {
        return parentList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }



    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return 1
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val infalInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View=infalInflater.inflate(R.layout.choice_radio_item,null)

        val mRadioButton = view.findViewById(R.id.recyclerView) as RecyclerView
        var relatedList:ArrayList<ModelCategoryData> =arrayListOf()
       for(i in 0 until childList.size){
           if (parentList[groupPosition].id == childList[i].parent_category){
               val model = ModelCategoryData()
               model.name = childList[i].name
               model.id = childList[i].id
               model.image_url = childList[i].image_url
               model.parent_category = childList[i].parent_category
               model.checked = childList[i].checked
               relatedList.add(model)
           }
       }
//        val model = ModelCategoryData()
//        model.name = "All"
//        model.id = "-1"
//        model.image_url = ""
//        model.parent_category = "-1"
//        model.checked = false
//        relatedList.add(model)
//        relatedList.add(0,model)
//        relatedList.distinctBy { it.id }
        adapter = MultiChoiceRadioAdapter(context, relatedList,allCatagories)
        val manager = LinearLayoutManager(context)
        mRadioButton.layoutManager = manager
        mRadioButton.adapter = adapter
        return view

    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
       return this.parentList.size
    }
}