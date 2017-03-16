package com.sharekeg.streetpal.Home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharekeg.streetpal.Androidversionapi.ApiInterface;
import com.sharekeg.streetpal.R;
import com.sharekeg.streetpal.userinfoforeditingprofile.UsersInfoForEditingProfile;
import com.sharekeg.streetpal.userinfoforlogin.UserInfoForLogin;
import com.sharekeg.streetpal.userinfoforsignup.UsersInfoForSignUp;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditProfileActivity extends AppCompatActivity {
    private EditText etUserName, etEmail, etPhoneNumber, etAge, etWork, etPassword;
    private TextView tvBack, tvSex, tvNationalId;
    private ImageView ivSettings, ivChanceProfilePicture;
    private String mediaPath, email, userName, phone, age, work, password;
    View focusView = null;
    ApiInterface apiInterface;
    private ProgressDialog pDialog;
    private Retrofit retrofit;
    private String fileProfiePhotoPath;
    private Uri selectedImage;
    private File f;
    private String token;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.27.27.86/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        etUserName = (EditText) findViewById(R.id.userNameEditTextId);
        etEmail = (EditText) findViewById(R.id.mailEditTextId);
        etPhoneNumber = (EditText) findViewById(R.id.phoneNumberEditTextId);
        tvSex = (TextView) findViewById(R.id.sexEditTextId);
        etAge = (EditText) findViewById(R.id.ageEditTextId);
        etWork = (EditText) findViewById(R.id.workEditTextId);
        tvNationalId = (TextView) findViewById(R.id.nationalIdEditTextId);
        etPassword = (EditText) findViewById(R.id.passwordEditTextId);
        ivChanceProfilePicture = (ImageView) findViewById(R.id.changeProfileImg);
        tvBack = (TextView) findViewById(R.id.backTextId);
        ivSettings = (ImageView) findViewById(R.id.settingsImgId);


//        token = getIntent().getExtras().getString("Token");
                ApiInterface mApiService = retrofit.create(ApiInterface.class);
        Call<UserInfoForLogin> mSerivce = mApiService.displayUserInfo();
        mSerivce.enqueue(new Callback<UserInfoForLogin>() {
            @Override
            public void onResponse(Call<UserInfoForLogin> call, Response<UserInfoForLogin> response) {
                etEmail.setText(response.body().getEmail());
                etUserName.setText(response.body().getName().getFirst());
                tvSex.setText(response.body().getGender());




            }

            @Override
            public void onFailure(Call<UserInfoForLogin> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("Save Changes")
                        .setMessage("Are you sure you want to save the changes ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                attemptEditProfile();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(EditProfileActivity.this, HomeActivity.class);
                                startActivity(i);

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();



            }
        });

        ivChanceProfilePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();

            }
        });


    }

    private void attemptEditProfile() {


        boolean mCancel = this.editProfileValidation();
        if (mCancel) {
            focusView.requestFocus();
        } else {
            pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


            uploadProfilePhoto(fileProfiePhotoPath);

            editCurrentUser(email, userName, age, work, password, phone);


        }


    }

    private void uploadProfilePhoto(final String fileProfiePhotoPath) {


        File file = new File(fileProfiePhotoPath);
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), file);

// Synchronous.
        try {
            Response<RequestBody> response = apiInterface.uploadprofilePhoto(photoBody).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

// Asynchronous.
        apiInterface.uploadprofilePhoto(photoBody).enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                                Intent i = new Intent(EditProfileActivity.this, HomeActivity.class);
                startActivity(i);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {

                Snackbar.make(etPassword, "Failed to upload profilePhoto", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadProfilePhoto(fileProfiePhotoPath);
                        pDialog.show();
                    }
                }).show();
            }
        });



    }


    private boolean editProfileValidation() {


        userName = etUserName.getText().toString();
        password = etPassword.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhoneNumber.getText().toString();
        age = etAge.getText().toString();
        work = etWork.getText().toString();
        boolean cancel = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError("Invalid password");
            focusView = etPassword;
            cancel = true;

        }
        if (TextUtils.isEmpty(age)) {
            etAge.setError("Empty age");
            focusView = etAge;
            cancel = true;
        }

        if (TextUtils.isEmpty(work)) {
            etWork.setError("Empty work");
            focusView = etWork;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhoneNumber.setError("Empty phone");
            focusView = etPhoneNumber;
            cancel = true;
        }
        if (TextUtils.isEmpty(userName)) {
            etUserName.setError("Empty UserName");
            focusView = etUserName;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Empty username");
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError("Invalid email");
            focusView = etEmail;
            cancel = true;
        }
        return cancel;
    }


    private boolean isEmailValid(String email) {

        return email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 6;
    }


    private void editCurrentUser(final String email, final String userName, final String age, final String work, String password, final String phoneNumber) {

        ApiInterface mApi = retrofit.create(ApiInterface.class);
        Call<UsersInfoForEditingProfile> mycall = mApi.editCurrentUser(new UsersInfoForEditingProfile(email, userName, phoneNumber, age, work, password));
        mycall.enqueue(new Callback<UsersInfoForEditingProfile>() {
            @Override
            public void onResponse(Call<UsersInfoForEditingProfile> call, Response<UsersInfoForEditingProfile> response) {
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UsersInfoForEditingProfile> call, Throwable t) {
                pDialog.dismiss();

                Snackbar.make(etPassword, "Connection Failed!", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attemptEditProfile();
                        pDialog.show();
                    }
                }).show();
            }
        });

    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                EditProfileActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        fileProfiePhotoPath = cursor.getString(columnIndex);
                        cursor.close();
                        selectedImage = imageReturnedIntent.getData();

                        ivChanceProfilePicture.setImageURI(selectedImage);
                    }

                    break;
                case 2:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        fileProfiePhotoPath = cursor.getString(columnIndex);
                        cursor.close();
                        selectedImage = imageReturnedIntent.getData();
                        ivChanceProfilePicture.setImageURI(selectedImage);
                    }
                    break;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show();
        }
    }


}
