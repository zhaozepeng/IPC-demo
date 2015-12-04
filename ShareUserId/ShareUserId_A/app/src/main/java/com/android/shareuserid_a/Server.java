package com.android.shareuserid_a;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.libcore.log.L;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.shareduserid_a.R;

import java.lang.reflect.InvocationTargetException;

/**
 * Description: 服务器端
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-04
 */
public class Server extends BaseActivity implements View.OnClickListener{

    private TextView tv_context;
    private Button btn_pic;
    private ImageView iv_pic;
    private Button btn_string;
    private TextView tv_string;
    private Button btn_open;
    private Button btn_method;
    private TextView tv_sum;
    private Button btn_get_preference;
    private TextView tv_shared_preference;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server);
        tv_context = (TextView) findViewById(R.id.tv_context);
        btn_pic = (Button) findViewById(R.id.btn_pic);
        btn_pic.setOnClickListener(this);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        btn_string = (Button) findViewById(R.id.btn_string);
        btn_string.setOnClickListener(this);
        btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(this);
        tv_string = (TextView) findViewById(R.id.tv_string);
        btn_method = (Button) findViewById(R.id.btn_method);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        btn_method.setOnClickListener(this);
        btn_get_preference = (Button) findViewById(R.id.btn_get_preference);
        btn_get_preference.setOnClickListener(this);
        tv_shared_preference = (TextView) findViewById(R.id.tv_shared_preference);
        getContext();
    }

    private void getContext(){
        try {
            context = createPackageContext("com.android.shareduserid_b", CONTEXT_INCLUDE_CODE|CONTEXT_IGNORE_SECURITY);
            tv_context.setText(context.toString());
        } catch (PackageManager.NameNotFoundException e) {
            tv_context.setText("context 为空");
            L.e(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pic:
                //获取b应用图片
                iv_pic.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.share));
                break;
            case R.id.btn_string:
                tv_string.setText(context.getString(R.string.share_string));
                break;
            case R.id.btn_open:
                ComponentName componentName = new ComponentName("com.android.shareduserid_b",
                        "com.android.shareuserid_b.Client");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
                break;
            case R.id.btn_method:
                try {
                    Class clazz = context.getClassLoader().loadClass("com.android.shareuserid_b.Method");
                    Object object = clazz.newInstance();
                    int[] ints = new int[]{1,2,3};
                    int sum = (int) clazz.getMethod("add", int[].class).invoke(object, ints);
                    tv_sum.setText("sum is :"+sum);
                } catch (Exception e) {
                    L.e(e);
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_preference:
                //注意Context.MODE_MULTI_PROCESS不可靠
                SharedPreferences sharedPreferences = context.getSharedPreferences("permanent", MODE_MULTI_PROCESS);
                String time = sharedPreferences.getString("time", "get time error");
                tv_shared_preference.setText(time);
                break;
        }
    }
}
