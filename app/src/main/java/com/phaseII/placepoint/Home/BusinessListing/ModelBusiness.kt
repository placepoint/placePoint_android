package com.phaseII.placepoint.Home.BusinessListing

import android.os.Parcel
import android.os.Parcelable

class ModelBusiness() : Parcelable {
    var id: String = ""
    var business_user_id: String = ""
    var business_name: String = ""
    var video_link: String = ""
    var town_id: String = ""
    var category_id: String = ""
    var address: String = ""
    var contact_no: String = ""
    var opening_time: String = ""
    var description: String = ""
    var cover_image: String = ""
    var lat: String = ""
    var long: String = ""
    lateinit var created_at: String
    var image_url: Array<String> = arrayOf(String())
    var updated_at: String = ""
    var user_type: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        business_user_id = parcel.readString()
        business_name = parcel.readString()
        video_link = parcel.readString()
        town_id = parcel.readString()
        category_id = parcel.readString()
        address = parcel.readString()
        contact_no = parcel.readString()
        opening_time = parcel.readString()
        description = parcel.readString()
        cover_image = parcel.readString()
        lat = parcel.readString()
        long = parcel.readString()
        created_at = parcel.readString()
        image_url = parcel.createStringArray()
        updated_at = parcel.readString()
        user_type = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(business_user_id)
        parcel.writeString(business_name)
        parcel.writeString(video_link)
        parcel.writeString(town_id)
        parcel.writeString(category_id)
        parcel.writeString(address)
        parcel.writeString(contact_no)
        parcel.writeString(opening_time)
        parcel.writeString(description)
        parcel.writeString(cover_image)
        parcel.writeString(lat)
        parcel.writeString(long)
        parcel.writeString(created_at)
        parcel.writeStringArray(image_url)
        parcel.writeString(updated_at)
        parcel.writeString(user_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelBusiness> {
        override fun createFromParcel(parcel: Parcel): ModelBusiness {
            return ModelBusiness(parcel)
        }

        override fun newArray(size: Int): Array<ModelBusiness?> {
            return arrayOfNulls(size)
        }
    }


}
