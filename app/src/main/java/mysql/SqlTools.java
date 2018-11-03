package mysql;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HERO on 2018/6/11.
 */

public class SqlTools {
    private SharedPreferences sharedPreferences;
    private Context context;
    private String type;
    public SqlTools(Context context,String type){
        this.context=context;
        this.type=type;
    };
    public void insert(String isRequestMedia){
        sharedPreferences=context.getSharedPreferences("isRequest",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if (sharedPreferences.getString(type,"n")=="n"){
            editor.putString(type, "false");//写入
        }
        editor.putString(type,isRequestMedia);
        editor.commit();//只有提交才能真正写入文件
    }
    public String  get(){
        sharedPreferences=context.getSharedPreferences("isRequest",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if (sharedPreferences.getString(type,"n")=="n"){
            editor.putString(type, "false");//写入
        }

        editor.commit();//只有提交才能真正写入文件
        return sharedPreferences.getString(type,"n");
    }
}
