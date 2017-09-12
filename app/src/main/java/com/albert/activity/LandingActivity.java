package com.albert.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button intent;
    private int xiangji = 3;
    private File
            sdcardTempFile = new

            File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");
    private EditText pheon;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            );
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//
//        }
        setContentView(R.layout.activity_landing);
        bindID();

//        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//        editor.putString("userName", String.valueOf(pheon.getText()));
//        editor.putString("passwed", String.valueOf(password.getText()));
//        editor.apply();
//
//        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
//        pheon.setText(sp.getString("userName", ""));
//        password.setText(sp.getString("passwed", ""));
//        showSoftInputFromWindow(this, pheon);

    }

    private void bindID() {
        intent = (Button) findViewById(R.id.intentBt_landing);
        intent.setOnClickListener(this);

        pheon = (EditText) findViewById(R.id.phone_landing);
        password = (EditText) findViewById(R.id.password_landing);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.intentBt_landing:
                saveIfo();


//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri u = Uri.fromFile(sdcardTempFile);
//                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
//                intent.putExtra("return-data", true);
//                startActivityForResult(intent, xiangji);

//                finish();
                break;


            default:
                break;
        }

    }

    private void saveIfo() {


        String phoneNum = pheon.getText().toString();
        String passwrd = password.getText().toString();

        String url = "https://fuwu.legouzb.weadd.cn/server/appsev.ashx?Command=userlogin&username=" + phoneNum + "&pwd=" + passwrd;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                Toast.makeText(LandingActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    int userName = Integer.parseInt(obj.getString("code"));
                    if (userName == 1) {
                        Toast.makeText(LandingActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LandingActivity.this, PreviewActivity.class);
                        startActivity(intent);

                    } else if (userName == 0) {
                        Toast.makeText(LandingActivity.this, "您输入的用户名或密码有误,请重新输入", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
