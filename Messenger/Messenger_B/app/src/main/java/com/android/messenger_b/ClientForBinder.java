package com.android.messenger_b;

import android.os.Bundle;
import android.view.View;

import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-08
 */
public class ClientForBinder extends BaseActivity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_binder);
        findViewById(R.id.connect_binder).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
