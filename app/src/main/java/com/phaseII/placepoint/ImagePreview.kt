package com.phaseII.placepoint

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreview : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
         window.setBackgroundDrawable( ColorDrawable(0))
        Glide.with(this)
                .load(intent.getStringExtra("image"))
                .apply(RequestOptions()

                        .placeholder(R.mipmap.placeholder))
                .into(imagePreView)
        cancel.setOnClickListener{
            finish()
        }
    }
}
