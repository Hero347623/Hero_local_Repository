package socket;

import android.app.Application;

/**
 * Created by HERO on 2018/6/11.
 */

public class MyApplication extends Application {
    private String ipAddress;
    private Boolean isRequestMedia;
    private Boolean isRequestVideo;
    private int encode;
    @Override
    public void onCreate() {
        super.onCreate();
        setIpAddress("192.168.1.4");
        setEncode(1251);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getRequestMedia() {
        return isRequestMedia;
    }

    public void setRequestMedia(Boolean requestMedia) {
        isRequestMedia = requestMedia;
    }

    public Boolean getRequestVideo() {
        return isRequestVideo;
    }

    public void setRequestVideo(Boolean requestVideo) {
        isRequestVideo = requestVideo;
    }

    public int getEncode() {
        return encode;
    }

    public void setEncode(int encode) {
        this.encode = encode;
    }
}
