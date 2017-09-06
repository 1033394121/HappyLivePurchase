package com.albert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button intent;
    private int xiangji = 3;
    private File
            sdcardTempFile = new

            File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");

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

    }

    private void bindID() {
        intent = (Button) findViewById(R.id.intentBt_landing);
        intent.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.intentBt_landing:
                saveIfo();
                Intent intent = new Intent(LandingActivity.this, PreviewActivity.class);
                startActivity(intent);

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


    }


}
