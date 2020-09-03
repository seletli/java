package com.sametseletli.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    EditText commentText;
    ImageView imageView;
    Bitmap chosenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        commentText=findViewById(R.id.upload_activity_editText);
        imageView=findViewById(R.id.upload_activity_imageview);

    }

    public void upload(View view)
    {

        String comment=commentText.getText().toString();

        //Image kaydetme

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        chosenImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        //byte dizisi olmalı çünkü parse bizden byte dizisi şeklinde image istiyor.

        ParseFile parseFile=new ParseFile("image.png",bytes);

        ParseObject object=new ParseObject("Post");
        object.put("image",parseFile);
        object.put("Comment",comment);
        object.put("UserName", ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e!=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Post Uploaded!",Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
                   startActivity(intent);

                }
            }
        });

    }

    public  void chooseImage(View view)
    {
        //Bellek ulaşım izni


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);

        }
        else
            {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent,1);
            }
    }

    //izin sonrası yapılacak sey.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==2)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED);
            {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent,1);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //Resim seçildikten sonra yapılacak
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==RESULT_OK && data!=null)
        {
            Uri uri=data.getData();
            try {
                //Yeni versionlar için
                if(Build.VERSION.SDK_INT>=28)
                {
                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),uri);
                    chosenImage=ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(chosenImage);
                }
                //eski versiyonlar için
                else {
                    chosenImage= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    imageView.setImageBitmap(chosenImage);
                }

            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
