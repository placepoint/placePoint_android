package com.phaseII.placepoint.BusinessDetailMap

import android.content.Intent

class BusinessDetailMapPresenter(val view: BusinessDetailMapHelper) {
    fun setMapConfiguration() {
        view.setMapMethods()
    }

    fun getIntentData(intent: Intent?) {
        if (intent != null) {
            val location = intent.getStringExtra("location")
            view.setLocationToMap(location)
        }
    }

}
