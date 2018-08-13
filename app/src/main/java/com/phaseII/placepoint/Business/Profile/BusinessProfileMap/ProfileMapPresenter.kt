package com.phaseII.placepoint.Business.Profile.BusinessProfileMap

import com.google.android.gms.location.places.Place

class ProfileMapPresenter(val view: ProfileMapHelper) {
    fun setMapConfiguration() {
        view.setMapMethods()
    }

    fun setLocationData(p0: Place?) {

        view.setGeoCoderLocation(p0)
    }

}
