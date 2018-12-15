package com.phaseII.placepoint.Home.LiveFeeds

import android.content.*
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.BufferType
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.AboutBusiness.AboutBusinessActivity
import com.phaseII.placepoint.ConstantClass.MySpannable
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.ModelHome
import com.phaseII.placepoint.R
import android.support.v7.app.AlertDialog
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.Toast
import com.phaseII.placepoint.BusEvents.ClaimPost
import com.phaseII.placepoint.Home.ModelClainService
import kotlinx.android.synthetic.main.flash_main_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class HomeAdapter(private val context: Context, private val list: ArrayList<ModelHome>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.flash_main_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelData = list.get(position)
        holder.itemView.name.text = modelData.business_name
        holder.itemView.postText.text = modelData.description
        holder.itemView.dateTime.text = Constants.getDate(modelData.updated_at)
        if (!list[position].video_link.trim().isEmpty()){
            holder.itemView.videoUrl.visibility=View.GONE
            holder.itemView.videoUrl.text=Html.fromHtml("<u>"+list[position].video_link+"</u>")

        }else{
            holder.itemView.videoUrl.visibility=View.GONE
        }
        holder.itemView.videoUrl.setOnClickListener {
            context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelData.video_link)))

        }
         holder.itemView.shareFaceBook.setOnClickListener {
             setClipboard(context,modelData.description,modelData)


        }
        holder.itemView.name.setOnClickListener {
            val click=Constants.getPrefs(context!!)!!.getString(Constants.STOPCLICK,"")
            if (click=="no") {
                val intent = Intent(context, AboutBusinessActivity::class.java)
                intent.putExtra("busId", modelData.bussness_id)
                intent.putExtra("showallpost", "no")
                intent.putExtra("from", "homeadapter")
                intent.putExtra("busName", modelData.business_name)
                intent.putExtra("subscriptionType", modelData.user_type)

                context!!.startActivity(intent)
            }
        }

        try {
            if (modelData.video_link.startsWith("http")) {
                holder.itemView.name.videoUrl.visibility = View.VISIBLE
                holder.itemView.name.videoUrl.text = modelData.video_link
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }



        if (context != null) {
            /*Glide.with(context).load(modelData.image_url)
                    .apply(RequestOptions()
                            .override(180, 100).dontAnimate())
                    .into(holder.itemView.postImage)*/
            if (modelData.image_url.equals("")) {
                holder.itemView.postImage.visibility = View.GONE
            } else {
                holder.itemView.postImage.visibility = View.VISIBLE
                Glide.with(context)
                        .load(modelData.image_url)
                        .apply(RequestOptions()

                                .placeholder(R.mipmap.placeholder))
                        .into(holder.postImage)
            }

            if (modelData.video_link.equals("")) {
                holder.itemView.videoLayout.visibility = View.GONE
            } else {
                holder.itemView.videoLayout.visibility = View.VISIBLE
                var videoId = extractYoutubeVideoId(modelData.video_link)
                Glide.with(context)
                        .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                        .apply(RequestOptions()

                                .placeholder(R.mipmap.placeholder))
                        .into(holder.videoImage)
            }
            holder.itemView.videoLayout.setOnClickListener {
                var text = holder.itemView.videoUrl.text.toString()
                if (URLUtil.isValidUrl(text)) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelData.video_link)))
                    // startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")))
                }

//            val intent = Intent(context, VideoLinkPlayerActivity::class.java)
//            intent.putExtra("link",modelData.video_link)
//            context.startActivity(intent)
            }

        }

        if(modelData.ftype=="1"){
            holder.itemView.header.visibility=View.VISIBLE
            if (modelData.expired=="1"){
                holder.itemView.relativeLayout2.visibility = View.GONE
                holder.itemView.claimButton.visibility = View.GONE
                if (modelData.redeemed.isEmpty() || modelData.redeemed == "0") {
                    holder.itemView.header.text = "***Expired***"
                } else {
                    holder.itemView.header.text = "***Expired ${modelData.redeemed} offer(s) claimed***"
                }
                holder.itemView.validityText.text = ""
                holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.expire_red))
            }else{
                //holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.lightGreen))
                holder.itemView.header.background=context.resources.getDrawable(R.drawable.shape_light_green)

                holder.itemView.relativeLayout2.visibility=View.VISIBLE
                holder.itemView.header.text="***Flash Alert Sale***"
                val left:Int=modelData.max_redemption.toInt()-modelData.redeemed.toInt()
                if (left == 0) {
                    holder.itemView.relativeLayout2.visibility = View.GONE
                    holder.itemView.claimButton.visibility = View.GONE
                    if (modelData.redeemed.isEmpty() || modelData.redeemed == "0") {
                        holder.itemView.header.text = "***Expired***"
                    } else {
                        holder.itemView.header.text = "***Expired ${modelData.redeemed} offer(s) claimed***"
                    }
                    holder.itemView.validityText.text = ""
                    holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.expire_red))
                } else {
                    holder.itemView.claimButton.visibility = View.VISIBLE
                    holder.itemView.validityText.text = "Hurry Expires in " + findExpirey(modelData.validity_date) + " - Only " + left + " left"
                }

            }

        }else{
            holder.itemView.header.visibility=View.GONE
            holder.itemView.relativeLayout2.visibility=View.GONE
        }

        holder.itemView.claimButton.setOnClickListener{
            dialogForClaim(modelData,position)
        }
        if (modelData.redeemed.toInt()>0) {
            holder.itemView.desc.visibility=View.VISIBLE
            holder.itemView.desc.setText(modelData.redeemed + " Offers successfully Claimed. To redeem visit the business and mention your name and email address.")
        }else{
            holder.itemView.desc.visibility=View.GONE
        }
   }
    private fun findExpirey(validity_date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val cal = Calendar.getInstance()
        System.out.println("time => " + dateFormat.format(cal.time))

        val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val date = sdf.parse(validity_date)
        val date2 = sdf.parse(dateFormat.format(cal.time))
        val diff = date.time - date2.time
        if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() == "0") {
            if (TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS).toString() == "0") {
                return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS).toString() + "min"
            } else {
                return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS).toString() + "hrs " + (TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) - 60 * TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)).toString() + "min"
            }
        }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() + "d "+( TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)- 24 * TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)).toString() + "hrs"
    }
    private fun dialogForClaim(modelData: ModelHome, position: Int) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.claim_layout, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle("Enter Details")
        //show dialog
        val  mAlertDialog = mBuilder.show()
        //login button click of custom layout
        val done=mAlertDialog.findViewById<TextView>(R.id.done)
        val cancel=mAlertDialog.findViewById<TextView>(R.id.cancel)
        val name=mAlertDialog.findViewById<EditText>(R.id.name)
        val email=mAlertDialog.findViewById<EditText>(R.id.emailClaim)
        done!!.setOnClickListener {
            //dismiss dialog
            if (name!!.text.toString().isEmpty()||email!!.text.toString().isEmpty()){
                Toast.makeText(context,"Please enter details", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val emailEntered=email!!.text.toString()
            if(!isEmailValid(emailEntered)){
                Toast.makeText(context,"Please enter valid email.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //update.claimPostService(modelData,name!!.text.toString(),email!!.text.toString())
            var modelc= ModelClainService()
            modelc.postId=modelData.id
            modelc.name=name!!.text.toString()
            modelc.email=email!!.text.toString()
            modelc.position=position.toString()
            Constants.getBus().post(ClaimPost(modelc))
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout

        }
        //cancel button click of custom layout
        cancel!!.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }

    }

    fun isEmailValid(email: String): Boolean {
        var isValid = false

        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    fun extractYoutubeVideoId(ytUrl: String): String {

        var vId: String = ""

        // var pattern: String = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        var pattern: String = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

        var compiledPattern = Pattern.compile( pattern)
        var matcher = compiledPattern.matcher(ytUrl)

        if (matcher.find()) {
            vId = matcher.group()
        }
        return vId
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       val postImage=itemView.findViewById<ImageView>(R.id.postImage)
       val videoImage=itemView.findViewById<ImageView>(R.id.videoImage)
    }

    private fun setClipboard(context: Context, text: String, modelData: ModelHome) {
        subscriptionDialog(context,modelData)//Toast.makeText(context,"Your text has been copied to clipBoard.",Toast.LENGTH_LONG).show()
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = text
        } else {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Copied Text", text)
            clipboard.primaryClip = clip
        }
    }

    fun subscriptionDialog(context: Context, modelData: ModelHome) {

        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle("Alert")
        dialog.setMessage("You are about to share an image to Facebook. Facebook does not allow us to send the text from the image automatically. We have copied the text from the post to the clipboard for your convenience. If you would like to add the text to the image please paste it into the comment section on the next section when sharing the post.")
        dialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
            Constants.shareOnFaceBook(modelData.description,modelData.image_url, this.context)
        })
//                .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
//                    dialog.dismiss()
//                })

        val alert = dialog.create()
        alert.show()
    }

    fun makeTextViewResizable(tv: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {

        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                val text: String
                val lineEndIndex: Int
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    lineEndIndex = tv.layout.getLineEnd(0)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1).toString() + " " + expandText
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1).toString() + " " + expandText
                } else {
                    lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                }
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                                viewMore), BufferType.SPANNABLE)
            }
        })

    }


    private fun addClickablePartTextViewResizable(strSpanned: Spanned, tv: TextView,
                                                  maxLine: Int, spanableText: String, viewMore: Boolean): SpannableStringBuilder {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)

        if (str.contains(spanableText)) {


            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), BufferType.SPANNABLE)
                    tv.invalidate()
                    if (viewMore) {
                        makeTextViewResizable(tv, -2, "less", false)
                    } else {
                        makeTextViewResizable(tv, 2, "more..", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)

        }
        return ssb

    }
}
