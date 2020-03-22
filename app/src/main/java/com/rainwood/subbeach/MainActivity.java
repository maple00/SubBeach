package com.rainwood.subbeach;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.subbeach.looper.LooperPageActivity;
import com.rainwood.subbeach.looper.SuperLooperPager;
import com.rainwood.subbeach.provider.VerifyCodeActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private final String TAG = MainActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSmsReadPermission();
    }

    /**
     * 权限检查
     */
    private void checkSmsReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResultCode = checkSelfPermission(Manifest.permission.READ_SMS);
            if (permissionResultCode != PackageManager.PERMISSION_GRANTED){
                // 请求权限
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);

            }
        }
    }

    /**
     * 获取短信权限
     * @param view
     */
    public void  getSmsContent(View view){
        ContentResolver cr = getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        Cursor query = cr.query(uri, null, null, null, null);
        String[] columnNames = query.getColumnNames();
        while (query.moveToNext()) {
            for (String columnName : columnNames) {
                Log.d(TAG, columnName + " === " + query.getString(query.getColumnIndex( columnName)));
            }
        }
        query.close();
    }

    /**
     * 跳转到短信验证码页面
     * @param view
     */
    public void toVerifyCodePage(View view){
        startActivity(new Intent(this, VerifyCodeActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            Log.d(TAG , "grantResults --  " + grantResults[0]);
        }
    }

    /**
     * 跳转到轮播图界面
     * @param view
     */
    public void tolooperPage(View view){
        startActivity(new Intent(this, LooperPageActivity.class));
    }

    /**
     * 跳转到自定义；轮播图
     * @param v
     */
    public void toCustomViewPager(View v){
        startActivity(new Intent(this, SuperLooperPager.class));
    }
}
