package com.phaseII.placepoint.Business.ScheduledPost

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.phaseII.placepoint.BusEvents.DeleteScheduleEvent
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.schdule_item.view.*
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.BusEvents.EditScheduleEvent
import com.squareup.picasso.Picasso


class ScheduleAdapter(var context:Context,var list:List<ModelSchdule>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View=LayoutInflater.from(context).inflate(R.layout.schdule_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model=list[position]
        try{
            Picasso.with(context).load(model.image_url).into(holder.itemView.imageItem)
        }catch (e:Exception){
            e.printStackTrace()
        }
        if (model.type=="1"){var day=""
            if (model.day=="1"){
               day="Monday"
            }else if (model.day=="2"){
                day="Tuesday"
            }else if (model.day=="3"){
                day="Wednesday"
            }else if (model.day=="4"){
                day="Thursday"
            }else if (model.day=="5"){
                day="Friday"
            }else if (model.day=="6"){
                day="Saturday"
            }else if (model.day=="7"){
                day="Sunday"
            }
            holder.itemView.textItem.text="Scheduled on every "+ day+" @"+model.time
        }else if (model.type=="2"){
            holder.itemView.textItem.text="Scheduled on every "+ model.day+" of the month@"+model.time
        }else{
            holder.itemView.textItem.text="Scheduled on  "+ model.day+"@"+model.time
        }
        holder.itemView.imageView3.setOnClickListener{
            openOptionMenu(it,position,model)
        }
    }

    private fun openOptionMenu(v: View, position: Int, model: ModelSchdule) {
        val popup = PopupMenu(v.getContext(), v)

        popup.menuInflater.inflate(R.menu.navigation_drawer_menu_items, popup.getMenu())
        popup.setOnMenuItemClickListener {
            //Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle() + " position " + position, Toast.LENGTH_SHORT).show()
            if (it.itemId==R.id.edit){
                Constants.getBus().post(EditScheduleEvent(model))
            }else{
                Constants.getBus().post(DeleteScheduleEvent(model,position))

            }
            true
        }
        popup.show()

    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
}