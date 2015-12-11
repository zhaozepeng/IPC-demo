package com.android.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-11
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    Uri studentUri;
    Uri gradeUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mainactivity);
        findViewById(R.id.btn_query).setOnClickListener(this);
        studentUri = StudentProvider.STUDENT_CONTENT_URI;
        gradeUri = StudentProvider.GRADE_CONTENT_URI;

        ContentValues studentValues = new ContentValues();
        studentValues.put("id", 1);
        studentValues.put("name", "zhao");
        studentValues.put("gender", "male");
        studentValues.put("weight", 68.5);
        getContentResolver().insert(studentUri, studentValues);

        ContentValues gradeValues = new ContentValues();
        gradeValues.put("id", 1);
        gradeValues.put("chinese", 90.5);
        gradeValues.put("math", 80.5);
        gradeValues.put("english", 91.5);
        getContentResolver().insert(gradeUri, gradeValues);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_query:
                StringBuilder stringBuilder = new StringBuilder();
                Cursor cursor= getContentResolver().query(studentUri, null, null, null, null);
                stringBuilder.append("STUDENT\n");
                while (cursor.moveToNext()){
                    stringBuilder.append("id:").append(cursor.getString(0)).append("\n");
                    stringBuilder.append("name:").append(cursor.getString(1)).append("\n");
                    stringBuilder.append("gender:").append(cursor.getString(2)).append("\n");
                    stringBuilder.append("weight:").append(cursor.getString(3)).append("\n");
                }
                cursor.close();
                cursor = getContentResolver().query(gradeUri, null, null, null, null);
                stringBuilder.append("GRADE\n");
                while (cursor.moveToNext()){
                    stringBuilder.append("id:").append(cursor.getString(0)).append("\n");
                    stringBuilder.append("chinese:").append(cursor.getString(1)).append("\n");
                    stringBuilder.append("math:").append(cursor.getString(2)).append("\n");
                    stringBuilder.append("english:").append(cursor.getString(3)).append("\n");
                }
                cursor.close();
                ((TextView)findViewById(R.id.tv_result)).setText(stringBuilder);
                break;
        }
    }
}
