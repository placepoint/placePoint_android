package com.phaseII.placepoint

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Loveleen on 11/6/18.
 */
interface Service {

    @GET("getAppData")
    fun getAppData(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getAppAuth")
    fun getAppAuth(@Field("device_id") device_id: String,
                   @Field("town_id") town_id: String?,
                   @Field("device_type") device_type: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("logout")
    fun logOut(@Field("auth_code") auth_code: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("register")
    fun postRegisterData(@Field("email") email: String,
                         @Field("password") pass: String,
                         @Field("business_name") bName: String,
                         @Field("location") bLoc: String,
                         @Field("category") bCat: String,
                         @Field("auth_code") auth_code: String,
                         @Field("type") type: String,
                         @Field("token") token: String,
                         @Field("coupon") coupon: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("login")
    fun postLoginData(@Field("email") email: String,
                      @Field("password") password: String,
                      @Field("auth_code") auth_code: String?): Call<ResponseBody>

    //    @Multipart
//    @POST("addPostVideo")
//    fun addPost(@Part("auth_code") auth_code: RequestBody?,
//                @Part("width") width: RequestBody?,
//                @Part("height") height: RequestBody?,
//                @Part("description") desc: RequestBody?,
//                @Part("video_link") video_link: RequestBody?,
//                @Part images: MultipartBody.Part?,
//                @Part("image_status") image_status: RequestBody?,
//                @Part("title") title1: RequestBody,
//                @Part("type") type: RequestBody,
//                @Part("day") day: RequestBody,
//                @Part("time") time: RequestBody,
//                @Part("now_status") now_status: RequestBody,
//                @Part("category") category: RequestBody,
//                @Part("ftype") ftype: RequestBody,
//                @Part("max_redemption") max_redemption: RequestBody,
//                @Part("validity_date") validity_date: RequestBody,
//                @Part("validity_time") validity_time: RequestBody,
//                @Part("per_person_redemption") per_person_redemption: RequestBody,
//                @Part upload_video: MultipartBody.Part?): Call<ResponseBody>
    @Multipart
    @POST("addPostVideo_v1")
    fun addPost(@Part("auth_code") auth_code: RequestBody?,
                @Part("width") width: RequestBody?,
                @Part("height") height: RequestBody?,
                @Part("description") desc: RequestBody?,
                @Part("video_link") video_link: RequestBody?,
                @Part images: MultipartBody.Part?,
                @Part("image_status") image_status: RequestBody?,
                @Part("title") title1: RequestBody,
                @Part("type") type: RequestBody,
                @Part("day") day: RequestBody,
                @Part("time") time: RequestBody,
                @Part("now_status") now_status: RequestBody,
                @Part("category") category: RequestBody,
                @Part("ftype") ftype: RequestBody,
                @Part("max_redemption") max_redemption: RequestBody,
                @Part("validity_date") validity_date: RequestBody,
                @Part("validity_time") validity_time: RequestBody,
                @Part("per_person_redemption") per_person_redemption: RequestBody,
                @Part("retail_price") retail_price: RequestBody,
                @Part("sale_price") sale_price: RequestBody,
                @Part("discount_price") discount_price: RequestBody,
                @Part("email_body") email_body: RequestBody,
                @Part upload_video: MultipartBody.Part?): Call<ResponseBody>

// @Multipart
//    @POST("addpost")
//    fun addPost(@Part("auth_code") auth_code: RequestBody?,
//                @Part("width") width: RequestBody?,
//                @Part("height") height: RequestBody?,
//                @Part("description") desc: RequestBody?,
//                @Part("video_link") video_link: RequestBody?,
//                @Part images: MultipartBody.Part?,
//                @Part("image_status") image_status: RequestBody?,
//                @Part("title") title1: RequestBody,
//                @Part("type") type: RequestBody,
//                @Part("day") day: RequestBody,
//                @Part("time") time: RequestBody,
//                @Part("now_status") now_status: RequestBody,
//                @Part("category") category: RequestBody,
//                @Part("ftype") ftype: RequestBody,
//                @Part("max_redemption") max_redemption: RequestBody,
//                @Part("validity_date") validity_date: RequestBody,
//                @Part("validity_time") validity_time: RequestBody,
//                @Part("per_person_redemption") per_person_redemption: RequestBody,
//                @Part upload_video: MultipartBody.Part?): Call<ResponseBody>
//

    @Multipart
    @POST("editSchedulePost")
    fun editSchedulePost(@Part("auth_code") auth_code: RequestBody?,
                         @Part("width") width: RequestBody?,
                         @Part("height") height: RequestBody?,
                         @Part("description") desc: RequestBody?,
                         @Part("video_link") video_link: RequestBody?,
                         @Part images: MultipartBody.Part?,
                         @Part("image_status") image_status: RequestBody?,
                         @Part("post_id") post_id: RequestBody,
                         @Part("type") type: RequestBody,
                         @Part("day") day: RequestBody,
                         @Part("time") time: RequestBody,
                         @Part("category") category: RequestBody,
                         @Part("title") title1: RequestBody,
                         @Part("ftype") ftype: RequestBody,
                         @Part("max_redemption") max_redemption: RequestBody,
                         @Part("validity_date") validity_date: RequestBody,
                         @Part("validity_time") validity_time: RequestBody,
                         @Part("per_person_redemption") per_person_redemption: RequestBody): Call<ResponseBody>


    @FormUrlEncoded
    @POST("getFeeds")
    fun getHomeFeedData(@Field("auth_code") auth_code: String
                        , @Field("town_id") town_id: String,
                        @Field("limit") limit: String,
                        @Field("page") page: String,
                        @Field("category_id") category_id: String,
                        @Field("timeline") timeline: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getAllFeeds")
    fun getALLFeedData(@Field("auth_code") auth_code: String
                       , @Field("town_id") town_id: String,
                       @Field("limit") limit: String,
                       @Field("page") page: String,
                       @Field("type") type: String): Call<ResponseBody>


//    @Multipart
//    @POST("updateBusinessPage")
//    fun updateBusinessPage(@Part("business_name") bus_name: RequestBody?,
//                           @Part("auth_code") auth_code: RequestBody?,
//                           @Part("town_id") town_id: RequestBody?,
//                           @Part("email") email: RequestBody?,
//                           @Part("category_id") category_id: RequestBody?,
//                           @Part images: MultipartBody.Part?,
//                           @Part("image_status") image_status: RequestBody?,
//                           @Part("video_link") video_link: RequestBody?,
//                           @Part("opening_hours") opening_hours: RequestBody?,
//                           @Part("image_count") image_count: RequestBody?,
//                           @Part("address") address: RequestBody?,
//                           @Part("contact_no") contact_no: RequestBody?,
//                           @Part("lat") lat: RequestBody?,
//                           @Part("long") long: RequestBody?,
//                           @Part("oldimages") oldimages: RequestBody?,
//                           @Part("description") bus_desc: RequestBody?,
//                           @Part("business_email") business_email: RequestBody?): Call<ResponseBody>


    @Multipart
    @POST("updateBusinessPage_v1")
    fun updateBusinessPage(@Part("business_name") business_name: RequestBody,
                           @Part("auth_code") auth_code: RequestBody,
                           @Part("town_id") town_id: RequestBody,
                           @Part("email") email: RequestBody,
                           @Part("category_id") category_id: RequestBody,
                           @Part surveyImage: ArrayList<MultipartBody.Part>,
                           @Part("image_status") image_status: RequestBody,
                           @Part("video_link") video_link: RequestBody,
                           @Part("opening_hours") opening_hours: RequestBody,
                           @Part("image_count") image_count: RequestBody,
                           @Part("address") address: RequestBody,
                           @Part("contact_no") contact_no: RequestBody,
                           @Part("lat") lat: RequestBody,
                           @Part("long") long: RequestBody,
                           @Part("description") bus_desc: RequestBody,
                           @Part("oldimages") oldimages: RequestBody,
                           @Part("website") websiteUrl: RequestBody,
                           @Part("business_email") business_email: RequestBody,
                           @Part coverImage: MultipartBody.Part?): Call<ResponseBody>

//   @Multipart
//    @POST("updateBusinessPage")
//    fun updateBusinessPage(@Part("business_name") business_name: RequestBody,
//                           @Part("auth_code") auth_code: RequestBody,
//                           @Part("town_id") town_id: RequestBody,
//                           @Part("email") email: RequestBody,
//                           @Part("category_id") category_id: RequestBody,
//                           @Part surveyImage: ArrayList<MultipartBody.Part>,
//                           @Part("image_status") image_status: RequestBody,
//                           @Part("video_link") video_link: RequestBody,
//                           @Part("opening_hours") opening_hours: RequestBody,
//                           @Part("image_count") image_count: RequestBody,
//                           @Part("address") address: RequestBody,
//                           @Part("contact_no") contact_no: RequestBody,
//                           @Part("lat") lat: RequestBody,
//                           @Part("long") long: RequestBody,
//                           @Part("description") bus_desc: RequestBody,
//                           @Part("oldimages") oldimages: RequestBody,
//                           @Part("business_email") business_email: RequestBody,
//                           @Part coverImage: MultipartBody.Part?): Call<ResponseBody>
//
//    @Multipart
//    @POST("updateBusinessPage")
//    fun updateBusinessPage2(@Part("business_name") business_name: RequestBody,
//                            @Part("auth_code") auth_code: RequestBody,
//                            @Part("town_id") town_id: RequestBody,
//                            @Part("email") email: RequestBody,
//                            @Part("category_id") category_id: RequestBody,
//                            @Part surveyImage: ArrayList<MultipartBody.Part>,
//                            @Part("image_status") image_status: RequestBody,
//                            @Part("video_link") video_link: RequestBody,
//                            @Part("opening_hours") opening_hours: RequestBody,
//                            @Part("image_count") image_count: RequestBody,
//                            @Part("address") address: RequestBody,
//                            @Part("contact_no") contact_no: RequestBody,
//                            @Part("lat") lat: RequestBody,
//                            @Part("long") long: RequestBody,
//                            @Part("description") bus_desc: RequestBody,
//                            @Part("oldimages") oldimages: RequestBody,
//                            @Part("business_email") business_email: RequestBody): Call<ResponseBody>


  @Multipart
    @POST("updateBusinessPage_v1")
    fun updateBusinessPage2(@Part("business_name") business_name: RequestBody,
                            @Part("auth_code") auth_code: RequestBody,
                            @Part("town_id") town_id: RequestBody,
                            @Part("email") email: RequestBody,
                            @Part("category_id") category_id: RequestBody,
                            @Part surveyImage: ArrayList<MultipartBody.Part>,
                            @Part("image_status") image_status: RequestBody,
                            @Part("video_link") video_link: RequestBody,
                            @Part("opening_hours") opening_hours: RequestBody,
                            @Part("image_count") image_count: RequestBody,
                            @Part("address") address: RequestBody,
                            @Part("contact_no") contact_no: RequestBody,
                            @Part("lat") lat: RequestBody,
                            @Part("long") long: RequestBody,
                            @Part("description") bus_desc: RequestBody,
                            @Part("oldimages") oldimages: RequestBody,
                            @Part("website") websiteUrl: RequestBody,
                            @Part("business_email") business_email: RequestBody): Call<ResponseBody>


    @Multipart
    @POST("updateBusinessPage_v1")
    fun updateBusinessPage3(@Part("business_name") bus_name: RequestBody?,
                            @Part("auth_code") auth_code: RequestBody?,
                            @Part("town_id") town_id: RequestBody?,
                            @Part("email") email: RequestBody?,
                            @Part("category_id") category_id: RequestBody?,
                            @Part("image_status") image_status: RequestBody?,
                            @Part("video_link") video_link: RequestBody?,
                            @Part("opening_hours") opening_hours: RequestBody?,
                            @Part("image_count") image_count: RequestBody?,
                            @Part("address") address: RequestBody?,
                            @Part("contact_no") contact_no: RequestBody?,
                            @Part("lat") lat: RequestBody?,
                            @Part("long") long: RequestBody?,
                            @Part("description") bus_desc: RequestBody?,
                            @Part("oldimages") oldimages: RequestBody?,
                            @Part("website") websiteUrl: RequestBody,
                            @Part("business_email") business_email: RequestBody?,
                            @Part coverImage: MultipartBody.Part?): Call<ResponseBody>

// @Multipart
//    @POST("updateBusinessPage")
//    fun updateBusinessPage3(@Part("business_name") bus_name: RequestBody?,
//                            @Part("auth_code") auth_code: RequestBody?,
//                            @Part("town_id") town_id: RequestBody?,
//                            @Part("email") email: RequestBody?,
//                            @Part("category_id") category_id: RequestBody?,
//                            @Part("image_status") image_status: RequestBody?,
//                            @Part("video_link") video_link: RequestBody?,
//                            @Part("opening_hours") opening_hours: RequestBody?,
//                            @Part("image_count") image_count: RequestBody?,
//                            @Part("address") address: RequestBody?,
//                            @Part("contact_no") contact_no: RequestBody?,
//                            @Part("lat") lat: RequestBody?,
//                            @Part("long") long: RequestBody?,
//                            @Part("description") bus_desc: RequestBody?,
//                            @Part("oldimages") oldimages: RequestBody?,
//                            @Part("business_email") business_email: RequestBody?,
//                            @Part coverImage: MultipartBody.Part?): Call<ResponseBody>
//

    @FormUrlEncoded
    @POST("updateBusinessPage_v1")
    fun updateBusinessPage33(@Field("business_name") bus_name: String,
                             @Field("auth_code") auth_code: String,
                             @Field("town_id") town_id: String,
                             @Field("email") email: String,
                             @Field("category_id") category_id: String,
                             @Field("image_status") image_status: String,
                             @Field("video_link") video_link: String,
                             @Field("opening_hours") opening_hours: String,
                             @Field("image_count") image_count: String,
                             @Field("address") address: String,
                             @Field("contact_no") contact_no: String,
                             @Field("lat") lat: String,
                             @Field("long") long: String,
                             @Field("description") bus_desc: String,
                             @Field("oldimages") oldimages: String,
                             @Field("website") websiteUrl: String,
                             @Field("business_email") business_email: String): Call<ResponseBody>

//
//    @FormUrlEncoded
//    @POST("updateBusinessPage")
//    fun updateBusinessPage33(@Field("business_name") bus_name: String,
//                             @Field("auth_code") auth_code: String,
//                             @Field("town_id") town_id: String,
//                             @Field("email") email: String,
//                             @Field("category_id") category_id: String,
//                             @Field("image_status") image_status: String,
//                             @Field("video_link") video_link: String,
//                             @Field("opening_hours") opening_hours: String,
//                             @Field("image_count") image_count: String,
//                             @Field("address") address: String,
//                             @Field("contact_no") contact_no: String,
//                             @Field("lat") lat: String,
//                             @Field("long") long: String,
//                             @Field("description") bus_desc: String,
//                             @Field("oldimages") oldimages: String,
//                             @Field("business_email") business_email: String): Call<ResponseBody>
//

    @FormUrlEncoded
    @POST("getBusinessDetails_v1")
    fun getBusinessDetail(@Field("auth_code") auth_code: String?,
                          @Field("town_id") town_id: String?,
                          @Field("limit") limit: String?,
                          @Field("page") page: String?,
                          @Field("category_id") category_id: String?): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getSingleBusiness_v1")
    fun getSingleBusiness(@Field("auth_code") auth_code: String,
                          @Field("business_id") business_id: String,
                          @Field("mydetail") mydetail: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("forgotPassword")
    fun forgotPassword(@Field("auth_code") auth_code: String,
                       @Field("email") business_id: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("updatePassword")
    fun updatePassword(@Field("auth_code") auth_code: String,
                       @Field("old_password") new: String,
                       @Field("new_password") confirm: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getSchedulePost")
    fun getSchedulePosts(@Field("auth_code") auth_code: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("delete")
    fun deleteSchedule(@Field("auth_code") auth_code: String,
                       @Field("post_id") post_id: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getPackageDetails")
    fun getPackageDetails(@Field("auth_code") auth_code: String,
                          @Field("currenttype") currenttype: String,
                          @Field("upgrade_type") upgrade_type: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("upgrade")
    fun upgrade(@Field("auth_code") auth_code: String,
                @Field("currenttype") currenttype: String,
                @Field("upgrade_type") upgrade_type: String,
                @Field("token") token: String,
                @Field("amount") amount: String): Call<ResponseBody>


    @FormUrlEncoded
    @POST("logout")
    fun logoutApp(@Field("auth_code") auth_code: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("apply_coupon")
    fun CheckCouponValidity(@Field("auth_code") auth_code: String,
                            @Field("coupon") couponID: String,
                            @Field("type") userType: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getFlashPostV1")
    fun getFlashDetail(@Field("auth_code") auth_code: String,
                       @Field("town_id") town_id: String,
                       @Field("category_id") category_id: String,
                       @Field("limit") limit: String,
                       @Field("page") page: String): Call<ResponseBody>

    //    @FormUrlEncoded
//    @POST("claimDeal")
//    fun claimPost(@Field("auth_code") auth_code: String,
//                  @Field("post_id") town_id: String,
//                  @Field("name") name: String,
//                  @Field("phone_no") phone_no: String,
//                  @Field("email") email: String): Call<ResponseBody>
    @FormUrlEncoded
    @POST("claimDeal_v1")
    fun claimPost(@Field("auth_code") auth_code: String,
                  @Field("post_id") town_id: String,
                  @Field("name") name: String,
                  @Field("phone_no") phone_no: String,
                  @Field("email") email: String,
                  @Field("quantity") quantity: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("getClaimedFlashPostList")
    fun getClaimedFlashPostList(@Field("auth_code") auth_code: String,
                                @Field("post_id") town_id: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("changeFlashPostStatus")
    fun changeFlashPostStatus(@Field("auth_code") auth_code: String,
                              @Field("ids") ids: String,
                              @Field("post_id") town_id: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("updateOnesignalid")
    fun updateOnesignalid(@Field("auth_code") auth_code: String,
                          @Field("onesignal_id") onesignal_id: String,
                          @Field("town_id") town_id: String,
                          @Field("category_id") category_id: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("sendEmail")
    fun sendEmail(@Field("auth_code") auth_code: String,
                  @Field("post_id") post_id: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("bumppost")
    fun bumpPost(@Field("auth_code") auth_code: String,
                 @Field("post_id") post_id: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("post_status")
    fun getPostStatus(@Field("auth_code") auth_code: String,
                      @Field("post_id") post_id: String): Call<ResponseBody>


}
