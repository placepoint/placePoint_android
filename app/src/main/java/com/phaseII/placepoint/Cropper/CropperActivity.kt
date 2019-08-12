package com.phaseII.placepoint.Cropper


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.widget.Toast
import com.naver.android.helloyako.imagecrop.view.ImageCropView
import java.io.File
import java.io.FileOutputStream
import java.util.*
import com.phaseII.placepoint.R
import android.provider.MediaStore.Images
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.Constants
import java.io.ByteArrayOutputStream


class CropperActivity : BaseActivity(), CropperHelper, ImageAdapter.sendDataListener {
    // lateinit var imageView: ImageView
    lateinit var image_profile: ImageCropView
    lateinit var mPresenter: CropperPresenter
    lateinit var filePath: String
    lateinit var done: FloatingActionButton
    lateinit var recycler: RecyclerView
    var pagerList: ArrayList<Uri> = arrayListOf()
    var pos: Int = 0
    var from: String = ""
    var recyclerItems: MutableList<Uri> = arrayListOf()
    private var listFromCamera: ArrayList<Uri>? = arrayListOf()
    lateinit var resultUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cropper)
        Constants.getSSlCertificate(this)
        mPresenter = CropperPresenter(this@CropperActivity)
        image_profile = findViewById(R.id.image_profile)
        //imageView = findViewById(R.id.imageView)
        done = findViewById(R.id.done)
        recycler = findViewById(R.id.recycler)

        recycler.stopNestedScroll()
        recycler.setHasFixedSize(true)
        mPresenter = CropperPresenter(this@CropperActivity)


        try {
            from = intent.getStringExtra("from")
            if (from == "post") {
                image_profile.setAspectRatio(1, 1)
            } else {
                image_profile.setAspectRatio(2, 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var v = 0
        try {
            val args = intent.getBundleExtra("BUN")
            listFromCamera = (args.getSerializable("LIST") as ArrayList<Uri>?)
            mPresenter.setBottomRecyclerImages(listFromCamera as ArrayList<Uri>)
            filePath = getRealPathFromURI(listFromCamera!![0], this@CropperActivity)
            image_profile.setImageFilePath(filePath)
        } catch (e: Exception) {
            e.printStackTrace()

            v = 1
        }
        try {
            if (v == 1) {
                val args = intent.getBundleExtra("BUN")
                listFromCamera = (args.getSerializable("LIST") as ArrayList<Uri>?)
                mPresenter.setBottomRecyclerImages(listFromCamera as ArrayList<Uri>)
                val buri=getBitmapFromUri(listFromCamera!![0])
                val ff=getImageUri(this,buri)
                filePath = getRealPathFromURI(ff,this)
                image_profile.setImageFilePath(filePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        recycler.stopNestedScroll()
        recycler.setHasFixedSize(true)
        mPresenter = CropperPresenter(this@CropperActivity)
        image_profile = findViewById(R.id.image_profile)


        done.setOnClickListener {

            if (!image_profile.isChangingScale) {
                try{
                val b = image_profile.croppedImage
                if (b != null) {
                    try {
                        val f = bitmapConvertToFile(b)
                        val ur = Uri.fromFile(f)
                        val hashSet = HashSet<Uri>()
                        if (!pagerList.contains(ur)) {
                            pagerList.add(ur)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(this@CropperActivity, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
                }
                }catch (e:Exception){
                    Toast.makeText(this@CropperActivity, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
                    e.printStackTrace()
                }
            }
            val intent = Intent()
            val args = Bundle()
            args.putSerializable("ARRAYLIST", pagerList)
            intent.putExtra("BUNDLE", args)
            setResult(2, intent)
            finish()
            recyclerItems.clear()
            pagerList.clear()
            listFromCamera!!.clear()
        }

//        try {
//            val args = intent.getBundleExtra("BUN")
//            listFromCamera = (args.getSerializable("LIST") as ArrayList<Uri>?)
//            mPresenter.setBottomRecyclerImages(listFromCamera as ArrayList<Uri>)
//            filePath = getRealPathFromURI(listFromCamera!![0], this@CropperActivity)
//            var uri1:Uri= Uri.parse(filePath)
//            image_profile.setImageFilePath(filePath)
//
////            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri1)
////            image_profile1.setImageBitmap(bitmap)
////            image_profile1.requestLayout()
////            image_profile1.setImageUriAsync(listFromCamera!![0])
//            try {
//                from=intent.getStringExtra("from")
//                if (from=="post"){
//                   image_profile.setAspectRatio(1, 1)
//                }else{
//                    image_profile.setAspectRatio(2, 1)
//                }
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }
    fun getBitmapFromUri(uri: Uri): Bitmap {
        var parcelFileDescriptor =
                this.contentResolver.openFileDescriptor(uri, "r")
        var fileDescriptor = parcelFileDescriptor.fileDescriptor
        var image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image;
    }

    override fun onDataRecieve(uri: Uri, position: Int, items: ArrayList<Uri>) {
        pos = position
        try {

            if (!image_profile.isChangingScale) {
                val b = image_profile.croppedImage
                //val b = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)

                if (b != null) {
                    val f = SaveImage(b)
                    val ur = Uri.fromFile(f)
                    if (!pagerList.contains(ur)) {
                        pagerList.add(ur)
                    }
                    filePath = getRealPathFromURI(uri, this)
                    image_profile.setImageFilePath(filePath)
//                var uri1:Uri= Uri.parse(filePath)
//                image_profile1.setImageUriAsync(uri1)
                } else {
                    Toast.makeText(this@CropperActivity, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun SaveImage(finalBitmap: Bitmap): File {

        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/Android/data/com.example.user24.placepoint/Asaved_images")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    fun getRealPathFromURI(uri: Uri?, context: CropperActivity): String {
        var cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null)
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }


    override fun onDataDelete(uri: Uri, position: Int) {
        //pagerList.remove(uri)
        recyclerItems.remove(uri)
        // listFromCamera!!.remove(uri)
    }


    override fun setDataToAdapter(arrayList: ArrayList<Uri>) {
        if (listFromCamera!!.size > 0) {
            val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            recycler.layoutManager = linearLayoutManager
            recycler.adapter = ImageAdapter(this@CropperActivity, listFromCamera!!)
        }
    }


    fun bitmapConvertToFile(bitmap: Bitmap): File? {
        var fileOutputStream: FileOutputStream? = null
        var bitmapFile: File? = null
        try {
            val file = File(Environment.getExternalStoragePublicDirectory("image_crop_sample"), "")
            if (!file.exists()) {
                file.mkdir()
            }

            //    bitmapFile = File(file, "IMG_" + SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().time) + ".jpg")
            bitmapFile = File(filePath)
            fileOutputStream = FileOutputStream(bitmapFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            MediaScannerConnection.scanFile(this, arrayOf(bitmapFile.absolutePath), null, object : MediaScannerConnection.MediaScannerConnectionClient {
                override fun onMediaScannerConnected() {

                }

                override fun onScanCompleted(path: String, uri: Uri) {
                    //runOnUiThread { Toast.makeText(this@CropperActivity, "file saved", Toast.LENGTH_LONG).show() }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: Exception) {
                }

            }
        }
        return bitmapFile
    }
}
