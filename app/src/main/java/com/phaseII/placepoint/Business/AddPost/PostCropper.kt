package com.phaseII.placepoint.Business.AddPost

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton

import android.widget.Toast
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.naver.android.helloyako.imagecrop.util.BitmapLoadUtils
import com.phaseII.placepoint.Cropper.BaseActivity
import com.phaseII.placepoint.Cropper.CropperHelper
import com.phaseII.placepoint.Cropper.CropperPresenter
import com.phaseII.placepoint.Cropper.ImageAdapter
import com.phaseII.placepoint.R
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PostCropper : BaseActivity(), CropperHelper, ImageAdapter.sendDataListener {
    // lateinit var imageView: ImageView
     lateinit var image_profile1: CropImageView
    lateinit var mPresenter: CropperPresenter
    lateinit var filePath: String
    lateinit var done: FloatingActionButton
    lateinit var recycler: RecyclerView
    var pagerList: ArrayList<Uri> = arrayListOf()
    var pos: Int = 0
    var from:String=""
    var recyclerItems: MutableList<Uri> = arrayListOf()
    private var listFromCamera: ArrayList<Uri>? = arrayListOf()
    lateinit var resultUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_cropper)
        mPresenter = CropperPresenter(this@PostCropper)
        image_profile1 = findViewById(R.id.image_profile12)
        //imageView = findViewById(R.id.imageView)
        done = findViewById(R.id.done)
        recycler = findViewById(R.id.recycler)

        recycler.stopNestedScroll()
        recycler.setHasFixedSize(true)
        mPresenter = CropperPresenter(this@PostCropper)


        done.setOnClickListener {
            val b =image_profile1.croppedImage
            //if (!image_profile.isChangingScale) {
           // val b = image_profile1.croppedImage
            //val b = resultUri
            // val b = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
            if (b != null) {
                val f = SaveImage(b)
                val ur = Uri.fromFile(f)
                val hashSet = HashSet<Uri>()
                if (!pagerList.contains(ur)) {
                    pagerList.add(ur)
                }
            } else {
                Toast.makeText(this@PostCropper, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
            }
            // }
            val intent = Intent()
            val args = Bundle()
            args.putSerializable("ARRAYLIST1", pagerList)
            intent.putExtra("BUNDLE1", args)
            intent.putExtra("url", pagerList[0].toString())
            setResult(2, intent)
            recyclerItems.clear()
            pagerList.clear()
            listFromCamera!!.clear()
            finish()

        }
        var v = 0
        try {
            val args = intent.getBundleExtra("BUN")
            listFromCamera = (args.getSerializable("LIST") as ArrayList<Uri>?)
            mPresenter.setBottomRecyclerImages(listFromCamera as ArrayList<Uri>)
            filePath = getRealPathFromURI(listFromCamera!![0], this@PostCropper)
            var uri1: Uri = Uri.parse(filePath)
            // image_profile.setImageFilePath(filePath)

//            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri1)
//            image_profile1.setImageBitmap(bitmap)
//            image_profile1.requestLayout()
            image_profile1.setImageUriAsync(listFromCamera!![0])
            //image_profile1.setImageUriAsync(uri1)
//            try {
//                from=intent.getStringExtra("from")
//                if (from=="post"){
//                    image_profile1.setAspectRatio(1, 1)
//                }else{
//                    image_profile1.setAspectRatio(2, 1)
//                }
//            }catch (e:Exception){
//                e.printStackTrace()
//            }

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
                image_profile1.setImageUriAsync(ff)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
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

            //if (!image_profile.isChangingScale) {
            val b = image_profile1.croppedImage
            //val b = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)

            if (b != null) {
                val f = SaveImage(b)
                val ur = Uri.fromFile(f)
                if (!pagerList.contains(ur)) {
                    pagerList.add(ur)
                }
                filePath = getRealPathFromURI(uri, this@PostCropper)
                // image_profile.setImageFilePath(filePath)
                //var uri1: Uri = Uri.parse(filePath)
               image_profile1.setImageUriAsync(items[0])
               // image_profile1.setImageBitmap(b)
            } else {
                Toast.makeText(this@PostCropper, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
            }
            // }
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

    fun getRealPathFromURI(uri: Uri?, context: Context): String {
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
            recycler.adapter = ImageAdapter(this@PostCropper, listFromCamera!!)
        }
    }




//    fun bitmapConvertToFile(bitmap: Bitmap): File? {
//        var fileOutputStream: FileOutputStream? = null
//        var bitmapFile: File? = null
//        try {
//            val file = File(Environment.getExternalStoragePublicDirectory("image_crop_sample"), "")
//            if (!file.exists()) {
//                file.mkdir()
//            }
//
//            //    bitmapFile = File(file, "IMG_" + SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().time) + ".jpg")
//            bitmapFile = File(filePath)
//            fileOutputStream = FileOutputStream(bitmapFile)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
//            MediaScannerConnection.scanFile(this, arrayOf(bitmapFile.absolutePath), null, object : MediaScannerConnection.MediaScannerConnectionClient {
//                override fun onMediaScannerConnected() {
//
//                }
//
//                override fun onScanCompleted(path: String, uri: Uri) {
//                    //runOnUiThread { Toast.makeText(this@CropperActivity, "file saved", Toast.LENGTH_LONG).show() }
//                }
//            })
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.flush()
//                    fileOutputStream.close()
//                } catch (e: Exception) {
//                }
//
//            }
//        }
//        return bitmapFile
//    }
}
