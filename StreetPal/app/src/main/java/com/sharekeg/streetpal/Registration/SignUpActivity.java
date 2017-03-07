package com.sharekeg.streetpal.Registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sharekeg.streetpal.Androidversionapi.ApiInterface;
import com.sharekeg.streetpal.Data.UsersInfoForSignUp;
import com.sharekeg.streetpal.Login.LoginActivity;
import com.sharekeg.streetpal.R;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView IV_profile;
    private Button btnSignUp, btn_UploadNationalCard;
    Retrofit retrofit;
    private Spinner spinner;
    private String email, userName, phone, age, nationalId, gender, work, password;
    View focusView = null;
    ApiInterface apiInterface;
    private ProgressDialog pDialog;
    private EditText etEmail, etGender, etUserName, etPhone, etAge, etNationalId, etWork, etPassword;
    private static String fileNationaPhotoPath = null;
    private static String fileProfiePhotoPath = null;
    private static final int REQUEST_TAKE_National_Id_PHOTO = 1;
    private static int RESULT_LOAD_National_Id_IMAGE = 1;

    private TextView tvback, TV_upload;
    private int REQUEST_TAKE_profile_PHOTO = 2;
    private int RESULT_LOAD_profile_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // object of retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        etEmail = (EditText) findViewById(R.id.etemail);
        etAge = (EditText) findViewById(R.id.etage);
        etWork = (EditText) findViewById(R.id.etWork);
        etAge = (EditText) findViewById(R.id.etage);
        etPassword = (EditText) findViewById(R.id.TV_passward);
        etUserName = (EditText) findViewById(R.id.etuserName);
        etPhone = (EditText) findViewById(R.id.etphone);
        etNationalId = (EditText) findViewById(R.id.etnationalid);
        etGender = (EditText) findViewById(R.id.TV_sex);
        IV_profile = (ImageView) findViewById(R.id.IV_profile);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        TV_upload = (TextView) findViewById(R.id.TV_upload);
        tvback = (TextView) findViewById(R.id.tvback);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btn_UploadNationalCard = (Button) findViewById(R.id.btn_UploadNationalCard);

        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                attemptSignUp();
                // pDialog.show();



            }
        });
        btn_UploadNationalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Choose Option")
                        .setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_National_Id_PHOTO);


                                } else {
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, RESULT_LOAD_National_Id_IMAGE);

                                }

                            }
                        }).create().show();

            }
        });


        TV_upload
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setTitle("Choose Option")
                                .setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(takePictureIntent, REQUEST_TAKE_profile_PHOTO);


                                        } else {
                                            Intent intent = new Intent(
                                                    Intent.ACTION_PICK,
                                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(intent, RESULT_LOAD_profile_IMAGE);

                                        }

                                    }
                                }).create().show();


                    }
                });


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Sex, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }

    private void attemptSignUp() {

        boolean mCancel = this.signupValidation();
        if (mCancel) {
            focusView.requestFocus();
        } else {
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            Intent i = new Intent(SignUpActivity.this, ConfirmationActivity.class);
            i.putExtra("UserNameWelcomeText", userName);
            startActivity(i);
            pDialog.dismiss();
//            uploadNationalIdPhoto(fileNationaPhotoPath);
//            uploadProfilePhoto(fileProfiePhotoPath);
            //  insertNewUser(email, userName, age, nationalId, work, password, phone, gender);


        }


    }

    private boolean signupValidation() {
        userName = etUserName.getText().toString();
        password = etPassword.getText().toString();
        email = etEmail.getText().toString();
        gender = etGender.getText().toString();
        phone = etPhone.getText().toString();
        age = etAge.getText().toString();
        nationalId = etNationalId.getText().toString();
        work = etWork.getText().toString();
        boolean cancel = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError("Invalid password");
            focusView = etPassword;
            cancel = true;

        }
        if (TextUtils.isEmpty(age)) {
            etAge.setError("Empty age");
            focusView = etEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(gender)) {
            etGender.setError("Empty gender");
            focusView = etEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(work)) {
            etWork.setError("Empty work");
            focusView = etEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(nationalId)) {
            etNationalId.setError("Empty email");
            focusView = etEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Empty phone");
            focusView = etEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(userName)) {
            etUserName.setError("Empty phone");
            focusView = etEmail;
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


    private void insertNewUser(final String email, final String userName, final String age, final String nationalId, final String work, String password, final String phone, final String gender) {

        ApiInterface mApi = retrofit.create(ApiInterface.class);
        Call<UsersInfoForSignUp> mycall = mApi.insertUserinfo(new UsersInfoForSignUp(email, userName, phone, age, nationalId, gender, work, password));
        mycall.enqueue(new Callback<UsersInfoForSignUp>() {
            @Override
            public void onResponse(Call<UsersInfoForSignUp> call, Response<UsersInfoForSignUp> response) {
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UsersInfoForSignUp> call, Throwable t) {
                pDialog.dismiss();

                Snackbar.make(btnSignUp, "Connection Failed!", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attemptSignUp();
                        pDialog.show();
                    }
                }).show();
            }
        });


    }

    private void uploadProfilePhoto(final String fileProfiePhotoPath) {
        File file = new File(fileProfiePhotoPath);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");


        Call<ResponseBody> req = apiInterface.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                Snackbar.make(btnSignUp, "Failed to upload Profile Photo", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        uploadProfilePhoto(fileProfiePhotoPath);
//
//                    }
//                }).show();
            }
        });
    }

    private void uploadNationalIdPhoto(final String fileNationaPhotoPath) {
        File file = new File(fileNationaPhotoPath);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");


        Call<ResponseBody> req = apiInterface.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(SignUpActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Snackbar.make(btnSignUp, "Failed to upload National-Id-Photo", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        uploadNationalIdPhoto(fileNationaPhotoPath);
//                        //pDialog.show();
//                    }
//                }).show();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_National_Id_PHOTO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            fileNationaPhotoPath = cursor.getString(columnIndex);
            cursor.close();


        }
        if (requestCode == RESULT_LOAD_National_Id_IMAGE && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            fileNationaPhotoPath = cursor.getString(columnIndex);
            cursor.close();


        }
        if (requestCode == REQUEST_TAKE_profile_PHOTO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            fileProfiePhotoPath = cursor.getString(columnIndex);
            cursor.close();


        }
        if (requestCode == RESULT_LOAD_profile_IMAGE && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            fileProfiePhotoPath = cursor.getString(columnIndex);
            cursor.close();


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = spinner.getSelectedItem().toString();
        etGender.setText(text);
        etGender.getText();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
