package socket;

import android.app.Activity;
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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.hero.test.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by HERO on 2018/6/10.
 */

public class VideoActivity extends Activity {
    private Socket socket;
    private ImageButton back;
    private MyApplication myApplication;
    private RequestTools requestTools;
    private VideoView videoView;
    private MediaController mediaController;
    private File file;
    private final int PLAY=0x12;
    private ProgressBar progressBar;
    /**
     * 播放视频，设置滚动条不可见
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==PLAY){
                progressBar.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(file.getAbsolutePath());
                videoView.setMediaController(mediaController);
                videoView.requestFocus();
                videoView.start();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        progressBar=findViewById(R.id.progressbar);
        back=(ImageButton)findViewById(R.id.bt_back);

        myApplication= (MyApplication) getApplication();
        requestTools=new RequestTools(myApplication);

        videoView=findViewById(R.id.video_player);
        mediaController=new MediaController(VideoActivity.this);
        videoView.setVisibility(View.INVISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        request();
    }

    /**
     * 请求视频
     */
    private void request(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                file=requestTools.requestMethod("Request Movie");
                Log.i("VideoActivity客户端","值为"+file);

                Message message=new Message();
                message.what=PLAY;
                handler.sendMessage(message);
            }
        }).start();
    }
}
