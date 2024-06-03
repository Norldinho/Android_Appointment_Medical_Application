package com.example.mkjli.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mkjli.ui.chat.UserModel;
import com.google.firebase.firestore.auth.User;

public class AndroidUtil {

    public static  void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void paaUserModelAsIntent1(Intent intent, UserId model){
        intent.putExtra("koki",model.getId_patient());
    }
    public static void passUserModelAsIntent(Intent intent, UserId model){
        intent.putExtra("username",model.getId_medcin());

    }

    public static UserId getUserModelFromIntent(Intent intent){
        UserId userId = new UserId();
        userId.setId_medcin(intent.getStringExtra("username"));

        return userId;
    }

    public static UserId getUserModelFromIntent11(Intent intent){
        UserId userId = new UserId();
        userId.setId_patient((intent.getStringExtra("koki")));
        return userId;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}