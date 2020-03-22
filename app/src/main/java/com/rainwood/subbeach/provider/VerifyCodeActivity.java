package com.rainwood.subbeach.provider;

import android.annotation.SuppressLint;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
 * @Author: sxs
 * @Time: 2020/3/8 18:06
 * @Desc: 短信内容提供者 --- 获取短信中的验证码
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
        sUriMatcher.addURI("sms", "*", MATCH_CODE);
    }

    /**
     * verify code count down
     */
    private CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @SuppressLint("DefaultLocale")
        @Override
        public void onTick(long millisUntilFinished) {
            btGetCode.setText(String.format("%d 可重新获取", millisUntilFinished / 1000));
            btGetCode.setClickable(false);
        }

        @Override
        public void onFinish() {
            btGetCode.setClickable(true);
        }
    };

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        initView();
        initEvents();
        // 构建短信观察者
        Uri uri = Uri.parse("content://sms/");
        getContentResolver().registerContentObserver(uri, true,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        if (sUriMatcher.match(uri) == MATCH_CODE) {
                            Log.d(TAG, " selfChange -- > " + selfChange);
                            Log.d(TAG, " uri -- > " + uri);
                            Cursor query = getContentResolver().query(uri, new String[]{"message_body"},
                                    null, null, null);
                            // 获取短信的column
                            /*String[] columnNames = query.getColumnNames();
                            while (query.moveToNext()){
                                for (String columnName : columnNames) {
                                    Log.d(TAG, columnName + "  ==== " + query.getString(query.getColumnIndex(columnName)));
                                }
                            }*/
                            if (query.moveToNext()) {
                                String msg_body = query.getString(0);
                                Log.d(TAG, " body ===== >" + msg_body);
                                handlerBody(msg_body);
                            }
                            query.close();
                        }
                    }
                });

    }

    /**
     * 匹配短信验证码
     *
     * @param body
     */
    private void handlerBody(String body) {
        if (!TextUtils.isEmpty(body) && body.startsWith("【阳光沙滩】")) {
            // 数字正则匹配
            Pattern rex = Pattern.compile("(?<![0-9])([0-9]{4})(?![0-9])");
            Matcher matcher = rex.matcher(body);
            boolean contain = matcher.find();
            if (contain) {
                Log.d(TAG, "verifyCode ===== > " + matcher.group());
                etVerifyCode.setText(matcher.group());
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
                mCountDownTimer.start();
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
