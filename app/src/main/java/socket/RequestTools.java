package socket;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created by HERO on 2018/6/11.
 */

public class RequestTools {
    private Socket socket;
    private int size=0;//读取的数据长度
    private int order=0;//数据块序列号
    private int[] array=new int[102400];//用于放置已接收到的数据块的序列号
    private byte[] bufferReader;
    private String strISSame="true";//客户端给服务端的反馈信息
    private byte[] bytes1;//用于放置解析后的数据
    private int length=0;//数据块的实际长度
    private File file;//被打开的文件对象
    private File saveFile;//保存的文件路径
    private String typeName="mp3";//文件类型名,默认为MP3
    private MyApplication myApplication;
    //输入输出流
    private DataOutputStream dos;
    private DataInputStream dis;
    //文件输出流
    private FileOutputStream outStream;
    public RequestTools(MyApplication myApplication){
        this.myApplication=myApplication;
    }

    /**
     * 请求方法
     */
    public File requestMethod(String typeName) {
        try {
            //获取服务端的地址，数据将传送到该服务端
            socket = new Socket(myApplication.getIpAddress(), myApplication.getEncode());
            DataOutputStream os = new DataOutputStream(
                    socket.getOutputStream());
            //请求获取文本
            if (typeName.equalsIgnoreCase("Request Text")){
                os.write("Request Text".getBytes());
                os.flush();
                getText();
            }
            //请求获取图片
            else  if (typeName.equalsIgnoreCase("Request Photo")){
                os.write("Request Photo".getBytes());
                os.flush();
                getPhoto();
            }
            //请求获取音频
            else  if (typeName.equalsIgnoreCase("Request Media")){
                os.write("Request Media".toString().getBytes());
                os.flush();

                getMedia();
            }
            //请求获取视频
            else  if (typeName.equalsIgnoreCase("Request Movie")){
                os.write("Request Movie".getBytes());
                os.flush();
                getMyMovie();
            }
            os.close();
            socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return file;

    }

    /**
     * 获取视频
     */
    private void getMyMovie() {
        Log.i("客户端：","尝试接收视频");
        try {
            readData();
            Log.i("客户端","写入完毕1");

            Log.i("客户端","尝试播放视频");

            file = new File("/mnt/sdcard/"+typeName);
            dos.close();
            outStream.close();
        } catch (IOException e) {
            Log.i("客户端：","接收视频失败");
            e.printStackTrace();
        }
    }

    /**
     * 获取音频
     */
    private void getMedia() {
        Log.i("客户端：","尝试接收音频");
        try {
            //从服务端读取数据
            readData();
            Log.i("客户端","写入完毕1");
            Log.i("客户端","尝试播放音频");

            file = new File("/mnt/sdcard/"+typeName);
            dos.close();
            dis.close();
            outStream.close();
            socket.close();
        }catch (EOFException eofException){
            Log.i("客户端：","读取到尾部");
        }
        catch (IOException e) {
            Log.i("客户端：","接收音频失败");
            e.printStackTrace();
        }

    }



    /**
     * 获取图片
     */
    private void getPhoto() {
        try {
            //从服务端读取数据
            readData();
            Log.i("客户端","写入完毕1");
            file = new File("/mnt/sdcard/"+typeName);
            dos.close();
            dis.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            Log.i("客户端","输入输出流错误");
            e.printStackTrace();
        }
    }
    /**
     * 获取文本
     */
    private void getText( ) {
        try {
//            File sdCardDir = Environment.getExternalStorageDirectory();
//            File saveFile = new File(sdCardDir, "MyText.txt");
//            outStream = new FileOutputStream(saveFile);
//            //从服务端读取数据
//            dis=new DataInputStream(socket.getInputStream());
//
//            byte[] bytes=new byte[1024];
//            int data=0;
//            while (true){
//                data=dis.read(bytes,0,1024);
//                if (data<0) break;
//                outStream.write(bytes,0,data);
//            }
            readData();
            Log.i("客户端","写入完毕1");
            //文件的存储路径
            file = new File("/mnt/sdcard/MyText.txt");

            dis.close();
            outStream.close();
            socket.close();
            Log.i("客户端","文本写入完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务端读取数据
     * @throws IOException
     */
    private void readData() throws IOException {
        dos = new DataOutputStream(
                socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());

        boolean isOut=true;//用于判断是否第一次执行，是就打开输出文件
        int i=1;
        while (true) {
            bufferReader=new byte[1413];
            size = dis.read(bufferReader,0,1413);
            Log.i("客户端","size="+size);
            if (size<0) break;
            //拆包检验
            processData(bufferReader);
            /**
             *根据数据块的类型决定输出文件的名字
             * 只执行一次
             */
            if (isOut){
                saveFile = new File(Environment.getExternalStorageDirectory(), typeName);
                outStream = new FileOutputStream(saveFile);
                isOut=false;
            }
            if (strISSame.equalsIgnoreCase("true")){
                Log.i("客户端",strISSame);
                Log.i("客户端","写入了");
                //如果该数据块的序列号是新的，就写入文件中
                if (isInArray(order)){
                    outStream.write(bytes1);
                    outStream.flush();
                }
                dos.write(strISSame.getBytes());
                dos.flush();
            }else if (strISSame.equalsIgnoreCase("false")){
                Log.i("客户端",strISSame);
                dos.write(strISSame.getBytes());
                dos.flush();
            }
        }
    }

    /**
     *拆包检验
     * @param bufferReader
     */
    private void processData(byte[] bufferReader) {
        byte[] typeByte=new byte[4];
        byte[] lengthByte=new byte[4];
        byte[] index=new byte[4];
        System.arraycopy(bufferReader,0,typeByte,0,4);
        System.arraycopy(bufferReader,4,lengthByte,0,4);
        System.arraycopy(bufferReader,8,index,0,4);

        int type=ByteArrayToInt2(typeByte);
        //判断文件类型及格式
        typeName=switchType(type);
        length=ByteArrayToInt2(lengthByte);
        order=ByteArrayToInt2(index);
        Log.i("客户端","length="+length+",type="+type+",order="+order);

        if (length<0||length>1413||type<0||order<0||order>102400){
            strISSame="false";
            return;
        }

        bytes1=new byte[length];
        System.arraycopy(bufferReader,12,bytes1,0,bytes1.length);
        byte b=xor(bytes1);
        if (bufferReader[12+length]==b){
            strISSame="true";
        }else {
            strISSame="false";
            return;
        }
    }

    /**
     * 判断数据块的类型（音频、图片、视频的格式）
     * @param type
     * @return
     */
    private String switchType(int type) {
        String value="";
        switch (type){
            /**
             * 音频部分
             */
            case 65:
                value="music.mp3";
                break;
            case 66:
                value="music.wav";
                break;
            case 67:
                value="music.flac";
                break;
            /**
             * 图片部分
             */
            case 71:
                value="photo.jpg";
                break;
            case 72:
                value="photo.gif";
                break;
            case 73:
                value="photo.png";
                break;
            /**
             * 视频部分
             */
            case 81:
                value="video.mp4";
                break;
            case 82:
                value="video.flv";
                break;
            case 83:
                value="video.mkv";
                break;
            case 84:
                value="video.avi";
                break;
            case 100:
                value="MyText.txt";
                break;
        }
        return value;
    }

    /**
     * 求解异或码
     * @param bufferRead1
     */
    private static byte xor(byte[] bufferRead1) {
        byte b=bufferRead1[0];
        for (int i=1;i<bufferRead1.length;i++){
            b= (byte) (b^bufferRead1[i]);
        }
        return b;
    }
    /**
     * 判断数据块的序列号是否已经存在
     * @param order
     * @return
     */
    private boolean isInArray(int order){
        for (int i=0;i<array.length;i++){
            if (array[i]==order){
                return false;
            }
        }
        array[order]=order;
        return true;
    }

    /**
     * 辅助方法
     * byte转int
     * @param bArr
     * @return
     */
    public int ByteArrayToInt2(byte[] bArr) {
        if(bArr.length!=4){
            return -1;
        }
        return (int) ((((bArr[0] & 0xff) << 24)
                | ((bArr[1] & 0xff) << 16)
                | ((bArr[2] & 0xff) << 8)
                | ((bArr[3] & 0xff) << 0)));
    }

}
