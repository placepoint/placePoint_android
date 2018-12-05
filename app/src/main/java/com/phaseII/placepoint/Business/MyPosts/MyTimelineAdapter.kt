package com.phaseII.placepoint.Business.MyPosts

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.BufferType
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import kotlinx.android.synthetic.main.timeline_item.view.*
import com.phaseII.placepoint.AboutBusiness.AboutBusinessActivity
import com.phaseII.placepoint.Business.MyPosts.FlashDetail.FlashDetailActivity
import com.phaseII.placepoint.ConstantClass.MySpannable
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.ModelHome
import com.phaseII.placepoint.R
import java.text.SimpleDateFormat
import java.util.regex.Pattern


class MyTimelineAdapter(private val context: Context, private val list: ArrayList<ModelHome>) : RecyclerView.Adapter<MyTimelineAdapter.ViewHolder>() {

    private var inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.timeline_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelData = list[position]
        val name = Constants.getPrefs(context)?.getString(Constants.MY_BUSINESS_NAME, "")!!
        val inFormat1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val inFormat = SimpleDateFormat("MMM, dd yyyy hh:mm aa")
        val newDate = inFormat1.parse(modelData.created_at)

if(modelData.ftype=="0"){
    holder.itemView.shareFaceBook.visibility=View.VISIBLE
    holder.itemView.flashImage.visibility=View.GONE
}else{
    holder.itemView.shareFaceBook.visibility=View.GONE
    holder.itemView.flashImage.visibility=View.VISIBLE

}
        holder.itemView.dateTime.text = inFormat.format(newDate)
        holder.itemView.name.text = name
        if (!modelData.video_link.isEmpty()) {
            holder.itemView.videoUrl.visibility = View.GONE
            holder.itemView.videoUrl.text = modelData.video_link
            holder.itemView.videoUrl.paintFlags = holder.itemView.videoUrl.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        } else {
            holder.itemView.videoUrl.visibility = View.GONE
        }


        holder.itemView.name.setOnClickListener {
            val intent = Intent(context, AboutBusinessActivity::class.java)
            intent.putExtra("busId", modelData.bussness_id)
            intent.putExtra("showallpost", "no")
            intent.putExtra("from", "timelineadapter")
            intent.putExtra("busName", modelData.business_name)
            intent.putExtra("subscriptionType", Constants.getPrefs(context)!!.getString(Constants.USERTYPE, ""))
            context.startActivity(intent)
        }

        holder.itemView.flashImage.setOnClickListener {
            val intent = Intent(context, FlashDetailActivity::class.java)
            intent.putExtra("busId", modelData.id)
            intent.putExtra("showallpost", "no")
            intent.putExtra("from", "timelineadapter")
            intent.putExtra("busName", modelData.business_name)
            intent.putExtra("subscriptionType", Constants.getPrefs(context)!!.getString(Constants.USERTYPE, ""))
            context.startActivity(intent)
        }
        holder.itemView.shareFaceBook.setOnClickListener {
            setClipboard(context, modelData.description, modelData)

        }
        holder.itemView.postText.text = modelData.description
        try {
            if (modelData.video_link.startsWith("http")) {
                holder.itemView.name.videoUrl.visibility = View.VISIBLE
                holder.itemView.name.videoUrl.text = modelData.video_link
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (context != null) {

            if (modelData.image_url == "") {
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
                if(videoId.contains("frameborder")){
                    val split = videoId.split("frameborder")
                    videoId = split[0]

                }
                Glide.with(context)
                        .load("https://img.youtube.com/vi/$videoId/0.jpg")
                        .apply(RequestOptions()

                                .placeholder(R.mipmap.placeholder))
                        .into(holder.videoImage)
            }
            holder.itemView.videoLayout.setOnClickListener {
                val text = holder.itemView.videoUrl.text.toString()
                if (URLUtil.isValidUrl(text)) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelData.video_link)))
                }
            }
        }
    }

    fun extractYoutubeVideoId(ytUrl: String): String {

        var vId: String = ""
        val pattern: String = "(?<=watch\\?v=|/videos/|embed\\/|embed/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

        val compiledPattern = Pattern.compile( pattern)
        val matcher = compiledPattern.matcher(ytUrl)

        if (matcher.find()) vId = matcher.group()
        return vId
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setClipboard(context: Context, text: String, modelData: ModelHome) {

        subscriptionDialog(context, modelData)//Toast.makeText(context,"Your text has been copied to clipBoard.",Toast.LENGTH_LONG).show()
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
            Constants.shareOnFaceBook(modelData.description, modelData.image_url, context)
        })
        val alert = dialog.create()
        alert.show()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var postImage: ImageView = itemView.findViewById(R.id.postImage)
        var videoImage: ImageView = itemView.findViewById(R.id.videoImage)
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
                tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.text.toString()), tv, expandText,
                                viewMore), BufferType.SPANNABLE)
            }
        })

    }


    private fun addClickablePartTextViewResizable(strSpanned: Spanned, tv: TextView, spannableText: String, viewMore: Boolean): SpannableStringBuilder {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)

        if (str.contains(spannableText)) {


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
            }, str.indexOf(spannableText), str.indexOf(spannableText) + spannableText.length, 0)

        }
        return ssb
    }

}

