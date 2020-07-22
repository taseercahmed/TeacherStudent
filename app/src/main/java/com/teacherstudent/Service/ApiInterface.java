package com.teacherstudent.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Authorization: key=" + "AAAArHBVYN8:APA91bHaQkvmogi-PhVw-iJegbMWD7WBGg-gplCtgZom_4iR5JywXYf3Pn69Rv_UvzFB1ZWjePwfT-FcUjca3u4sjkzo-UwmcZOwLoMWYA4ejvKNgjCwSsptOxapakm4tumo_4bE8Q8Q", "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body RootModel root);
}
