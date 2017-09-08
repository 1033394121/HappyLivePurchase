package com.albert.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.albert.uitl.CameraHintView;
import com.albert.uitl.VerticalSeekBar;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;

import java.util.List;

import static android.view.View.VISIBLE;
import static com.albert.activity.R.drawable.dialog;
import static com.ksyun.media.streamer.kit.StreamerConstants.VIDEO_RESOLUTION_720P;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity.............";
    private KSYStreamer mStreamer;
    private CameraHintView mCameraHintView;

    private ImageButton peautyBtn;
    private ImageButton changedCaBtn;

    private ImageButton peautyBtnL;
    private ImageButton changedCaBtnL;
    private ImageButton colseS;
    private ImageButton dm;
    private ImageButton stopLive;

    private View mBeautyChooseView;
    private AppCompatSpinner mBeautySpinner;
    private LinearLayout mBeautyGrindLayout;
    private TextView mGrindText;
    private AppCompatSeekBar mGrindSeekBar;
    private LinearLayout mBeautyWhitenLayout;
    private TextView mWhitenText;
    private AppCompatSeekBar mWhitenSeekBar;
    private LinearLayout mBeautyRuddyLayout;
    private TextView mRuddyText;
    private AppCompatSeekBar mRuddySeekBar;
    private VerticalSeekBar mExposureSeekBar;

    private View mPreviewView;
    private View mLiveingView;

    private boolean beauty = true;
    private boolean muteABoolean = true;
    private String id;
    private String url;

    private Dialog myDialog;
    private EditText editTextDialog;
    private Button startDialog;
    private ImageButton dissDialog;
    private CheckBox roomA;
    private CheckBox roomB;
    private CheckBox roomC;


//    private MyDialog dialog;

    private ImageButton startLiveBtn;
    private GLSurfaceView mCameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_preview);
        bindID();
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        id = sp.getString("roomid", "");

        initCamera();

        mCameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //聚焦和变焦功能
                CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
                cameraTouchHelper.setCameraCapture(mStreamer.getCameraCapture());
                mCameraPreview.setOnTouchListener(cameraTouchHelper);
                mBeautyChooseView.setVisibility(View.INVISIBLE);

                // set CameraHintView to show focus rect and zoom ratio
                cameraTouchHelper.setCameraHintView(mCameraHintView);
                cameraTouchHelper.setEnableZoom(true);
                cameraTouchHelper.setEnableTouchFocus(true);

                return false;
            }
        });

    }

    private void bindID() {


        mCameraHintView = (CameraHintView) findViewById(R.id.camera_hint);
        startLiveBtn = (ImageButton) findViewById(R.id.camera_start_pr_layout);
        peautyBtn = (ImageButton) findViewById(R.id.beauty_preview_layout);
        changedCaBtn = (ImageButton) findViewById(R.id.changedCa_preview_layout);

        mBeautyChooseView = findViewById(R.id.beauty_choose);

        mBeautySpinner = (AppCompatSpinner) findViewById(R.id.beauty_spin);
        mBeautyGrindLayout = (LinearLayout) findViewById(R.id.beauty_grind);
        mGrindText = (TextView) findViewById(R.id.grind_text);
        mGrindSeekBar = (AppCompatSeekBar) findViewById(R.id.grind_seek_bar);
        mBeautyWhitenLayout = (LinearLayout) findViewById(R.id.beauty_whiten);
        mWhitenText = (TextView) findViewById(R.id.whiten_text);
        mWhitenSeekBar = (AppCompatSeekBar) findViewById(R.id.whiten_seek_bar);
        mBeautyRuddyLayout = (LinearLayout) findViewById(R.id.beauty_ruddy);
        mRuddyText = (TextView) findViewById(R.id.ruddy_text);
        mRuddySeekBar = (AppCompatSeekBar) findViewById(R.id.ruddy_seek_bar);
        mExposureSeekBar = (VerticalSeekBar) findViewById(R.id.exposure_seekBar);
        mExposureSeekBar.setProgress(50);
        mExposureSeekBar.setSecondaryProgress(50);
        mExposureSeekBar.setOnSeekBarChangeListener(getVerticalSeekListener());

        mPreviewView = findViewById(R.id.preview_btn);
        mLiveingView = findViewById(R.id.liveing_btn);

        peautyBtnL = (ImageButton) findViewById(R.id.beauty_live_layout);
        changedCaBtnL = (ImageButton) findViewById(R.id.changedCa_live_layout);
        colseS = (ImageButton) findViewById(R.id.noSound_live);
        dm = (ImageButton) findViewById(R.id.danm_live);
        stopLive = (ImageButton) findViewById(R.id.camera_stop_li_layout);


        myDialog = new Dialog(PreviewActivity.this, R.style.dialog);
        myDialog.setContentView(R.layout.dialog_chose);
        editTextDialog = (EditText) myDialog.findViewById(R.id.roomID_dialog);
        startDialog = (Button) myDialog.findViewById(R.id.intentBtn_dialog);
        dissDialog = (ImageButton) myDialog.findViewById(R.id.closeBtn_dialog);
        roomA = (CheckBox) myDialog.findViewById(R.id.roomA_dialog);
        roomB = (CheckBox) myDialog.findViewById(R.id.roomB_dialog);
        roomC = (CheckBox) myDialog.findViewById(R.id.roomC_dialog);
        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        lp.width = 1000;


        startLiveBtn.setOnClickListener(this);
        peautyBtn.setOnClickListener(this);
        changedCaBtn.setOnClickListener(this);

        peautyBtnL.setOnClickListener(this);
        changedCaBtnL.setOnClickListener(this);
        colseS.setOnClickListener(this);
        dm.setOnClickListener(this);
        stopLive.setOnClickListener(this);


    }

    private void haveDialog() {
        myDialog = new Dialog(PreviewActivity.this, R.style.dialog);
        myDialog.setContentView(R.layout.dialog_chose);

        editTextDialog = (EditText) myDialog.findViewById(R.id.roomID_dialog);
        startDialog = (Button) myDialog.findViewById(R.id.intentBtn_dialog);
        dissDialog = (ImageButton) myDialog.findViewById(R.id.closeBtn_dialog);
        roomA = (CheckBox) myDialog.findViewById(R.id.roomA_dialog);
        roomB = (CheckBox) myDialog.findViewById(R.id.roomB_dialog);
        roomC = (CheckBox) myDialog.findViewById(R.id.roomC_dialog);


        startDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextDialog.getText().toString().equals("")) {
                    Toast.makeText(PreviewActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    editor.putString("roomid", String.valueOf(editTextDialog.getText().toString()));
                    editor.apply();
                    mPreviewView.setVisibility(View.INVISIBLE);
                    mLiveingView.setVisibility(View.VISIBLE);
                    mStreamer.startStream();
                    myDialog.dismiss();
                }

            }
        });
        dissDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        lp.width = 1000;
        myDialog.show();


    }

    private void initCamera() {

        mCameraPreview = (GLSurfaceView) findViewById(R.id.camera_preview);
        // 创建KSYStreamer实例
        mStreamer = new KSYStreamer(PreviewActivity.this);
// 设置预览View
        mStreamer.setDisplayPreview(mCameraPreview);
// 设置推流url（需要向相关人员申请，测试地址并不稳定！）
        url = "rtmp://58.56.9.249:1935/live/" + id + "A";

        mStreamer.setUrl(url);

        mStreamer.setVideoCodecId(AVConst.CODEC_ID_AVC);

// 设置预览分辨率, 当一边为0时，SDK会根据另一边及实际预览View的尺寸进行计算
        mStreamer.setPreviewResolution(480, 0);
// 设置推流分辨率，可以不同于预览分辨率（不应大于预览分辨率，否则推流会有画质损失）
        mStreamer.setTargetResolution(VIDEO_RESOLUTION_720P);
// 设置预览帧率
        mStreamer.setPreviewFps(15);
// 设置推流帧率，当预览帧率大于推流帧率时，编码模块会自动丢帧以适应设定的推流帧率
        mStreamer.setTargetFps(15);
// 设置视频码率，分别为初始平均码率、最高平均码率、最低平均码率，单位为kbps，另有setVideoBitrate接口，单位为bps
        mStreamer.setVideoKBitrate(600, 800, 1000);
//        mStreamer.setVideoKBitrate(1000kbps);
// 设置音频采样率

        mStreamer.setAudioSampleRate(44100);
// 设置音频码率，单位为kbps，另有setAudioBitrate接口，单位为bps
        mStreamer.setAudioKBitrate(48);
/**
 * 设置编码模式(软编、硬编)，请根据白名单和系统版本来设置软硬编模式，不要全部设成软编或者硬编,白名单可以联系金山云商务:
 * StreamerConstants.ENCODE_METHOD_SOFTWARE
 * StreamerConstants.ENCODE_METHOD_HARDWARE
 */
        mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
// 设置屏幕的旋转角度，支持 0, 90, 180, 270
        mStreamer.setRotateDegrees(0);
// 设置开始预览使用前置还是后置摄像头
        mStreamer.setCameraFacing(CameraCapture.FACING_FRONT);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_start_pr_layout:
                haveDialog();


                break;
            case R.id.beauty_preview_layout:
                if (beauty) {
                    mBeautyChooseView.setVisibility(View.VISIBLE);
                    initBeautyUI();
                    beauty = false;
                } else {
                    mBeautyChooseView.setVisibility(View.INVISIBLE);
                    beauty = true;

                }

                break;
            case R.id.changedCa_preview_layout:
                changedCamera();
                break;


            case R.id.beauty_live_layout:
                if (beauty) {
                    mBeautyChooseView.setVisibility(View.VISIBLE);
                    initBeautyUI();
                    beauty = false;
                } else {
                    mBeautyChooseView.setVisibility(View.INVISIBLE);
                    beauty = true;

                }
                break;
            case R.id.changedCa_live_layout:
                changedCamera();
                break;
            case R.id.noSound_live:
                if (muteABoolean) {
                    mStreamer.setMuteAudio(true);
                    muteABoolean = false;

                } else {
                    mStreamer.setMuteAudio(false);
                    muteABoolean = true;
                }

                break;
            case R.id.danm_live:
                changedCamera();
                break;
            case R.id.camera_stop_li_layout:
                stopDialog();


                break;

            default:
                break;
        }

    }

    private void stopDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PreviewActivity.this);
        builder.setTitle("温馨提示:");
        builder.setMessage("您确定要退出直播吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mStreamer.stopStream();
                mPreviewView.setVisibility(View.VISIBLE);
                mLiveingView.setVisibility(View.INVISIBLE);


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    private void changedCamera() {
        mStreamer.switchCamera();
        mCameraHintView.hideAll();


    }


    @Override
    public void onResume() {
        super.onResume();
        // 一般可以在onResume中开启摄像头预览
        mStreamer.startCameraPreview();
        // 调用KSYStreamer的onResume接口
        mStreamer.onResume();
        // 如果onPause中切到了DummyAudio模块，可以在此恢复
        mStreamer.setUseDummyAudioCapture(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mStreamer.onPause();
        // 一般在这里停止摄像头采集
        mStreamer.stopCameraPreview();
        // 如果希望App切后台后，停止录制主播端的声音，可以在此切换为DummyAudio采集，
        // 该模块会代替mic采集模块产生静音数据，同时释放占用的mic资源
        mStreamer.setUseDummyAudioCapture(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 清理相关资源
        mStreamer.release();
    }

    private void initBeautyUI() {
        mBeautyChooseView.setVisibility(View.VISIBLE);

        String[] items = new String[]{"美颜"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBeautySpinner.setAdapter(adapter);
        mBeautySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView) parent.getChildAt(0));
                if (textView != null) {
                    textView.setTextColor(getResources().getColor(R.color.font_color_35));
                }
                if (position == 0) {

                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), ImgTexFilterMgt.KSY_FILTER_BEAUTY_SMOOTH);

                }

//
                List<ImgFilterBase> filters = mStreamer.getImgTexFilterMgt().getFilter();
                if (filters != null && !filters.isEmpty()) {
                    final ImgFilterBase filter = filters.get(0);
                    mBeautyGrindLayout.setVisibility(filter.isGrindRatioSupported() ?
                            VISIBLE : View.GONE);
                    mBeautyWhitenLayout.setVisibility(filter.isWhitenRatioSupported() ?
                            VISIBLE : View.GONE);
                    mBeautyRuddyLayout.setVisibility(filter.isRuddyRatioSupported() ?
                            VISIBLE : View.GONE);
                    SeekBar.OnSeekBarChangeListener seekBarChangeListener =
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress,
                                                              boolean fromUser) {
                                    if (!fromUser) {
                                        return;
                                    }
                                    float val = progress / 100.f;
                                    if (seekBar == mGrindSeekBar) {
                                        filter.setGrindRatio(val);
                                    } else if (seekBar == mWhitenSeekBar) {
                                        filter.setWhitenRatio(val);
                                    } else if (seekBar == mRuddySeekBar) {
                                        if (filter instanceof ImgBeautyProFilter) {
                                            val = progress / 50.f - 1.0f;
                                        }
                                        filter.setRuddyRatio(val);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                }
                            };
                    mGrindSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mWhitenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mRuddySeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mGrindSeekBar.setProgress((int) (filter.getGrindRatio() * 100));
                    mWhitenSeekBar.setProgress((int) (filter.getWhitenRatio() * 100));
                    int ruddyVal = (int) (filter.getRuddyRatio() * 100);
                    if (filter instanceof ImgBeautyProFilter) {
                        ruddyVal = (int) (filter.getRuddyRatio() * 50 + 50);
                    }
                    mRuddySeekBar.setProgress(ruddyVal);
                } else {
                    mBeautyGrindLayout.setVisibility(View.GONE);
                    mBeautyWhitenLayout.setVisibility(View.GONE);
                    mBeautyRuddyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        mBeautySpinner.setPopupBackgroundResource(R.color.transparent1);
        mBeautySpinner.setSelection(0);
    }


    private VerticalSeekBar.OnSeekBarChangeListener getVerticalSeekListener() {
        VerticalSeekBar.OnSeekBarChangeListener listener = new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(VerticalSeekBar seekBar, int progress, boolean fromUser) {
                Camera.Parameters parameters = mStreamer.getCameraCapture().getCameraParameters();
                if (parameters != null) {
                    int minValue = parameters.getMinExposureCompensation();
                    int maxValue = parameters.getMaxExposureCompensation();
                    int range = 100 / (maxValue - minValue);
                    parameters.setExposureCompensation(progress / range - maxValue);
                }
                mStreamer.getCameraCapture().setCameraParameters(parameters);
            }

            @Override
            public void onStartTrackingTouch(VerticalSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar seekBar) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean enable) {

            }
        };
        return listener;
    }


}
