package socket;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.hero.test.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by HERO on 2018/6/10.
 */

public class MediaActivity extends Activity {
    private Socket socket;
    private MyApplication myApplication;
    private ImageButton back;
    private RequestTools requestTools;
    private  MediaPlayer mediaPlayer;
    private File file;
    private final int TAG=0x11;
    private ProgressBar progressBar;
    private Boolean isPlay=true;
    private Button start;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==TAG){
                progressBar.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        progressBar=findViewById(R.id.progressbar);
        back=(ImageButton)findViewById(R.id.bt_back);
        start=(Button) findViewById(R.id.bt_start);

        myApplication= (MyApplication) getApplication();
        requestTools=new RequestTools(myApplication);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });

        request();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay){
                    start.setText("停止");
                    mediaPlayer.pause();
                    isPlay=false;
                }else {
                    start.setText("播放");
                    mediaPlayer.start();
                    isPlay=true;
                }
            }
        });
    }

    /**
     * 请求音频
     */
    private void request(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                file=requestTools.requestMethod("Request Media");
                Log.i("MediaActivity客户端","值为"+file);

                playMedia();
                Message message=new Message();
                message.what=TAG;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 播放音频
     */
    private void playMedia(){
        try {
            mediaPlayer=MediaPlayer.create(this, Uri.parse(file.getAbsolutePath()));
            mediaPlayer.reset();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
