package com.phaseII.placepoint.Business.Profile.AddImageVideo

import android.Manifest
import android.app.Activity
import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.phaseII.placepoint.R
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.google.android.gms.common.api.GoogleApiClient
import com.phaseII.placepoint.BusEvents.ClaimPostLiveFeed
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Cropper.CropperActivity
import com.phaseII.placepoint.Home.ModelClainService
import kotlinx.android.synthetic.main.business_profile_scroll.*


class ImageVideoActivity : AppCompatActivity() {

    lateinit var youtubeLink: EditText
    lateinit var videoViewLay: LinearLayout
    lateinit var imageViewLay: LinearLayout
    lateinit var horz_Image_recycler: RecyclerView
    var uriArray: ArrayList<ModelImageVideo> = ArrayList()
    var uriNewArray = ArrayList<Uri>()
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var back: ImageView
    private var listFromCropper: java.util.ArrayList<Uri>? = arrayListOf()
    lateinit var array: ArrayList<String>
    lateinit var array2: ArrayList<ModelImageVideo>
    private lateinit var pickAddress: RelativeLayout
    lateinit var client: GoogleApiClient
    protected val REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101
    protected val REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102
    protected val REQUEST_STORAGE_WRITE_ACCESS = 112
    protected val FINE_LOC = 120
    protected val REQUEST_CAMERA = 100
    protected val REQUEST_CAMERA_COVER = 103
    protected val SELECT_PICTURES = 110
    protected val SELECT_COVER = 111
    protected var GALLERY: Boolean? = false
    private var mAlertDialog: AlertDialog? = null
    var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_video)
        setToolBar()
        youtubeLink = findViewById(R.id.youtubeLink)
        imageViewLay = findViewById(R.id.imageViewLay)
        videoViewLay = findViewById(R.id.videoViewLay)
        horz_Image_recycler = findViewById(R.id.horz_Image_recycler)


        youtubeLink.setText(intent.getStringExtra("Link"))
        val bundle = intent.extras
        array = bundle!!.getStringArrayList("preImages") as ArrayList<String>
        array2 = bundle!!.getParcelableArrayList<ModelImageVideo>("newImages") as ArrayList<ModelImageVideo>
        for (i in 0 until array.size) {
            var model = ModelImageVideo()
            model.type = "old"
            model.oldUri = array[i]
            model.uri = Uri.parse("")
            uriArray.add(model)
        }
        if (array2.size > 0) {
            uriArray.addAll(0, array2)
        }

        setAdapterForCroppedImages(uriArray)
        videoViewLay.setOnClickListener {
            // showOptions()
            pickVideo()
        }
        imageViewLay.setOnClickListener {
            //            val intent1 = Intent()
//            intent1.type = "image/*"
//            intent1.setAction(Intent.ACTION_GET_CONTENT);
            setCoverImage()


        }
    }


    fun setCoverImage() {
        val items = arrayOf("Open Camera", "Choose From Gallery")
        val value = arrayOfNulls<String>(1)
        val builder = AlertDialog.Builder(this)
                .setSingleChoiceItems(items, 0) { dialog, which -> value[0] = items[which] }
                .setPositiveButton("Ok") { dialog, which ->
                    try {
                        val lw = (dialog as AlertDialog).listView
                        val pos = lw.checkedItemPosition
                        if (pos == 0) {
                            cameraIntent("cover")
                        } else {
                            pickFromGallery("one")
                        }
                        dialog.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                .setNegativeButton("Cancel"
                ) { dialog, which -> dialog.dismiss() }.create()
        builder.show()

    }
    private fun pickVideo() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 975)
        }else {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 10)
        }

    }

    private fun pickFromGallery(s: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 89)
        }else {
            startActivityForResult(Intent.createChooser(getFileChooserIntentForImageAndPdf(), "Select Picture"), 12)
        }

    }

    fun cameraIntent(s: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 900)
        } else {

            if (s == "cover") {

               val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, 112)


            } else {

                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, 112)
            }


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 975) {
            if (checkWriteExternalPermission()) {
                val intent = Intent()
                intent.type = "video/*"
                intent.action = Intent.ACTION_OPEN_DOCUMENT
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 10)

            }
        }
     if (requestCode == 89) {
            if (checkWriteExternalPermission()) {
                startActivityForResult(Intent.createChooser(getFileChooserIntentForImageAndPdf(), "Select Picture"), 12)

            }
        }
        if (requestCode == 900) {
            if (checkCameraPermission()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, 112)

            }
        }
    }

    fun showOptions() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.gallery_youtube, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Select")
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        val done = mAlertDialog.findViewById<TextView>(R.id.done)
        val cancel = mAlertDialog.findViewById<TextView>(R.id.cancel)
        val radioYoutube = mAlertDialog.findViewById<RadioButton>(R.id.youtubeRadio)
        val radioGallery = mAlertDialog.findViewById<RadioButton>(R.id.galleryRadio)


        done!!.setOnClickListener {

            if (radioGallery!!.isChecked) {
                val intent = Intent()
                intent.type = "video/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 10)

            } else if (radioYoutube!!.isChecked) {
            }
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout

        }
        //cancel button click of custom layout
        cancel!!.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        back = toolbar.findViewById(R.id.back) as ImageView
        back.visibility = View.GONE
        mTitle.text = "Add Files & Video"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_des, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.done) {
            val intent = intent
            intent.putExtra("uriFile", uriArray)
            intent.putExtra("youTubeLinkString", youtubeLink.text.toString().trim())
            setResult(509, intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setAdapterForCroppedImages(list: ArrayList<ModelImageVideo>?) {
        if (list != null) {
            if (list.size > 0) {
                val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                horz_Image_recycler.layoutManager = linearLayoutManager
                horz_Image_recycler.adapter = HorzRecyclerAdapter(this, list)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            try {
                val uri = data!!.data
                var videoOrImage = isImageFile(uri)
                var model = ModelImageVideo()
                if (videoOrImage.contains("video")) {
                    model.type = "video"
                    model.uri = uri
                    model.oldUri = ""
                } else {
                    model.type = "image"
                    model.uri = uri
                    model.oldUri = ""
                }
                uriArray.add(0, model)

                setAdapterForCroppedImages(uriArray)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 112) {
                try {
                    var videoOrImage = isImageFile(imageUri)
                    var model = ModelImageVideo()
                    if (videoOrImage == "application/pdf") {
                        model.type = "pdf"
                        model.uri = this!!.imageUri!!
                        model.oldUri = ""
                        uriArray.add(0, model)
                        setAdapterForCroppedImages(uriArray)
                    } else {
                        uriNewArray.clear()
                        uriNewArray.add(this!!.imageUri!!)
                        openCropper(uriNewArray, "")
                    }
//                    recyclerItems.clear()
//                    recyclerItems.add(imageUri!!)
//                    openCropper(recyclerItems, "")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (requestCode == 12) {
            try {
                val uri = data!!.data

                var videoOrImage = isImageFile(uri)
                var model = ModelImageVideo()
                if (videoOrImage == "application/pdf") {
                    model.type = "pdf"
                    model.uri = uri
                    model.oldUri = ""
                    uriArray.add(0, model)
                    setAdapterForCroppedImages(uriArray)
                } else {
                    uriNewArray.clear()
                    uriNewArray.add(uri)
                    openCropper(uriNewArray, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (requestCode == 2) {
            try {
                val args = data!!.getBundleExtra("BUNDLE")
                listFromCropper = (args.getSerializable("ARRAYLIST") as java.util.ArrayList<Uri>?)

                for (i in 0 until listFromCropper!!.size) {
                    var videoOrImage = isImageFile(this!!.listFromCropper!![i])
                    var model = ModelImageVideo()
                    if (videoOrImage == "application/pdf") {
                        model.type = "pdf"
                        model.uri = this!!.listFromCropper!![i]
                        model.oldUri = ""
                    } else {
                        model.type = "image"
                        model.uri = this!!.listFromCropper!![i]
                        model.oldUri = ""
                    }

                    uriArray.add(0, model)
                }

                setAdapterForCroppedImages(uriArray)
            } catch (e: Exception) {

            }
        }

    }

    private fun checkWriteExternalPermission(): Boolean {
        var permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        var res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private fun checkCameraPermission(): Boolean {
        var permission = android.Manifest.permission.CAMERA;
        var res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    fun openCropper(list: java.util.ArrayList<Uri>, s: String) {
        if (s == "cover") {
            val intent = Intent(this, CropperActivity::class.java)
            val args = Bundle()
            //args.putSerializable("LIST", CoverItems)
            intent.putExtra("BUN", args)
            intent.putExtra("from", "bus")
            startActivityForResult(intent, 7)
        } else {
            val intent = Intent(this, CropperActivity::class.java)
            val args = Bundle()
            args.putSerializable("LIST", list)
            intent.putExtra("BUN", args)
            intent.putExtra("from", "bus")
            startActivityForResult(intent, 2)
        }
    }

    fun getFileChooserIntentForImageAndPdf(): Intent {
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*|application/pdf")
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        return intent
    }

    fun isImageFile(uri: Uri?): String {
        var mimeType: String = ""
        if (uri!!.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            var cr = contentResolver
            mimeType = cr.getType(uri)
        } else {
            var fileExtension: String = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString())
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase())
        }
        return mimeType

    }

}
