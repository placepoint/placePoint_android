package com.phaseII.placepoint.ConstantClass

import android.app.Activity
import android.support.v4.app.ActivityCompat

class Utils {



 /*   fun categoryList(): ArrayList<CategoryModel> {
        val list = arrayListOf<CategoryModel>()
        var model = CategoryModel()
        model.title = "Restaurant One"
        //  model.profilePic = R.drawable.ic_pp_one
        model.description = "This cafe has Yummy food..You can visit and enjoy :)"
        //  model.statusImage = R.drawable.resta_one
        list.add(model)
        model = CategoryModel()
        model.title = "Restaurant Two"
        //   model.profilePic = R.drawable.ic_pp_two
        model.description = "Enjoy Cheese burst Pizza :P"
        // model.statusImage = R.drawable.resta_two
        list.add(model)
        model = CategoryModel()
        model.title = "Restaurant Three"
        //  model.profilePic = R.drawable.ic_pp_three
        model.description = "Look at the menu and you will find something new"
        // model.statusImage = R.drawable.resta_three
        list.add(model)
        return list
    }*/

    fun getPermission(permission: String, context: Activity,requestCode:Int) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode)
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode)
        }
    }

 /*   private fun getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(client)
        if (mLastLocation != null) {
            myLocation = mLastLocation
        }

    }*/

}

