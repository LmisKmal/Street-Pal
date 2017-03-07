package com.sharekeg.streetpal.Androidversionapi;

import com.sharekeg.streetpal.Data.UsersInfoForSignUp;
import com.sharekeg.streetpal.Login.LoginCredentials;
import com.sharekeg.streetpal.userinfoforlogin.UserInfoForLogin;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by MMenem on 2/22/2017.
 */

public interface ApiInterface


{

    @POST("authenticate")
    Call<LoginCredentials> loginWithCredentials(@Body LoginCredentials data);


    @GET("me")
    Call<UserInfoForLogin> getUser(@Header("Authorization") String token);


    @POST("user")
    Call<UsersInfoForSignUp> insertUserinfo(@Body UsersInfoForSignUp usersInfoForSignUp);

    @Multipart
    @POST("me/national-id")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @Multipart
    @POST("me/photo")
    Call<RequestBody> uploadPhoto();
}
