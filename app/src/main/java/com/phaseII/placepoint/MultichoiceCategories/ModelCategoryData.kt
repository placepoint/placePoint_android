package com.phaseII.placepoint.MultichoiceCategories

import android.os.Parcel
import android.os.Parcelable

class ModelCategoryData() : Parcelable {
    lateinit var id:String
    lateinit var name:String
    lateinit var status:String
    lateinit var image_url:String
    lateinit var parent_category:String
    lateinit var created_at:String
    lateinit var updated_at:String
    lateinit var show_on_live:String
    lateinit var town_id:String
    var checked:Boolean=false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        status = parcel.readString()
        image_url = parcel.readString()
        parent_category = parcel.readString()
        created_at = parcel.readString()
        updated_at = parcel.readString()
        show_on_live = parcel.readString()
        town_id = parcel.readString()
        checked = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(image_url)
        parcel.writeString(parent_category)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(show_on_live)
        parcel.writeString(town_id)
        parcel.writeByte(if (checked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelCategoryData> {
        override fun createFromParcel(parcel: Parcel): ModelCategoryData {
            return ModelCategoryData(parcel)
        }

        override fun newArray(size: Int): Array<ModelCategoryData?> {
            return arrayOfNulls(size)
        }
    }

}