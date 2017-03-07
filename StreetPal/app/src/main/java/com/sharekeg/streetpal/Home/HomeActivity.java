package com.sharekeg.streetpal.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sharekeg.streetpal.Androidversionapi.ApiInterface;
import com.sharekeg.streetpal.R;
import com.sharekeg.streetpal.userinfoforlogin.UserInfoForLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeActivity extends AppCompatActivity {
    private String token;
    private Button btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnEditProfile=(Button)findViewById(R.id.btnedtitprofile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(HomeActivity.this,EditProfileActivity.class);
                startActivity(i);
            }
        });
        Toast.makeText(this, "SignUp Completed!", Toast.LENGTH_SHORT).show();

//        token = getIntent().getExtras().getString("Token");
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.36/v0/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiInterface mApiService = retrofit.create(ApiInterface.class);
//        Call<UserInfoForLogin> mSerivce = mApiService.getUser(token);
//        mSerivce.enqueue(new Callback<UserInfoForLogin>() {
//            @Override
//            public void onResponse(Call<UserInfoForLogin> call, Response<UserInfoForLogin> response) {
//                Toast.makeText(HomeActivity.this, "UserName " + response.body().getEmail(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<UserInfoForLogin> call, Throwable t) {
//                Toast.makeText(HomeActivity.this, "failed", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

}
