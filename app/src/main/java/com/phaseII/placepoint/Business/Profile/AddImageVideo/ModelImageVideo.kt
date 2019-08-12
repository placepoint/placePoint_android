package com.phaseII.placepoint.Business.Profile.AddImageVideo

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class ModelImageVideo() : Parcelable {
    lateinit var uri:Uri
    lateinit var type:String
    lateinit var oldUri:String

    constructor(parcel: Parcel) : this() {
        uri = parcel.readParcelable(Uri::class.java.classLoader)
        type = parcel.readString()
        oldUri = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(uri, flags)
        parcel.writeString(type)
        parcel.writeString(oldUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelImageVideo> {
        override fun createFromParcel(parcel: Parcel): ModelImageVideo {
            return ModelImageVideo(parcel)
        }

        override fun newArray(size: Int): Array<ModelImageVideo?> {
            return arrayOfNulls(size)
        }
    }
}