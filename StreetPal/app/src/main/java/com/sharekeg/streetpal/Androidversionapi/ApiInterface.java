package com.sharekeg.streetpal.Androidversionapi;

import com.sharekeg.streetpal.userinfoforeditingprofile.UsersInfoForEditingProfile;
import com.sharekeg.streetpal.userinfoforsignup.UsersInfoForSignUp;
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
    @GET("me")
    Call<UserInfoForLogin> displayUserInfo();

    @POST("user")
    Call<UsersInfoForSignUp> insertUserinfo(@Body UsersInfoForSignUp usersInfoForSignUp);

    @POST("user")
    Call<UsersInfoForEditingProfile> editCurrentUser(@Body UsersInfoForEditingProfile usersInfoForEditingProfile);

    @Multipart
    @POST("me/national-id")
    Call<RequestBody> uploadNationalIdPhoto(@Part("file\"; filename=\"my_image.jpg\" ") RequestBody image);

    @Multipart
    @POST("me/photo")
    Call<RequestBody> uploadprofilePhoto(@Part("file\"; filename=\"my_image.jpg\" ") RequestBody image);
}
