package com.phaseII.placepoint.Business.Profile

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Loveleen on 6/6/18.
 */
@SuppressLint("ParcelCreator")
class ModelTime() :Parcelable {
    var startFrom: String = "12:00 AM"
    var startTo: String = "12:00 AM"
    var closeFrom: String = "12:00 AM"
    var closeTo: String = "12:00 AM"
    var status: Boolean = false
    var closeStatus: Boolean = false

    constructor(parcel: Parcel) : this() {
        startFrom = parcel.readString()
        startTo = parcel.readString()
        closeFrom = parcel.readString()
        closeTo = parcel.readString()
        status = parcel.readByte() != 0.toByte()
        closeStatus = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(startFrom)
        parcel.writeString(startTo)
        parcel.writeString(closeFrom)
        parcel.writeString(closeTo)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeByte(if (closeStatus) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelTime> {
        override fun createFromParcel(parcel: Parcel): ModelTime {
            return ModelTime(parcel)
        }

        override fun newArray(size: Int): Array<ModelTime?> {
            return arrayOfNulls(size)
        }
    }
}