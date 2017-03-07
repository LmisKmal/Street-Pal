package com.sharekeg.streetpal.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharekeg.streetpal.R;

import java.io.File;


public class EditProfileActivity extends AppCompatActivity {
    private EditText userName, mail, phoneNumber,  age, work, password;
    private TextView ETback,sex , nationalID;
    private ImageView settings, changeProfile;
    private Uri selectedImage;
    private String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);


        userName = (EditText) findViewById(R.id.userNameEditTextId);
        mail = (EditText) findViewById(R.id.mailEditTextId);
        phoneNumber = (EditText) findViewById(R.id.phoneNumberEditTextId);
        sex = (TextView) findViewById(R.id.sexEditTextId);
        age = (EditText) findViewById(R.id.ageEditTextId);
        work = (EditText) findViewById(R.id.workEditTextId);
        nationalID = (TextView) findViewById(R.id.nationalIdEditTextId);
        password = (EditText) findViewById(R.id.passwordEditTextId);
        changeProfile = (ImageView) findViewById(R.id.changeProfileImg);
        ETback = (TextView) findViewById(R.id.backTextId);
        settings = (ImageView) findViewById(R.id.settingsImgId);
ETback.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(EditProfileActivity.this,HomeActivity.class);
        startActivity(i);

    }
});

        changeProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();

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
                    File f = new File(Environment
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
    protected void onActivityResult(int requestCode , int resultCode , Intent imageReturnedIntent){
        super.onActivityResult(requestCode ,resultCode , imageReturnedIntent );
        try{
            switch(requestCode) {
                case 1:
                    if(resultCode == RESULT_OK){
                        Uri selectedImage = imageReturnedIntent.getData();

                        changeProfile.setImageURI(selectedImage);
                    }

                    break;
                case 2:
                    if(resultCode == RESULT_OK){
                        Uri selectedImage = imageReturnedIntent.getData();
                        changeProfile.setImageURI(selectedImage);
                    }
                    break;
            }

        }
        catch (Exception e){
            Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show();
        }
    }
}
