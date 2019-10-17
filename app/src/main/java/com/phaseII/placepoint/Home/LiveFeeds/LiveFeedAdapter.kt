package com.phaseII.placepoint.Home.LiveFeeds

import android.arch.lifecycle.LifecycleObserver
import android.content.*
import android.graphics.Paint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView.BufferType
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.AboutBusiness.AboutBusinessActivity
import com.phaseII.placepoint.ConstantClass.MySpannable
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.ModelHome
import com.phaseII.placepoint.R
import android.support.v7.app.AlertDialog
import android.text.util.Linkify
import android.support.v7.widget.RecyclerView
import android.text.*
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.style.AbsoluteSizeSpan
import android.webkit.URLUtil
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.phaseII.placepoint.BusEvents.ClaimPostLiveFeed
import com.phaseII.placepoint.Home.ModelClainService
import com.phaseII.placepoint.Service
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener
import kotlinx.android.synthetic.main.livefeed_adapter.view.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class LiveFeedAdapter(private val context: Context, private val list: ArrayList<ModelHome>, var liveFeedFragment: LiveFeedFragment) :
        RecyclerView.Adapter<LiveFeedAdapter.ViewHolder>(), LifecycleObserver {

    var pos = 0;
    //var modelData = ModelHome()
    var release = 0
    var state = 0

    var from = "none"
    var pos2 = -99;
    var youtuber: YouTubePlayer? = null
    private var stopPosition: Int = 0
    private var youPause: Int = 0;
    private var clicked: Int = 0;
    var pause = 0
    var myView = 0
    var moreless = liveFeedFragment as ShowViewMoreLess
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.livefeed_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    public fun getVideoId(videoUrl: String): String {
        var reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
        var pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE)
        var matcher = pattern.matcher(videoUrl)

        if (matcher.find())
            return matcher.group(1)
        return ""
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // modelData = list[position]
        holder.itemView.imagesLayout.visibility = View.GONE
        pos = position
        if (Constants.getPrefs(context)!!.getString(Constants.EMAIL, "").equals("help@placepoint.ie")) {
            holder.bump1.visibility = View.VISIBLE
        } else {
            holder.bump1.visibility = View.GONE
        }
        holder.bump1.setOnClickListener {
            hitService(list[position].id)
        }

        holder.itemView.menu.setOnClickListener {

            val intent = Intent(context, MenuActivity::class.java)
            intent.putExtra("menuImages",list[position].menu_images)
            context.startActivity(intent)

        }
        var retail: Double = list[position].retail_price.toDouble()
        if (retail > 0) {
            holder.itemView.retailPrice.visibility = View.VISIBLE
            holder.itemView.discount.visibility = View.VISIBLE
            holder.itemView.retailPrice.text = list[position].retail_price
            holder.itemView.retailPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.discount.text = list[position].sale_price + "(" + list[position].discount_price + "% off)"
        } else {
            holder.itemView.retailPrice.visibility = View.GONE
            holder.itemView.discount.visibility = View.GONE
        }
        holder.itemView.townName.visibility = View.GONE
//        holder.itemView.youtube_view.initialize(YouTubePlayerInitListener {
//            it.addListener(object : AbstractYouTubePlayerListener(), com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener {
//                override fun onApiChange() {
//                    //it.loadVideo("S0Q4gqBUs7c",0f)
//                }
//
//                override fun onCurrentSecond(second: Float) {
//                 //   it.loadVideo(getVideoId("https://www.youtube.com/watch?v=2gLq4Ze0Jq4"),second)
//                }
//
//                override fun onError(error: Int) {
//                    //it.loadVideo("S0Q4gqBUs7c",0f)
//                }
//
//                override fun onPlaybackQualityChange(playbackQuality: String) {
//                   // it.loadVideo("S0Q4gqBUs7c",0f)
//                }
//
//                override fun onPlaybackRateChange(playbackRate: String) {
//                   // it.loadVideo("S0Q4gqBUs7c",0f)
//                }
//
//                override fun onStateChange(state: Int) {
//
//                    //it.loadVideo("S0Q4gqBUs7c",0f)
//                }
//
//                override fun onVideoDuration(duration: Float) {
//                   // it.loadVideo(getVideoId("https://www.youtube.com/watch?v=2gLq4Ze0Jq4"),duration)
//                }
//
//                override fun onVideoId(videoId: String) {
//                   // it.loadVideo(videoId,0f)
//                }
//
//                override fun onVideoLoadedFraction(loadedFraction: Float) {
//                 //   it.loadVideo(getVideoId("https://www.youtube.com/watch?v=2gLq4Ze0Jq4"),loadedFraction)
//                }
//
//                override fun onReady() {
//                   // it.loadVideo(getVideoId(modelData.video_link),0f)
//                  it.cueVideo(getVideoId(modelData.video_link),0f)
//
//                   // it.loadVideo("2gLq4Ze0Jq4",0f)
//
//                }
//
//            })
//        },true)

        if (list[position].created_by == "1") {
            holder.itemView.name.text = list[position].business_name
            holder.itemView.shared.visibility = View.GONE
        } else {
            holder.itemView.shared.visibility = View.VISIBLE
            holder.itemView.name.text = list[position].business_name
//            var text2 = "(shared)"
//            var span1 = SpannableString(list[position].business_name);
//            span1.setSpan(AbsoluteSizeSpan(32), 0, list[position].business_name.length, SPAN_INCLUSIVE_INCLUSIVE);
//
//            var span2 = SpannableString(text2);
//            span2.setSpan(AbsoluteSizeSpan(22), 0, text2.length, SPAN_INCLUSIVE_INCLUSIVE);
//
//// let's put both spans together with a separator and all
//            var finalText = TextUtils.concat(span1, " ", span2)
//            holder.itemView.name.text = finalText
        }
        try {
            holder.itemView.postText.text = URLDecoder.decode(list[position].description, "UTF-8")
        } catch (e: Exception) {
            holder.itemView.postText.text = list[position].description
        }
        //Constants.setViewMoreLessFunctionality(holder.itemView.postText)
        holder.itemView.postText.setOnClickListener {
            moreless.showMoreLess(position)
        }
        Linkify.addLinks(holder.itemView.postText, Linkify.WEB_URLS)
        holder.itemView.dateTime.text = Constants.getDate2(list[position].updated_at)
        if (!list[position].video_link.trim().isEmpty()) {
            holder.itemView.videoUrl.visibility = View.GONE
            holder.itemView.videoUrl.text = Html.fromHtml("<u>" + list[position].video_link + "</u>")

        } else {
            holder.itemView.videoUrl.visibility = View.GONE
        }
        holder.itemView.videoUrl.setOnClickListener {
            context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(list[position].video_link)))

        }
        holder.itemView.shareFaceBook1.setOnClickListener {
            setClipboard(context, list[position].description, list[position])


        }

        holder.itemView.name.setOnClickListener {

            var townName = Constants.getPrefs(context)!!.getString(Constants.TOWN_NAME, "")
            var logEventN = townName + "-" + Constants.getPrefs(context)!!.getString(Constants.CATEGORY_NAMEO, "") + " - " + list[position].business_name;


            val click = Constants.getPrefs(context!!)!!.getString(Constants.STOPCLICK, "")
            if (click == "no") {
                var mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                val bundle = Bundle()
                bundle.putString("town", Constants.getPrefs(context)!!.getString(Constants.TOWN_NAME, ""))
                bundle.putString("BusinessName", list[position].business_name)
                mFirebaseAnalytics.logEvent(logEventN, bundle)

                val intent = Intent(context, AboutBusinessActivity::class.java)
                intent.putExtra("busId", list[position].bussness_id)
                intent.putExtra("showallpost", "no")
                intent.putExtra("from", "homeadapter")
                intent.putExtra("busName", list[position].business_name)
                intent.putExtra("subscriptionType", list[position].user_type)
                intent.putExtra("show", "hideTaxi")
                context!!.startActivity(intent)
            }
        }
        val click = Constants.getPrefs(context!!)!!.getString(Constants.STOPCLICK, "")
        if (click == "no") {
            holder.itemView.infoLay.visibility = View.VISIBLE
            holder.itemView.menu.visibility = View.VISIBLE
        } else {
            holder.itemView.infoLay.visibility = View.INVISIBLE
            holder.itemView.menu.visibility = View.INVISIBLE
        }
        holder.itemView.infoLay.setOnClickListener {

            var townName = Constants.getPrefs(context)!!.getString(Constants.TOWN_NAME, "")
            var logEventN = townName + "-" + Constants.getPrefs(context)!!.getString(Constants.CATEGORY_NAMEO, "") + " - " + list[position].business_name;


            val click = Constants.getPrefs(context!!)!!.getString(Constants.STOPCLICK, "")
            if (click == "no") {
                var mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                val bundle = Bundle()
                bundle.putString("town", Constants.getPrefs(context)!!.getString(Constants.TOWN_NAME, ""))
                bundle.putString("BusinessName", list[position].business_name)
                mFirebaseAnalytics.logEvent(logEventN, bundle)

                val intent = Intent(context, AboutBusinessActivity::class.java)
                intent.putExtra("busId", list[position].bussness_id)
                intent.putExtra("showallpost", "no")
                intent.putExtra("from", "homeadapter")
                intent.putExtra("busName", list[position].business_name)
                intent.putExtra("subscriptionType", list[position].user_type)
                intent.putExtra("show", "hideTaxi")
                context!!.startActivity(intent)
            }
        }

        try {
            if (list[position].video_link.startsWith("http")) {
                // mVideoPlayerManager.playNewVideo(null, holder.itemView.video_player_1, modelData.video_link)
                holder.itemView.name.videoUrl.visibility = View.VISIBLE
                holder.itemView.name.videoUrl.text = list[position].video_link
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }



        if (context != null) {
            /*Glide.with(context).load(modelData.image_url)
                    .apply(RequestOptions()
                            .override(180, 100).dontAnimate())
                    .into(holder.itemView.postImage)*/
            if (list[position].image_url.equals("")) {
                holder.itemView.postImage.visibility = View.GONE
            } else {
                holder.itemView.postImage.visibility = View.VISIBLE
                Glide.with(context)
                        .load(list[position].image_url)
                        .into(holder.postImage)
            }
            holder.postImage.setOnClickListener {

                Constants.showImagePreview(list[position].image_url, context)
            }
            if (list[position].video_link.equals("")) {
                holder.itemView.videoLayout.visibility = View.GONE
                // holder.itemView.youLay.visibility = View.GONE
            } else {
                holder.itemView.videoLayout.visibility = View.VISIBLE
                var crThumb = context.getContentResolver()

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
//                Glide.with(context)
//                        .asBitmap()
//                        .load(modelData.video_link)
//                        .into(holder.itemView.play);
                //                Glide.with(context)
//                        .load(ThumbnailUtils.createVideoThumbnail(modelData.video_link, MediaStore.Video.Thumbnails.MINI_KIND))
//                        .apply(RequestOptions()
//
//                                .placeholder(R.mipmap.placeholder))
//                        .into(holder.itemView.play)

                var videoId = extractYoutubeVideoId(list[position].video_link)
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
                        var thumb: Long = (position * 1000).toLong()
                        var options = RequestOptions().frame(thumb);
                        Glide.with(context).load(list[position].video_link).apply(options).into(holder.itemView.thumbNail);

                        if (list[position].video_link.contains(".MOV")
                                || list[position].video_link.contains(".flv")
                                || list[position].video_link.contains(".wmv")
                                || list[position].video_link.contains("facebook")) {
                            holder.itemView.textFb.visibility = View.VISIBLE
                            holder.itemView.textFb.text = list[position].video_link
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
            holder.itemView.videoLayout.setOnClickListener {
                //                var text = holder.itemView.videoUrl.text.toString()
//                if (URLUtil.isValidUrl(text)) {
                //     context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelData.video_link)))

                // startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")))
//                }

//            val intent = Intent(context, VideoLinkPlayerActivity::class.java)
//            intent.putExtra("link",modelData.video_link)
//            context.startActivity(intent)
            }

        }

        if (list[position].ftype == "1") {
            holder.itemView.header.visibility = View.VISIBLE
            if (list[position].expired == "1") {
                holder.itemView.relativeLayout2.visibility = View.GONE
                holder.itemView.claimButton.visibility = View.GONE
                if (list[position].redeemed.isEmpty() || list[position].redeemed == "0") {
                    holder.itemView.header.text = "***Expired***"
                } else {
                    //holder.itemView.header.text = "***Expired ${modelData.redeemed} offer(s) claimed***"
                    holder.itemView.header.text = "*Expired ${list[position].redeemed} offer(s) claimed* " + findExpirey2(list[position].validity_date).replace("-", "") + " ago"
                }
                holder.itemView.validityText.text = ""
                holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.expire_red))
            } else {
                //holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.lightGreen))
                holder.itemView.header.background = context.resources.getDrawable(R.drawable.shape_light_green)

                holder.itemView.relativeLayout2.visibility = View.VISIBLE
                holder.itemView.header.text = "***Flash Alert Sale***"
                val left: Int = list[position].max_redemption.toInt() - list[position].redeemed.toInt()
                if (left == 0) {
                    holder.itemView.relativeLayout2.visibility = View.GONE
                    holder.itemView.claimButton.visibility = View.GONE
                    if (list[position].redeemed.isEmpty() || list[position].redeemed == "0") {
                        holder.itemView.header.text = "***Expired***"
                    } else {
                        // holder.itemView.header.text = "***Expired ${modelData.redeemed} offer(s) claimed***"
                        holder.itemView.header.text = "*Expired ${list[position].redeemed} offer(s) claimed* " + findExpirey2(list[position].validity_date).replace("-", "") + " ago"
                    }
                    holder.itemView.validityText.text = ""
                    holder.itemView.header.setBackgroundColor(context.resources.getColor(R.color.expire_red))
                } else {
                    holder.itemView.claimButton.visibility = View.VISIBLE
                    holder.itemView.validityText.text = "Hurry Expires in " + findExpirey(list[position].validity_date) + " - Only " + left + " left"
                }

            }

        } else {
            holder.itemView.header.visibility = View.GONE
            holder.itemView.relativeLayout2.visibility = View.GONE
        }

        holder.itemView.claimButton.setOnClickListener {
            dialogForClaim(list[position], position)
        }
        if (list[position].redeemed.toInt() > 0) {
            holder.itemView.desc.visibility = View.VISIBLE
            holder.itemView.desc.setText(list[position].redeemed + " Offers successfully Claimed. To redeem visit the business and mention your name and email address.")
        } else {
            holder.itemView.desc.visibility = View.GONE
        }
    }

    private fun hitService(post_id: String) {

        var auth_code = Constants.getPrefs(context)?.getString(Constants.AUTH_CODE, "")!!
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.bumpPost(auth_code, post_id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //  view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            Toast.makeText(context, `object`.optString("msg"), Toast.LENGTH_LONG).show()

                        } else {

                            Toast.makeText(context, "Unable to bump the post.", Toast.LENGTH_LONG).show()
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                view.hideLoader()
//                view.showNetworkError(R.string.network_error)
            }
        })
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

    private fun findExpirey2(validity_date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val cal = Calendar.getInstance()
        System.out.println("time => " + dateFormat.format(cal.time))

        val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val date = sdf.parse(validity_date)
        val date2 = sdf.parse(dateFormat.format(cal.time))
        val diff = date2.time - date.time
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
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() + "d " + (TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) - 24 * TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)).toString() + "hrs"
    }

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
        val mSpinner = mAlertDialog.findViewById<Spinner>(R.id.offerClaimed)
        //--------------max no of claims per person spinner-------------------------------------
        val array = ArrayList<String>()
        for (i in 1 until modelData.per_person_redemption.toInt() + 1) {
            array.add("" + i)
        }
        val adapter = ArrayAdapter<String>(
                context, R.layout.simple_spinner_item, array)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        mSpinner!!.adapter = adapter
        mSpinner.setSelection(0)
        mSpinner.prompt = "Select Claims"
        //--------------------------------------------------------------------------------------

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
            if (phoneNo!!.text.toString().isEmpty() || phoneNo!!.text.toString().length < 10) {
                Toast.makeText(context, "Please enter 10 digit phone number.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //update.claimPostService(modelData,name!!.text.toString(),email!!.text.toString())
            var modelc = ModelClainService()
            modelc.postId = modelData.id
            modelc.name = name.text.toString()
            modelc.email = email.text.toString()
            modelc.phoneNo = phoneNo!!.text.toString()
            modelc.position = position.toString()
            modelc.perPerson = mSpinner.selectedItem.toString()
            Constants.getBus().post(ClaimPostLiveFeed(modelc))
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
        val bump = itemView.findViewById<ImageView>(R.id.bump)
        val bump1 = itemView.findViewById<ImageView>(R.id.bump1)
        var myVideo: VideoView = itemView.findViewById(R.id.myVideo)
        var infoLay: LinearLayout = itemView.findViewById(R.id.infoLay)
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

    interface ShowViewMoreLess {
        fun showMoreLess(postion: Int)
    }
}
