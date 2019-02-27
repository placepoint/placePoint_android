package com.phaseII.placepoint.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.support.v7.widget.RecyclerView
import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.webkit.URLUtil
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.AboutBusiness.AboutBusinessActivity
import com.phaseII.placepoint.BusEvents.ClaimPost
import com.phaseII.placepoint.ConstantClass.MySpannable
import com.phaseII.placepoint.Constants
import kotlinx.android.synthetic.main.flash_main_item.view.*
import java.util.regex.Pattern
import com.phaseII.placepoint.R
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class FlashHomeAdapter(private val context: Context, private val list: ArrayList<ModelHome>) : RecyclerView.Adapter<FlashHomeAdapter.ViewHolder>() {

    var release = 0
    var state = 0
    var myPlay = 0
    var from="none"
    var pos=0
    var youtuber: YouTubePlayer? = null
    private var stopPosition: Int=0
    private var youPause: Int=0
    var pause = 0
    var pos2 = -99

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.flash_main_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelData = list.get(position)
        if (modelData.created_by=="1"){
            holder.itemView.name.text = modelData.business_name
        }else{
           // holder.itemView.name.text = "Posted on behalf of "+modelData.business_name
            var text2 = "(shared)"
            var span1 = SpannableString(modelData.business_name);
            span1.setSpan(AbsoluteSizeSpan(32), 0, modelData.business_name.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            var span2 = SpannableString(text2);
            span2.setSpan(AbsoluteSizeSpan(22), 0, text2.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

// let's put both spans together with a separator and all
            var finalText = TextUtils.concat(span1, " ", span2)
            holder.itemView.name.text = finalText

        }
        holder.itemView.postText.text = modelData.description
        Linkify.addLinks(holder.itemView.postText, Linkify.WEB_URLS)
        holder.itemView.dateTime.text = Constants.getDate2(modelData.updated_at)
        if (!list[position].video_link.trim().isEmpty()) {
            holder.itemView.videoUrl.visibility = View.GONE
            holder.itemView.videoUrl.text = Html.fromHtml("<u>" + list[position].video_link + "</u>")

        } else {
            holder.itemView.videoUrl.visibility = View.GONE
        }
        holder.itemView.videoUrl.setOnClickListener {
            context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelData.video_link)))

        }
        holder.itemView.shareFaceBook.setOnClickListener {
            setClipboard(context, list[position].description, list[position])


        }
        holder.itemView.name.setOnClickListener {
            val click = Constants.getPrefs(context!!)!!.getString(Constants.STOPCLICK, "")
            if (click == "no") {
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

        holder.itemView.claimButton.setOnClickListener {
            dialogForClaim(modelData, position)
        }

        if (context != null) {
            /*Glide.with(context).load(modelData.image_url)
                    .apply(RequestOptions()
                            .override(180, 100).dontAnimate())
                    .into(holder.itemView.postImage)*/
            if (modelData.image_url == "") {
                holder.itemView.postImage.visibility = View.GONE
            } else {
                holder.itemView.postImage.visibility = View.VISIBLE
                Glide.with(context)
                        .load(modelData.image_url)


                        .into(holder.postImage)

            }
            holder.postImage.setOnClickListener {

                Constants.showImagePreview(list[position].image_url, context)
            }
            if (modelData.video_link.equals("")) {
                holder.itemView.videoLayout.visibility = View.GONE
                // holder.itemView.youLay.visibility = View.GONE
            } else {
                holder.itemView.videoLayout.visibility = View.VISIBLE
//                var videoId = extractYoutubeVideoId(modelData.video_link)
//                if(videoId.contains("frameborder")){
//                    val split = videoId.split("frameborder")
//                    videoId = split[0]
//
//                }
//                Glide.with(context)
//                        .load("https://img.youtube.com/vi/$videoId/0.jpg")
//                        .apply(RequestOptions()
//
//                                .placeholder(R.mipmap.placeholder))
//                        .into(holder.videoImage)
                //   Toast.makeText(context,""+Constants.getPrefs(context!!)!!.getString("sposition","0"),Toast.LENGTH_SHORT).show()
                //Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show()


                var thumb: Long = (position * 1000).toLong()
                var options = RequestOptions().frame(thumb);
                Glide.with(context).load(modelData.video_link).apply(options).into(holder.itemView.thumbNail);
//                Glide.with(context)
//                        .load(ThumbnailUtils.createVideoThumbnail(modelData.video_link, MediaStore.Video.Thumbnails.MINI_KIND))
//                        .apply(RequestOptions()
//
//                                .placeholder(R.mipmap.placeholder))
//                        .into(holder.itemView.play)

                var videoId = extractYoutubeVideoId(modelData.video_link)
                if (videoId.contains("frameborder")) {
                    val split = videoId.split("frameborder")
                    videoId = split[0]

                }

                Glide.with(context)
                        .load("https://img.youtube.com/vi/$videoId/0.jpg")
                        .apply(RequestOptions()

                                .placeholder(R.mipmap.placeholder))
                        .into(holder.videoImage)
                //---------------------------------------------------------------------------


//                if (pos2 == -99) {
//                    intializeYoutube(holder, getVideoId(list[position].video_link), 0)// for(i in 0 until list.size){
//                }
                if (pos2 == position) {
                    holder.itemView.textFb.visibility = View.GONE
                    if (list[position].video_link.contains("yout")) {
                        pause = 0
                        holder.itemView.youtube_view.visibility = View.VISIBLE
                        holder.myVideo.visibility = View.GONE
                        holder.itemView.myVideoLay.visibility = View.GONE

                        if (youtuber != null && youPause != 1) {
                            if (youPause == 2) {
                                youtuber!!.pause()
                                youPause = 1
                            } else {
                                youPause = 2
                                intializeYoutube(holder, getVideoId(list[position].video_link), 1)
                            }
                        } else if (youtuber != null && youPause == 1) {
                            youPause = 2

                            youtuber!!.play()
                        } else {
                            //if (from == "you") {
                            youPause = 2
                            intializeYoutube(holder, getVideoId(list[position].video_link), 1)
                            // }
                        }


                    } else {
                        youPause = 0
                        intializeYoutube(holder, getVideoId(list[position].video_link), 0)
                        try {
                            if (youtuber != null) {
                                youtuber!!.setVolume(0)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        youtuber = null

                        holder.itemView.youtube_view.visibility = View.GONE
                        holder.itemView.thumbNail.visibility = View.GONE
                        holder.myVideo.visibility = View.VISIBLE
                        holder.itemView.myVideoLay.visibility = View.VISIBLE

                        holder.itemView.play.visibility = View.GONE
                        pause = 1
                        var LINK = list[position].video_link
                        //  holder.itemView.myVideo.setVideoPath(LINK)
                        var uri = Uri.parse(list[position].video_link)
                        holder.myVideo.setVideoURI(uri)
                        holder.myVideo.requestFocus();
                        holder.myVideo.start()
                        holder.myVideo.setOnCompletionListener(MediaPlayer.OnCompletionListener() {
                            holder.itemView.play.visibility = View.VISIBLE
                            holder.itemView.thumbNail.visibility = View.VISIBLE
                            pause = 0
                        })

                    }
                    holder.myVideo.setOnErrorListener { mediaPlayer: MediaPlayer, i: Int, i1: Int ->
                        holder.itemView.play.visibility = View.VISIBLE

                        true
                    };

                } else {
                    if (list[position].video_link.contains("yout")) {
                        intializeYoutube(holder, getVideoId(list[position].video_link), 0)
                        holder.itemView.youtube_view.visibility = View.VISIBLE
                        holder.myVideo.visibility = View.GONE
                        holder.itemView.textFb.visibility = View.GONE
                        holder.itemView.myVideoLay.visibility = View.GONE
                        try {
                            if (youtuber != null) {
                                youtuber!!.pause()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        holder.itemView.youtube_view.visibility = View.GONE
                        holder.myVideo.visibility = View.VISIBLE
                        holder.itemView.myVideoLay.visibility = View.VISIBLE
                        holder.itemView.textFb.visibility = View.GONE
                        holder.myVideo.pause()

                        holder.itemView.thumbNail.visibility = View.VISIBLE
                        holder.itemView.play.visibility = View.VISIBLE

                        if (list[position].video_link.contains(".MOV")
                                || list[position].video_link.contains(".flv")
                                || list[position].video_link.contains(".wmv")
                                || list[position].video_link.contains("facebook")) {
                            holder.itemView.textFb.visibility = View.VISIBLE
                            holder.itemView.textFb.text = modelData.video_link
                            holder.itemView.myVideoLay.visibility = View.GONE
                            holder.myVideo.visibility = View.GONE
                            holder.itemView.thumbNail.visibility = View.GONE
                            holder.itemView.youLay.visibility = View.GONE
                            holder.itemView.play.visibility = View.GONE
                            Linkify.addLinks(holder.itemView.textFb, Linkify.WEB_URLS)
                        } else {
                            holder.itemView.textFb.visibility = View.GONE
                            holder.itemView.myVideoLay.visibility = View.VISIBLE
                            holder.myVideo.visibility = View.VISIBLE
                            holder.itemView.thumbNail.visibility = View.VISIBLE
                            holder.itemView.play.visibility = View.VISIBLE
                            holder.itemView.youLay.visibility = View.VISIBLE
                        }
                    }
                }



                holder.itemView.youLay.setOnClickListener {


                    if (list[position].video_link.contains("yout")) {

                        from = "you"

                        if (pos2 != position) {
                            youPause = 0
                            youtuber = null
                        }
                        pos2 = position
                    } else {
                        if (list[position].video_link.contains(".MOV")
                                || list[position].video_link.contains(".flv")
                                || list[position].video_link.contains(".wmv")
                                || list[position].video_link.contains("facebook")) {
                            val text = holder.itemView.videoUrl.text.toString()
                            if (URLUtil.isValidUrl(text)) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(list[position].video_link)))
                            }
                        } else {
                            from = "my"
                            if (pos2 == position) {

                                holder.itemView.youtube_view.visibility = View.GONE
                                holder.myVideo.visibility = View.VISIBLE
                                holder.itemView.myVideoLay.visibility = View.VISIBLE
                                if (pause == 1) {
                                    holder.myVideo.pause()
                                    holder.itemView.play.visibility = View.VISIBLE
                                    stopPosition = holder.myVideo.getCurrentPosition();
                                    pause = 2
                                } else if (pause == 2) {
                                    pause = 1
                                    holder.itemView.play.visibility = View.GONE
                                    holder.itemView.thumbNail.visibility = View.GONE
                                    holder.itemView.pause.visibility = View.GONE
                                    holder.myVideo.seekTo(stopPosition);
                                    holder.myVideo.start();
                                } else {
                                    holder.itemView.play.visibility = View.GONE
                                    holder.itemView.thumbNail.visibility = View.GONE
                                    pause = 1
                                    var LINK = list[position].video_link
                                    //  holder.itemView.myVideo.setVideoPath(LINK)
                                    var uri = Uri.parse(list[position].video_link)
                                    holder.myVideo.setVideoURI(uri)
                                    holder.myVideo.requestFocus();
                                    holder.myVideo.start()


                                }
                                holder.myVideo.setOnCompletionListener(MediaPlayer.OnCompletionListener() {
                                    holder.itemView.play.visibility = View.VISIBLE
                                    holder.itemView.thumbNail.visibility = View.VISIBLE
                                    pause = 0

                                })
                                holder.myVideo.setOnErrorListener { mediaPlayer: MediaPlayer, i: Int, i1: Int ->
                                    holder.itemView.play.visibility = View.VISIBLE
                                    holder.itemView.thumbNail.visibility = View.VISIBLE

                                    true
                                };

                            } else {
                                pos2 = position
                                notifyDataSetChanged()
                            }
                        }
                    }

                    if (from == "my") {

                    } else {
                        notifyDataSetChanged()
                    }

                }

            }
            holder.itemView.videoLayout.setOnClickListener {
                //                val text = holder.itemView.videoUrl.text.toString()
//                if (URLUtil.isValidUrl(text)) {
//                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelData.video_link)))
//                }
            }
//            val intent = Intent(context, VideoLinkPlayerActivity::class.java)
//            intent.putExtra("link",modelData.video_link)
//            context.startActivity(intent)
            }




        if (modelData.expired == "1") {
            holder.itemView.relativeLayout2.visibility = View.GONE
            holder.itemView.claimButton.visibility = View.GONE
            if (modelData.redeemed.isEmpty() || modelData.redeemed == "0") {
                holder.itemView.header.text = "***Expired***"
            } else {
                //holder.itemView.header.text = "***Expired ${modelData.redeemed} offer(s) claimed***"
                holder.itemView.header.text = "*Expired ${modelData.redeemed} offer(s) claimed* "+findExpirey2(modelData.validity_date).replace("-","")+" ago"

            }
            holder.itemView.validityText.text = ""
            holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.expire_red))
        } else {
            val name = modelData.business_name
            holder.itemView.relativeLayout2.visibility = View.VISIBLE
            //  holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.lightGreen))
            holder.itemView.header.background = context.resources.getDrawable(R.drawable.shape_light_green)

            holder.itemView.header.text = "***Flash Alert Sale***"

            val left: Int = modelData.max_redemption.toInt() - modelData.redeemed.toInt()
            if (left == 0) {
                holder.itemView.relativeLayout2.visibility = View.GONE
                holder.itemView.claimButton.visibility = View.GONE
                if (modelData.redeemed.isEmpty() || modelData.redeemed == "0") {
                    holder.itemView.header.text = "***Expired***"
                } else {
                  //  holder.itemView.header.text = "***Expired ${modelData.redeemed} offer(s) claimed***"
                    holder.itemView.header.text = "*Expired ${modelData.redeemed} offer(s) claimed* "+findExpirey2(modelData.validity_date).replace("-","")+" ago"
                }
                holder.itemView.validityText.text = ""
                holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.expire_red))
            } else {
                holder.itemView.claimButton.visibility = View.VISIBLE
                holder.itemView.validityText.text = "Hurry Expires in " + findExpirey(modelData.validity_date) + " - Only " + left + " left"
            }

        }
        if (modelData.personRedeem.toInt() > 0) {
            holder.itemView.desc.visibility = View.VISIBLE

            holder.itemView.desc.visibility = View.VISIBLE
            holder.itemView.desc.text = modelData.personRedeem + " Offers successfully Claimed. To redeem visit the business and mention your name and email address."

        } else {
            holder.itemView.desc.visibility = View.GONE
        }
    }

    public fun getVideoId(videoUrl: String): String {
        var reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
        var pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE)
        var matcher = pattern.matcher(videoUrl)

        if (matcher.find())
            return matcher.group(1)
        return ""
    }

    private fun intializeYoutube(holder: ViewHolder, videoId: String, i: Int) {

        holder.itemView.youtube_view.initialize(YouTubePlayerInitListener {
            it.addListener(object : AbstractYouTubePlayerListener(), YouTubePlayerListener {
                override fun onApiChange() {
                    //it.loadVideo("S0Q4gqBUs7c",0f)
                }

                override fun onCurrentSecond(second: Float) {
                    //   it.loadVideo(getVideoId("https://www.youtube.com/watch?v=2gLq4Ze0Jq4"),second)
                }

                override fun onError(error: Int) {
                    //it.loadVideo("S0Q4gqBUs7c",0f)
                }

                override fun onPlaybackQualityChange(playbackQuality: String) {
                    // it.loadVideo("S0Q4gqBUs7c",0f)
                }

                override fun onPlaybackRateChange(playbackRate: String) {
                    Toast.makeText(context, playbackRate, Toast.LENGTH_SHORT).show()
                }

                override fun onStateChange(stateofVideo: Int) {
                    //Toast.makeText(context,""+Constants.getPrefs(activity!!)!!.getString("sposition","0"),Toast.LENGTH_SHORT).show()
                    //it.loadVideo("S0Q4gqBUs7c",0f)
//                    Toast.makeText(context, "State= " + stateofVideo.toString(), Toast.LENGTH_SHORT).show()
                    state = stateofVideo
//                    if (state == 2) {
//
//                        release = 0
//                    } else if (stateofVideo == 1) {
//
//                        holder.itemView.play.visibility = View.VISIBLE
//
//                        holder.myVideo.stopPlayback()
//                        holder.myVideo.seekTo(0)
//                        holder.myVideo.pause()
//
//                        holder.myVideo.stopPlayback()
//                    }
                }
//                                override fun onStateChange(@NonNull  youTubePlayer: YouTubePlayer, @NonNull  state: PlayerConstants.PlayerState) {
//
//                                }

                override fun onVideoDuration(duration: Float) {
                    // it.loadVideo(getVideoId("https://www.youtube.com/watch?v=2gLq4Ze0Jq4"),duration)
                }

                override fun onVideoId(videoId: String) {
                    // it.loadVideo(videoId,0f)
                }

                override fun onVideoLoadedFraction(loadedFraction: Float) {
                    //   it.loadVideo(getVideoId("https://www.youtube.com/watch?v=2gLq4Ze0Jq4"),loadedFraction)
                }

                override fun onReady() {
                    // it.loadVideo(getVideoId(modelData.video_link),0f)
//                                    if ((release==0)&&Constants.getPrefs(context)!!.getString("sposition", "0").toInt() == position) {

                    if (i == 1) {
                        youtuber = it


//                if (state==0) {
//
                        it.loadVideo(videoId, 0f)
                        // }

                    } else {
                        it.cueVideo(videoId, 0f)
                    }
//                                    } else{
//                                        holder.itemView.youtube_view.release()
//                                         release = 0
//                                    }
                    // it.loadVideo("2gLq4Ze0Jq4",0f)


                }

            })
        }, true)

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
                val m = (TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)).toString()
                val p = 60 * TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
                return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS).toString() + "hrs " + (TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) - 60 * TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)).toString() + "min"
            }
        }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() + "d " + (TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) - 24 * TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)).toString() + "hrs"
    }
private fun findExpirey2(validity_date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val cal = Calendar.getInstance()
        System.out.println("time => " + dateFormat.format(cal.time))

        val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val date = sdf.parse(validity_date)
        val date2 = sdf.parse(dateFormat.format(cal.time))
        val diff =  date2.time-date.time
    if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() == "0") {
            if (TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS).toString() == "0") {
                return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS).toString() + "m"
            } else {
                val m = (TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)).toString()
                val p = 60 * TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
                return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS).toString() + "h " + (TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) - 60 * TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)).toString() + "m"
            }
        }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() + "d " + (TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) - 24 * TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)).toString() + "h"
    }

    fun parseDateToddMMyyyy2(time: String): String {
        val inputPattern = "yyyy-MM-dd hh:mm:ss"
        val outputPattern = "dd-MM-yyyy"
        // val outputPattern = "hh:mm a"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
//        val sdf = SimpleDateFormat("_HHmmss")
//        val currentDateandTime = sdf.format(Date())
        return str!!
    }

    private var emailClaim: String = ""
    var countClaim: Int = 0

    private fun dialogForClaim(modelData: ModelHome, position: Int) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.claim_layout, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle("Enter Details")
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        val done = mAlertDialog.findViewById<TextView>(R.id.done)
        val cancel = mAlertDialog.findViewById<TextView>(R.id.cancel)
        val name = mAlertDialog.findViewById<EditText>(R.id.name)
        val email = mAlertDialog.findViewById<EditText>(R.id.emailClaim)
        val phoneNo = mAlertDialog.findViewById<EditText>(R.id.phoneNo)
        done!!.setOnClickListener {
            //dismiss dialog
            if (name!!.text.toString().isEmpty() || email!!.text.toString().isEmpty()) {
                Toast.makeText(context, "Please enter details", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val emailEntered = email.text.toString()
            if (!isEmailValid(emailEntered)) {
                Toast.makeText(context, "Please enter valid email.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (phoneNo != null) {
                if (phoneNo.text.toString().trim().isEmpty()||phoneNo.text.toString().length<10) {
                    Toast.makeText(context, "Please enter 10 digit Phone Number.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            //update.claimPostService(modelData,name!!.text.toString(),email!!.text.toString())
            if (emailClaim != email.text.toString()) {
                countClaim = 0
            }

            var modelc = ModelClainService()
            modelc.postId = modelData.id
            modelc.name = name.text.toString()
            modelc.email = email.text.toString()
            modelc.position = position.toString()
            modelc.phoneNo = phoneNo!!.text.toString()
            emailClaim = email.text.toString()
            countClaim += 1
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

        var compiledPattern = Pattern.compile(pattern)
        var matcher = compiledPattern.matcher(ytUrl)

        if (matcher.find()) {
            vId = matcher.group()
        }
        return vId
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage = itemView.findViewById<ImageView>(R.id.postImage)
        val videoImage = itemView.findViewById<ImageView>(R.id.videoImage)
        var myVideo: VideoView = itemView.findViewById(R.id.myVideo)
    }

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
            Constants.shareOnFaceBook(modelData.description, modelData.image_url, this.context)
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
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

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
                                viewMore), TextView.BufferType.SPANNABLE)
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
                    tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
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
