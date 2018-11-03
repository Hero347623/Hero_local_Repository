package socket;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hero.test.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by HERO on 2018/6/10.
 */

public class ImageActivity extends Activity {
    private byte[] data;
    private RequestTools requestTools;
    private MyApplication myApplication;
    private Bitmap bmp = null;
    private ImageButton back;
    private ImageView imageView;
    private ProgressBar progressBar;
    private File file;
    private final int TAG=0x11;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==TAG){
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(bmp);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        back=(ImageButton)findViewById(R.id.bt_back);
        progressBar=findViewById(R.id.progressbar);
        imageView=(ImageView) findViewById(R.id.iv_activity_image);
        myApplication= (MyApplication) getApplication();
        requestTools=new RequestTools(myApplication);
        request();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 请求图片
     */
    private void request() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                file=requestTools.requestMethod("Request Photo");
                Log.i("ImageActivity客户端","值为"+file);

                try {
                    FileInputStream fis = new FileInputStream(file);
                    int size=0;
                    int off=0;
                    data=new byte[10240000];

                    while (true){
                        size=fis.read(data,off,1024);
                        off+=1024;
                        if (size<0)break;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                Message message=new Message();
                message.what=TAG;
                handler.sendMessage(message);
            }
        }).start();
    }

}
