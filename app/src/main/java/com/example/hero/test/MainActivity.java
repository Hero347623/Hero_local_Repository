package com.example.hero.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button exit;
    private Button select;
    private int[] imageId;
    private String[] items;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews() {
        exit=findViewById(R.id.bt_exit);
        select=findViewById(R.id.bt_select);

        imageId=new int[]{R.drawable.mygirl,R.drawable.biggirl,R.drawable.teacher};
        items=new String[]{"图片一","图片二","图片三"};

        exit.setOnClickListener(this);
        select.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if (v==exit){
//            AlertDialog dialog=new AlertDialog.Builder(this).
//                    setTitle("退出").setIcon(R.drawable.icon).setMessage("要退出吗？")
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    }).create();
//            dialog.show();
//        }
//        if (v==select){
//            AlertDialog selectDialog=new AlertDialog.Builder(this)
//                    .setTitle("选择一张图片").setIcon(R.drawable.icon)
//                    .setItems(items, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).create();
//        }

    }
}
