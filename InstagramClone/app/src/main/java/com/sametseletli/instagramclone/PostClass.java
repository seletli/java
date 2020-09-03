package com.sametseletli.instagramclone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String>
{
    private  final ArrayList<String> userName;
    private  final ArrayList<String> userComment;
    private  final ArrayList<Bitmap> userImage;
    private  final Activity context;

    public PostClass(ArrayList<String> userName,ArrayList<String> userComment,ArrayList<Bitmap> userImage,Activity context)
    {
        //custom_view bağladım.
        super(context,R.layout.custom_view,userName);
     this.userName=userName;
     this.userComment=userComment;
     this.userImage=userImage;
     this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //custom view objeleştirdim.
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.custom_view,null,true);

        TextView userNameText=customView.findViewById(R.id.custom_view_usernameText);
        TextView commentText=customView.findViewById(R.id.custom_view_commentText);
        ImageView imageView=customView.findViewById(R.id.custom_view_imageView);

        userNameText.setText(userName.get(position));
        imageView.setImageBitmap(userImage.get(position));
        commentText.setText(userComment.get(position));


        return customView;
    }
}
