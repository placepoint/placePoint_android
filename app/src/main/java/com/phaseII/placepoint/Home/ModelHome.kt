package com.phaseII.placepoint.Home

import android.os.Parcel
import android.os.Parcelable

class ModelHome() : Parcelable {
    lateinit var id: String
    lateinit var image_url: String
    lateinit var title: String
    lateinit var video_link: String
    lateinit var description: String
    lateinit var bussness_id: String
    lateinit var created_at: String
    lateinit var updated_at: String
    lateinit var town: String
    lateinit var status: String
    lateinit var business_name: String
    lateinit var user_type: String
    lateinit var ftype: String
    lateinit var expired: String
    lateinit var redeemed: String
    lateinit var max_redemption: String
    lateinit var per_person_redemption: String
    lateinit var validity_date: String
    lateinit var created_by: String
    lateinit var retail_price: String
    lateinit var sale_price: String
    lateinit var discount_price: String
    var personRedeem: String = "0"
    lateinit var menu_images: ArrayList<String>

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        image_url = parcel.readString()
        title = parcel.readString()
        video_link = parcel.readString()
        description = parcel.readString()
        bussness_id = parcel.readString()
        created_at = parcel.readString()
        updated_at = parcel.readString()
        town = parcel.readString()
        status = parcel.readString()
        business_name = parcel.readString()
        user_type = parcel.readString()
        ftype = parcel.readString()
        expired = parcel.readString()
        redeemed = parcel.readString()
        max_redemption = parcel.readString()
        per_person_redemption = parcel.readString()
        validity_date = parcel.readString()
        created_by = parcel.readString()
        retail_price = parcel.readString()
        sale_price = parcel.readString()
        discount_price = parcel.readString()
        personRedeem = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image_url)
        parcel.writeString(title)
        parcel.writeString(video_link)
        parcel.writeString(description)
        parcel.writeString(bussness_id)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(town)
        parcel.writeString(status)
        parcel.writeString(business_name)
        parcel.writeString(user_type)
        parcel.writeString(ftype)
        parcel.writeString(expired)
        parcel.writeString(redeemed)
        parcel.writeString(max_redemption)
        parcel.writeString(per_person_redemption)
        parcel.writeString(validity_date)
        parcel.writeString(created_by)
        parcel.writeString(retail_price)
        parcel.writeString(sale_price)
        parcel.writeString(discount_price)
        parcel.writeString(personRedeem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelHome> {
        override fun createFromParcel(parcel: Parcel): ModelHome {
            return ModelHome(parcel)
        }

        override fun newArray(size: Int): Array<ModelHome?> {
            return arrayOfNulls(size)
        }
    }


}
