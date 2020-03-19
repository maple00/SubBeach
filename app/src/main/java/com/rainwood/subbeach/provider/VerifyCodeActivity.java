package com.rainwood.subbeach.provider;

import android.annotation.SuppressLint;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.subbeach.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: shearson
 * @Time: 2020/3/8 18:06
 * @Desc: java类作用描述
 */
public class VerifyCodeActivity extends AppCompatActivity {

    private String TAG = "VerifyCodeActivity";
    private EditText etVerifyCode;
    private Button btGetCode;
    private Button checkVerifyCode;
    private EditText phoneNum;

    public static final int MATCH_CODE = 0x101;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI("sms","*", MATCH_CODE);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        initView();
        initEvents();
        // 构建短信观察者
        Uri uri = Uri.parse("content://sms/");
        getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Log.d(TAG, " uri -- > " + uri);
                if (sUriMatcher.match(uri) == MATCH_CODE){
                    Log.d(TAG, " selfChange -- > " + selfChange);
                    Cursor query = getContentResolver().query(uri, new String[]{"body"}, null, null, null);
                    if (query.moveToNext()){
                        String body = query.getString(0);
                        Log.d(TAG, " body ===== >" + body);
                        handlerBody(body);
                    }
                    query.close();
                }
            }
        });

    }

    private void handlerBody(String body) {
        if (!TextUtils.isEmpty(body) && body.startsWith("【阳光沙滩】")){
            // 数字正则匹配
            Pattern rex = Pattern.compile("(?<![0-9])([0-9]{4})(?![0-9])");
            Matcher matcher = rex.matcher(body);
            boolean contain = matcher.find();
            if (contain){
                Log.d(TAG, "verifyCode ===== > " + matcher.group());
            }
        }
    }

    private void initEvents() {
        btGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNums = phoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNums)) {
                    Toast.makeText(VerifyCodeActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO: 向服务器请求验证码到手机

            }
        });

        checkVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查内容
                String phoneNum = VerifyCodeActivity.this.phoneNum.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(verifyCode)) {
                    Toast.makeText(VerifyCodeActivity.this, "手机号或验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initView() {
        phoneNum = findViewById(R.id.et_phone_num);
        etVerifyCode = findViewById(R.id.et_verify_code);
        checkVerifyCode = findViewById(R.id.btn_check_verify_code);
        btGetCode = findViewById(R.id.btn_get_code);
    }
}
