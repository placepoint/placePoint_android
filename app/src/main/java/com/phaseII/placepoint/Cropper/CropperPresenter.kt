package com.phaseII.placepoint.Cropper

import android.net.Uri
import java.util.ArrayList

class CropperPresenter(var view: CropperHelper)
{
    fun setBottomRecyclerImages(arrayList: ArrayList<Uri>) {
        view.setDataToAdapter(arrayList)
    }

}
