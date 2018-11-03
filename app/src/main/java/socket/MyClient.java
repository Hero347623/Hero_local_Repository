package socket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.hero.test.R;

import java.io.File;
import java.net.Socket;

/**
 * Created by HERO on 2018/6/7.
 */

public class MyClient extends Activity implements View.OnClickListener{
    private Intent intent;
    private MyApplication myApplication;
    private Button btRequest;
    private Button btRequestText;
    private Button btRequestMedia;
    private Button btRequestMovie;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        findViews();
    }
    private void findViews() {
        btRequest=(Button)findViewById(R.id.bt_request);
        btRequestText=(Button)findViewById(R.id.bt_requestText);
        btRequestMedia=(Button)findViewById(R.id.bt_requestMedia);
        btRequestMovie=(Button)findViewById(R.id.bt_requestMovie);

        myApplication= (MyApplication) getApplication();

        btRequest.setOnClickListener(this);
        btRequestText.setOnClickListener(this);
        btRequestMedia.setOnClickListener(this);
        btRequestMovie.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onClick(View v) {
        /**
         * 请求视频部分
         */
        if (v==btRequestMovie){
            intent=new Intent(MyClient.this, VideoActivity.class);
            startActivity(intent);
        }

        /**
         * 请求音频部分
         */
        else if (v==btRequestMedia){
            intent=new Intent(MyClient.this, MediaActivity.class);
            startActivity(intent);
        }


        /**
         * 请求文本部分
         */
        else if (v==btRequestText){
            intent=new Intent(MyClient.this, TextActivity.class);
            startActivity(intent);
        }


        /**
         * 请求图片部分
         * @param v
         */
        else if (v==btRequest){
            intent=new Intent(MyClient.this, ImageActivity.class);
            startActivity(intent);
        }
    }
}
