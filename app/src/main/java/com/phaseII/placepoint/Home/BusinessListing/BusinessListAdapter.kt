package com.phaseII.placepoint.Home.BusinessListing

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.AboutBusiness.AboutBusinessActivity
import com.phaseII.placepoint.R
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.content.DialogInterface
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.content.pm.PackageManager
import android.app.Activity
import android.location.Location
import android.support.v4.app.ActivityCompat
import com.phaseII.placepoint.BusEvents.CALL_EVENT
import com.phaseII.placepoint.BusEvents.TAXI_EVENT
import com.phaseII.placepoint.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.business_item.view.*
import kotlin.collections.ArrayList


class BusinessListAdapter(val context: Context, val list: ArrayList<ModelBusiness>,
                          var relatedTo: String, var currentLatitude: Double, var currentLongitude: Double) : RecyclerView.Adapter<BusinessListAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(inflater.inflate(R.layout.business_item, parent, false))
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val modelBusiness = list.get(position)

        if (modelBusiness.user_type == null) {
            modelBusiness.user_type = ""
        }
        if (modelBusiness.business_user_id == "0" || modelBusiness.user_type == "3") {
            val taxiTownId = Constants.getPrefs(context)!!.getString(Constants.TAXI_TOWNID, "");
            val choosenTownId = Constants.getPrefs(context)!!.getString(Constants.TOWN_ID, "");
            var idList = taxiTownId!!.split(",")
            val nameCat = Constants.getPrefs(context)!!.getString(Constants.CATEGORY_NAMEO, "")
            if (idList.contains(choosenTownId)) {
                holder.itemView.freeTaxi.visibility = View.VISIBLE
            } else {
                holder.itemView.freeTaxi.visibility = View.GONE
            }
            if (nameCat == "Taxis" || relatedTo == "TaxiRelatedData") {
                holder.itemView.freeTaxi.visibility = View.GONE
                holder.itemView.freenavigationImage.visibility = View.GONE
            }




            holder.itemView.view_foreground.visibility = View.GONE
            holder.itemView.layoutSecond.visibility = View.VISIBLE
            holder.itemView.title1.text = modelBusiness.business_name
            // holder.itemView.phoneNo.text = modelBusiness.contact_no
            var addList = modelBusiness.address.split(",")
            val stringBuilder = StringBuilder("")
            var prefix = ""
            if (addList.size > 3) {

                for (i in 0 until 3) {
                    stringBuilder.append(prefix)
                    prefix = ","
                    stringBuilder.append(addList[i])
                }
                holder.itemView.address.text = String(stringBuilder)
            } else {
                holder.itemView.address.text = modelBusiness.address
            }
            try {
//                Picasso.with(context).load(list[position].cover_image)
//                        .placeholder(R.mipmap.placeholder)
//                        .centerCrop()
//                        .into(holder.itemView.imageView)

//                Picasso.with(context)
//                        .load(list[position].cover_image)
//                        .fit()
//                        .placeholder(R.mipmap.placeholder)
//                        .into(holder.itemView.freeImage)
                Glide.with(context)
                        .load(list[position].cover_image)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.placeholder))
                        .into(holder.itemView.freeImage)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            holder.itemView.freeCall.setOnClickListener {
                // Constants.getBus().post(CALL_EVENT(modelBusiness.contact_no))
                showDialog(modelBusiness.contact_no)
            }


            if ((currentLatitude == 0.0 && currentLongitude == 0.0)) {
                holder.itemView.phoneNo.visibility = View.GONE
            } else {
                try {
                    var distance = Constants.findDistanceFromCurrentPosition(currentLatitude, currentLongitude
                            , modelBusiness.lat.toDouble(), modelBusiness.long.toDouble())
                    var roundDis = String.format("%.2f", distance)
                    holder.itemView.phoneNo.visibility = View.VISIBLE
                    holder.itemView.phoneNo.text = "" + roundDis.toString() + " Km away"
                } catch (e: Exception) {
                    holder.itemView.phoneNo.visibility = View.GONE
                    e.printStackTrace()
                }
            }

            holder.itemView.freeTaxi.setOnClickListener {

                Constants.getBus().post(TAXI_EVENT("taxi"))
            }


            holder.itemView.freenavigationImage.setOnClickListener {
                try {
                    var gmmIntentUri = Uri.parse("google.navigation:q=${modelBusiness.lat},${modelBusiness.long}")
//            var  gmmIntentUri = Uri.parse("google.navigation:q=30.7398,76.7827")
                    var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            holder.itemView.layoutSecond.setOnClickListener {
                val nameCat = Constants.getPrefs(context)!!.getString(Constants.CATEGORY_NAMEO, "")
                val intent = Intent(context, AboutBusinessActivity::class.java)
                intent.putExtra("busId", modelBusiness.id)
                intent.putExtra("showallpost", "no")
                intent.putExtra("from", "businessListadapter")
                intent.putExtra("busName", modelBusiness.business_name)
                intent.putExtra("subscriptionType", modelBusiness.user_type)
                intent.putExtra("freeListing", "yes")
                intent.putExtra("nameCat", relatedTo)

                intent.putExtra("distance", holder.itemView.distance.text.toString().trim())
                intent.putExtra("mobNumber", modelBusiness.contact_no)
                intent.putExtra("lati", modelBusiness.lat)
                intent.putExtra("longi", modelBusiness.long)
                context.startActivity(intent)
            }
        } else {
            val taxiTownId = Constants.getPrefs(context)!!.getString(Constants.TAXI_TOWNID, "");
            val choosenTownId = Constants.getPrefs(context)!!.getString(Constants.TOWN_ID, "");
            var idList = taxiTownId!!.split(",")
            val nameCat = Constants.getPrefs(context)!!.getString(Constants.CATEGORY_NAMEO, "")
            if (idList.contains(choosenTownId)) {
                holder.itemView.taxiImage.visibility = View.VISIBLE
            } else {
                holder.itemView.taxiImage.visibility = View.GONE
            }
            if (nameCat == "Taxis" || relatedTo == "TaxiRelatedData") {
                holder.itemView.taxiImage.visibility = View.GONE
                holder.itemView.navigationImage.visibility = View.GONE
            }
//            else{
//                holder.itemView.taxiImage.visibility = View.VISIBLE
//                holder.itemView.navigationImage.visibility = View.VISIBLE
//            }
            holder.itemView.view_foreground.visibility = View.VISIBLE
            holder.itemView.layoutSecond.visibility = View.GONE
            holder.itemView.title.text = modelBusiness.business_name
            try {
//var roundDis:String=Constants.findDistanceFromCurrentPosition(currentLatitude,currentLatitude , modelBusiness.lat.toDouble(), modelBusiness.long.toDouble())
//                var distance = findDistance(currentLatitude, currentLongitude
//                        , modelBusiness.lat.toDouble(), modelBusiness.long.toDouble())
                var distance = Constants.findDistanceFromCurrentPosition(currentLatitude, currentLongitude
                        , modelBusiness.lat.toDouble(), modelBusiness.long.toDouble())
                var roundDis = String.format("%.2f", distance)
                val stringBuilder = StringBuilder("")
                var prefix = ""
                var addList = modelBusiness.address.split(",")
                if (addList.size > 2) {
                    for (i in 0 until 2) {
                        stringBuilder.append(prefix)
                        prefix = ","
                        stringBuilder.append(addList[i])
                    }
                    holder.itemView.desc.text = String(stringBuilder)
                } else {
                    holder.itemView.address.text = modelBusiness.address
                }
                if (currentLatitude == 0.0 && currentLongitude == 0.0) {
                    holder.itemView.distance.visibility = View.GONE
                } else {
                    holder.itemView.distance.visibility = View.VISIBLE
                    holder.itemView.distance.text = "" + roundDis.toString() + " Km away"
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (modelBusiness.opening_time.length != 0) {
                logicForOpenCloseButton(modelBusiness, holder.itemView.timing,
                        holder.itemView.statusLay, holder.itemView.openAt)
                holder.itemView.statusLay.visibility = View.VISIBLE
            } else {
                holder.itemView.statusLay.visibility = View.GONE
            }
            try {
//                Picasso.with(context).load(list[position].cover_image)
//                        .placeholder(R.mipmap.placeholder)
//                        .centerCrop()
//                        .into(holder.itemView.imageView)

                Glide.with(context)
                        .load(list[position].cover_image)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.placeholder))
                        .into(holder.itemView.imageView)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            holder.itemView.taxiImage.setOnClickListener {

                Constants.getBus().post(TAXI_EVENT("taxi"))
            }


            holder.itemView.navigationImage.setOnClickListener {
                try {
                    var gmmIntentUri = Uri.parse("google.navigation:q=${modelBusiness.lat},${modelBusiness.long}")
//            var  gmmIntentUri = Uri.parse("google.navigation:q=30.7398,76.7827")
                    var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            holder.itemView.callImage.setOnClickListener {
                // Constants.getBus().post(CALL_EVENT(modelBusiness.contact_no))
                showDialog(modelBusiness.contact_no)
            }
            holder.itemView.imageView.setOnClickListener {
                val nameCat = Constants.getPrefs(context)!!.getString(Constants.CATEGORY_NAMEO, "")
                val intent = Intent(context, AboutBusinessActivity::class.java)
                intent.putExtra("busId", modelBusiness.id)
                intent.putExtra("showallpost", "no")
                intent.putExtra("from", "businessListadapter")
                intent.putExtra("busName", modelBusiness.business_name)
                intent.putExtra("subscriptionType", modelBusiness.user_type)
                intent.putExtra("freeListing", "no")
                intent.putExtra("nameCat", relatedTo)
                intent.putExtra("distance", holder.itemView.distance.text.toString().trim())
                intent.putExtra("mobNumber", modelBusiness.contact_no)
                intent.putExtra("lati", modelBusiness.lat)
                intent.putExtra("longi", modelBusiness.long)
                context.startActivity(intent)
            }
        }


    }

    private fun findDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lon1

        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lon2

        val distanceInMeters = loc1.distanceTo(loc2)
        return (distanceInMeters.toDouble() / 1000)
    }


    private fun showDialog(phoneNo: String) {

        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle("Make a call")
        dialog.setMessage(phoneNo)
        dialog.setPositiveButton("Call", DialogInterface.OnClickListener { dialog, id ->
            //            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.data = Uri.parse(phoneNo)
//            startActivity(callIntent)
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNo")
            if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return@OnClickListener
            }

            context.startActivity(callIntent)
        })
                .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                    //Action for "Cancel".
                })

        val alert = dialog.create()
        alert.show()
    }

    //@Override
//public void onRequestPermissionsResult(int requestCode,
//                                       String permissions[], int[] grantResults) {
//    switch (requestCode) {
//        case REQUEST_PHONE_CALL: {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918511812660"));
//                startActivity(intent);
//            }
//            else
//            {
//
//            }
//            return;
//        }
//    }
//}
    private fun logicForOpenCloseButton(modelBusiness: ModelBusiness, timing: TextView,
                                        statusLay: LinearLayout, openAt: TextView) {
        val currentDay = getCurrentTimeAndDate()
        val currentTime = getCurrentTime()


//        Log.i("currentTime: ",thu)

        if (currentDay == "Mon") {

            setOpenClosed(0, timing, modelBusiness, currentTime, statusLay, openAt)

        } else if (currentDay == "Tue") {
            setOpenClosed(1, timing, modelBusiness, currentTime, statusLay, openAt)
        } else if (currentDay == "Wed") {
            setOpenClosed(2, timing, modelBusiness, currentTime, statusLay, openAt)
        } else if (currentDay == "Thu") {
            setOpenClosed(3, timing, modelBusiness, currentTime, statusLay, openAt)
        } else if (currentDay == "Fri") {
            setOpenClosed(4, timing, modelBusiness, currentTime, statusLay, openAt)
        } else if (currentDay == "Sat") {
            setOpenClosed(5, timing, modelBusiness, currentTime, statusLay, openAt)
        } else if (currentDay == "Sun") {
            setOpenClosed(6, timing, modelBusiness, currentTime, statusLay, openAt)
        }
    }

    private fun checkPreviousDayClosingHours(jsonObject: JSONObject, dayValue: Int, timing: TextView, modelBusiness: ModelBusiness, currentTime: String,
                                             statusLay: LinearLayout, openAt: TextView) {
        var startTo = jsonObject.optString("startTo")
        var startFrom = jsonObject.optString("startFrom")
        var curr: String = ""
        val inFormat = SimpleDateFormat("hh:mm aa")
        val inFormat1 = SimpleDateFormat("hh:mm:ss aa")
        val outFormat = SimpleDateFormat("HH:mm")

        val time24 = outFormat.format(inFormat.parse(startTo))
        val time245 = outFormat.format(inFormat.parse(startFrom))
        try {
            curr = outFormat.format(inFormat1.parse(currentTime))
        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            val start = outFormat.parse(time245)
            val end = outFormat.parse(time24)
            val curr2 = outFormat.parse(curr)
            if (start.after(end)) {

                if (curr2.before(end)) {
                    timing.text = "OPEN NOW"
                    openAt.visibility = View.GONE
                    statusLay.setBackgroundResource(R.drawable.background_timing_green)
                } else {
                    checkInTodayTiming(dayValue, timing, modelBusiness, currentTime, statusLay, openAt)
                }
            } else {
                checkInTodayTiming(dayValue, timing, modelBusiness, currentTime, statusLay, openAt)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    private fun checkInTodayTiming(dayValue: Int, timing: TextView, modelBusiness: ModelBusiness, currentTime: String, statusLay: LinearLayout, openAt: TextView) {


        if (!modelBusiness.opening_time.isEmpty()) {
            val array = modelBusiness.opening_time
            val arr = JSONArray(array)
            if (arr.length() > 0) {


                val objMon = arr.getJSONObject(dayValue)
                if (objMon!!.optString("startFrom").equals("closed")) {
                    timing.text = "CLOSED"
                    val timeForOpening = Constants.findingOpenWhen(modelBusiness, dayValue)
                    if (!timeForOpening.isEmpty()) {
                        openAt.visibility = View.VISIBLE
                        openAt.text = "Open $timeForOpening"
                    } else {
                        openAt.visibility = View.GONE
                    }
                    statusLay.setBackgroundResource(R.drawable.background_timing_red)

                } else {

                    if (objMon!!.optString("startFrom").equals("12:00 AM") && objMon!!.optString("startTo").equals("12:00 AM")) {
                        timing.text = "CLOSED"
                        val timeForOpening = Constants.findingOpenWhen(modelBusiness, dayValue)
                        if (!timeForOpening.isEmpty()) {
                            openAt.visibility = View.VISIBLE
                            openAt.text = "Open $timeForOpening"
                        } else {
                            openAt.visibility = View.GONE
                        }
                        statusLay.setBackgroundResource(R.drawable.background_timing_red)
                    } else {
                        val exist = checkIfCurrentTimeLiesInGivenInterVal22(currentTime, changeTimeFormat(objMon.optString("startFrom")), "startTime")
                        val exist2 = checkIfCurrentTimeLiesInGivenInterVal23(currentTime, changeTimeFormat(objMon.optString("startFrom")), changeTimeFormat(objMon.optString("startTo")), "EndTime")
                        val existInClosedTimings = checkIfCurrentTimeLiesInGivenInterVal(currentTime, changeTimeFormat(objMon.optString("closeFrom")), changeTimeFormat(objMon.optString("closeTo")))
                        if (exist && exist2) {
                            timing.text = "OPEN NOW"
                            openAt.visibility = View.GONE
                            statusLay.setBackgroundResource(R.drawable.background_timing_green)
//                      if (existInClosedTimings){
//                          timing.text="CLOSED NOW"
//                      }
                        } else {
                            timing.text = "CLOSED"
                            val timeForOpening = Constants.findingOpenWhen(modelBusiness, dayValue)
                            if (!timeForOpening.isEmpty()) {
                                openAt.visibility = View.VISIBLE
                                openAt.text = "Open $timeForOpening"
                            } else {
                                openAt.visibility = View.GONE
                            }
                            statusLay.setBackgroundResource(R.drawable.background_timing_red)
                        }

                    }
                }
            }
        }
    }

    fun setOpenClosed(dayValue: Int, timing: TextView, modelBusiness: ModelBusiness,
                      currentTime: String, statusLay: LinearLayout, openAt: TextView) {
        if (modelBusiness.opening_time != null && !modelBusiness.opening_time.isEmpty() && modelBusiness.opening_time != "null") {
            val array = modelBusiness.opening_time
            val arr = JSONArray(array)
            if (dayValue == 0) {
                checkPreviousDayClosingHours(arr.getJSONObject(6), dayValue, timing, modelBusiness,
                        currentTime, statusLay, openAt)
            } else {
                checkPreviousDayClosingHours(arr.getJSONObject(dayValue - 1), dayValue, timing, modelBusiness,
                        currentTime, statusLay, openAt)
            }
        }

//        if (!modelBusiness.opening_time.isEmpty()) {
//            val array = modelBusiness.opening_time
//            val arr = JSONArray(array)
//            if (arr.length() > 0) {
//
//
//                val objMon = arr.getJSONObject(dayValue)
//                if (objMon!!.optString("startFrom").equals("closed")) {
//                    timing.text = "CLOSED"
//                    openAt.text = "Open at 9 AM"
//                    statusLay.setBackgroundResource(R.drawable.background_timing_red)
//
//                } else {
//
//                    if (objMon!!.optString("startFrom").equals("12:00 AM") && objMon!!.optString("startTo").equals("12:00 AM")) {
//                        timing.text = "CLOSED"
//                        openAt.text = "Open at 9 AM"
//                        statusLay.setBackgroundResource(R.drawable.background_timing_red)
//                    } else {
//                        val exist = checkIfCurrentTimeLiesInGivenInterVal22(currentTime, changeTimeFormat(objMon.optString("startFrom")), "startTime")
//                        val exist2 = checkIfCurrentTimeLiesInGivenInterVal23(currentTime, changeTimeFormat(objMon.optString("startFrom")), changeTimeFormat(objMon.optString("startTo")), "EndTime")
//                        val existInClosedTimings = checkIfCurrentTimeLiesInGivenInterVal(currentTime, changeTimeFormat(objMon.optString("closeFrom")), changeTimeFormat(objMon.optString("closeTo")))
//                        if (exist && exist2) {
//                            timing.text = "OPEN NOW"
//                            openAt.visibility = View.GONE
//                            statusLay.setBackgroundResource(R.drawable.background_timing_green)
////                      if (existInClosedTimings){
////                          timing.text="CLOSED NOW"
////                      }
//                        } else {
//                            timing.text = "CLOSED"
//                            openAt.text = "Open at 9 AM"
//                            statusLay.setBackgroundResource(R.drawable.background_timing_red)
//                        }
//
//                    }
//                }
//            }
//        }
    }

    private fun checkIfCurrentTimeLiesInGivenInterVal22(currentTime: String, start: String, from: String): Boolean {

        return checkStartTimings(currentTime, start, "start")


    }

    private fun checkIfCurrentTimeLiesInGivenInterVal23(currentTime: String, start: String, end: String, from: String): Boolean {

        if (start.contains("am") && end.contains("am") ||
                start.contains("AM") && end.contains("AM")) {
            if (checkStartTimings(start, end, "start")) {
                return checkStartTimings(currentTime, "11:59:59 PM", "end")
            } else {
                return checkStartTimings(currentTime, end, "end")
            }


        }
        if (start.contains("pm") && end.contains("pm") ||
                start.contains("PM") && end.contains("PM")) {

            val inFormat = SimpleDateFormat("hh:mm:ss aa")
            val outFormat = SimpleDateFormat("HH:mm")

            val time24 = outFormat.format(inFormat.parse(start))
            val time245 = outFormat.format(inFormat.parse(end))
            try {
                val start = outFormat.parse(time24)
                val end = outFormat.parse(time245)
                if (start.after(end)) {
                    return checkStartTimings(currentTime, "11:59:59 PM", "end")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return checkStartTimings(currentTime, end, "end")

        }
        if (start.contains("pm") && end.contains("am") ||
                start.contains("PM") && end.contains("AM")) {

            return checkStartTimings(currentTime, "11:59:59 PM", "end")


        }
        if (start.contains("am") && end.contains("pm") ||
                start.contains("AM") && end.contains("PM")) {

            return checkStartTimings(currentTime, end, "end")


        }
        return checkStartTimings(currentTime, start, "end")


    }

    private fun changeTimeFormat(time: String): String {
        val inputPattern = "hh:mm aa"
        val outputPattern = "hh:mm:ss aa"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String = ""

        try {
            date = inputFormat.parse(time.trim())
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }

    private fun checkIfCurrentTimeLiesInGivenInterVal(currentTime: String, start: String, end: String): Boolean {
//        if (start.contains("AM") && end.contains("AM")||
//                (start.contains("AM") && end.contains("PM"))||
//                start.contains("PM") && end.contains("PM")) {
//            val startTime= checkStartTimings(currentTime,start,"start")
//            if (startTime){
//                if (checkStartTimings(currentTime,end,"end")) {
//                    return true
//                }
//                return  false
//            }
//        }
//
//        if (start.contains("PM") && end.contains("AM")) {
//            val startTime= checkStartTimings(currentTime,start,"start")
//            if (startTime){
//                if (checkStartTimings(currentTime, "23:59:59 PM", "end")) {
//                    return true
//                }
//                if (checkStartTimings(currentTime,end,"end")) {
//                    return true
//                }
//                return  false
//            }
//        }
        val startTime = checkStartTimings(currentTime, start, "start")
        if (startTime) {
            if (end.contains("AM")) {
                if (checkStartTimings(currentTime, "23:59:59 PM", "end")) {
                    return true
                }
            } else {
                if (checkStartTimings(currentTime, end, "end")) {
                    return true
                }
            }
//                if (checkStartTimings(currentTime,end,"end")) {
//                    return true
//                }
            return false
        }
//        val startTime= checkStartTimings(currentTime,start,"start")
//        if (startTime){
//            if (checkStartTimings(currentTime,"23:59:59 PM","end")){
//                return true
//            }else if (checkStartTimings(currentTime,end,"end")) {
//                return true
//            }else{
//                return  false
//            }
//        }
        return false
    }

    private fun checkIfCurrentTimeLiesInGivenInterVal2(currentTime: String, start: String, end: String): Boolean {
        val startTime = checkStartTimings(currentTime, start, "start")
        if (startTime) {
            if (checkStartTimings(currentTime, end, "end")) {
                return true
            } else {
                return false
            }
        }
        return false

    }

    private fun checkStartTimings(time: String, endtime: String, from: String): Boolean {

        val pattern = "hh:mm:ss aa"
        val sdf = SimpleDateFormat(pattern)

        try {
            val date1 = sdf.parse(time)
            val date2 = sdf.parse(endtime)
            if (from == "start") {
                if (date1.after(date2)) {
                    return true
                } else {
                    return false
                }
            } else if (from == "end") {
                if (date1.before(date2)) {
                    return true
                } else {
                    return false
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }


    private fun getCurrentTime(): String {

        val sdf = SimpleDateFormat("hh:mm:ss aa")
        return sdf.format(Date())
    }

    private fun getCurrentTimeAndDate(): String {
        val sdf = SimpleDateFormat("EEE")
        return sdf.format(Date())

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ViewHolder0(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView)
}
