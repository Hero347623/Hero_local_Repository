package socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hero.test.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by HERO on 2018/6/10.
 */

public class TextActivity extends Activity {
    private RequestTools requestTools;
    public MyApplication myApplication;
    private ImageButton back;
    private TextView textContent;
    private final int TAG=0x11;
    private String result="";
    private ProgressBar progressBar;
    private File file;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==TAG){
                //显示文本
                progressBar.setVisibility(View.INVISIBLE);
                textContent.setText(result.toString());
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        textContent=(TextView)findViewById(R.id.tv_activity_content);
        back=(ImageButton)findViewById(R.id.bt_back);
        progressBar=findViewById(R.id.progressbar);
        myApplication=(MyApplication) getApplication();

        requestTools=new RequestTools(myApplication);

        request();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void request() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取保存的文件对象
                file=requestTools.requestMethod("Request Text");
                //读取文本
                try {
                    FileInputStream fis = new FileInputStream(file);
                    int size=0;
                    String temp;
                    StringBuffer stringBuffer=new StringBuffer();
                    byte[] data=new byte[1024];
                   while (true){
                       size=fis.read(data,0,1024);
                       if (size<0)break;
                       temp=new String(data,"UTF-8");
                       result+=temp;
                   }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.what=TAG;
                handler.sendMessage(message);
            }
        }).start();
    }


}
