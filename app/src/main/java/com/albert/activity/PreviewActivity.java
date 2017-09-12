package com.albert.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.albert.uitl.CameraHintView;
import com.albert.uitl.VerticalSeekBar;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautySpecialEffectsFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;
import static com.albert.activity.R.drawable.dialog;
import static com.albert.activity.R.drawable.live;
import static com.ksyun.media.streamer.kit.StreamerConstants.VIDEO_RESOLUTION_720P;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity.............";
    private KSYStreamer mStreamer;
    private CameraHintView mCameraHintView;

    private ImageButton peautyBtn;
    private ImageButton changedCaBtn;

    private ImageButton peautyBtnL;
    private ImageButton changedCaBtnL;
    private ImageView colseS;
    private ImageView dm;
    private ImageView stopLive;
    private ImageView state;

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
    private boolean mRecording = false;
    private boolean mIsFileRecording = false;


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
    private View mGwcView;
    private int SampleTimes;
    private SharedPreferences.Editor editor;

    private Handler mMainHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.BLACK);
            getWindow().setStatusBarColor(Color.alpha(10));

        }
        setContentView(R.layout.activity_preview);
        bindID();
        startCameraPreviewWithPermCheck();


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
        mGwcView = findViewById(R.id.people_gwc_ifo);

        peautyBtnL = (ImageButton) findViewById(R.id.beauty_live_layout);
        changedCaBtnL = (ImageButton) findViewById(R.id.changedCa_live_layout);
        colseS = (ImageView) findViewById(R.id.noSound_live);
        dm = (ImageView) findViewById(R.id.danm_live);
        stopLive = (ImageView) findViewById(R.id.camera_stop_li_layout);
        state = (ImageView) findViewById(R.id.state_img_top);


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

//        editTextDialog.setFocusable(true);
//        editTextDialog.setFocusableInTouchMode(true);
//        editTextDialog.requestFocus();


        startDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (editTextDialog.getText().toString().equals("")) {
                    Toast.makeText(PreviewActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    editor.putString("roomid", String.valueOf(editTextDialog.getText().toString()));
                    editor.apply();
//                    parameterSettings();
                    initCamera();


                }

            }
        });

        dissDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });


        myDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(editTextDialog, InputMethodManager.SHOW_IMPLICIT);

                editTextDialog.setFocusable(true);
                editTextDialog.setFocusableInTouchMode(true);
                //请求获得焦点
                editTextDialog.requestFocus();
                //调用系统输入法
                InputMethodManager inputManager = (InputMethodManager) editTextDialog
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editTextDialog, 0);

            }

        });
        myDialog.show();


    }

    private void initCamera() {

        state.setImageResource(R.drawable.live);
        // 设置推流url（需要向相关人员申请，测试地址并不稳定！）
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        id = sp.getString("roomid", "");
        Log.e("id=====", id);
        url = "rtmp://58.56.9.249:1935/live/" + id + "" + "A";

        Log.e("url=====", url);

        mStreamer.setUrl(url);


        // 设置推流分辨率，可以不同于预览分辨率（不应大于预览分辨率，否则推流会有画质损失）
        mStreamer.setTargetResolution(VIDEO_RESOLUTION_720P);

        // 设置推流帧率，当预览帧率大于推流帧率时，编码模块会自动丢帧以适应设定的推流帧率
        mStreamer.setTargetFps(15);
        // 设置视频码率，分别为初始平均码率、最高平均码率、最低平均码率，单位为kbps，另有setVideoBitrate接口，单位为bps
        mStreamer.setVideoKBitrate(600);
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
        mStreamer.setEnableAudioNS(true);
        mPreviewView.setVisibility(View.INVISIBLE);
        mLiveingView.setVisibility(View.VISIBLE);
        mGwcView.setVisibility(View.VISIBLE);
        mStreamer.startStream();
        mStreamer.showWaterMarkTime(0.6f, 0.85f, 0.35f, Color.WHITE, 1.0f);
        myDialog.dismiss();

        mStreamer.setOnErrorListener(mOnErrorListener);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_start_pr_layout:
//                showSoftInputFromWindow(this, editTextDialog);
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
                    colseS.setImageResource(R.drawable.mute2);

                } else {
                    mStreamer.setMuteAudio(false);
                    muteABoolean = true;
                    colseS.setImageResource(R.drawable.mute);

                }

                break;
            case R.id.danm_live:
//                if ()
                dm.setImageResource(R.drawable.dm2);
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
                mStreamer.hideWaterMarkTime();
                mPreviewView.setVisibility(View.VISIBLE);
                mLiveingView.setVisibility(View.INVISIBLE);
                mGwcView.setVisibility(View.INVISIBLE);
                editor.remove("roomid");
                editor.apply();


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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        SharedPreferences settings = getSharedPreferences("Selector", 0);
//        Boolean user_first = settings.getBoolean("FIRST", true);
//        if (user_first) {
//            settings.edit().putBoolean("FIRST", false).commit();
//            parameterSettings();
//        } else {
//            readParameter();
//            parameterSettings();
//        }
//    }

//    private void readParameter() {
//        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
//        id = sp.getString("roomid", "");
////        SharedPreferences sharedPreferences = getSharedPreferences("data",
////                MODE_PRIVATE);
////        String times = sharedPreferences.getString("times", "");
//
//        Log.e("id=====", id);
//
//        if (id == "") {
//            parameterSettings();
//        } else {
//            editTextDialog.setText(id);
////            SampleTimes = Integer.parseInt(times);
////            SampleCounts = Integer.parseInt(counts);
//        }
//    }

    private void parameterSettings() {


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
//        mStreamer.getImgTexFilterMgt().setFilter();
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

    //    private String tag;
//    private String tag1;
//    private String tag2;
//    private String tag3;
//    private String tag4;
//    private String tag5;
//    private String tag6;
//    private String tag7;
//    private String tag8;
//    private String tag11;
//    private String tag12;
//    private String tag13;
//    private String tag14;
    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {

//        private String tag10;
//        private String tag9;
//        private String tag;

        @Override
        public void onError(int what, int msg1, int msg2) {
//            switch (what) {
//                case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
//                    tag1 = TAG;
//                    Log.d(tag1, "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
//
//                    tag2 = TAG;
//                    Log.d(tag2, "KSY_STREAMER_ERROR_CONNECT_FAILED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
//                    tag3 = TAG;
//                    Log.d(tag3, "KSY_STREAMER_ERROR_PUBLISH_FAILED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
//                    tag4 = TAG;
//                    Log.d(tag4, "KSY_STREAMER_ERROR_CONNECT_BREAKED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
//                    tag5 = TAG;
//                    Log.d(tag5, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
//                    break;
//                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
//                    tag6 = TAG;
//                    Log.d(tag6, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
//                    tag7 = TAG;
//                    Log.d(tag7, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
//                    break;
//                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
//                    tag8 = TAG;
//                    Log.d(tag8, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
//                    tag = TAG;
//                    Log.d(tag, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
//                    break;
//                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
//
//                    tag9 = TAG;
//                    Log.d(tag9, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
//                    tag10 = TAG;
//                    Log.d(tag10, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
//                    break;
//                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
//                    PreviewActivity.this.tag = TAG;
//                    Log.d(PreviewActivity.this.tag, "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
//                    break;
//                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
//                    tag11 = TAG;
//                    Log.d(tag11, "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
//                    break;
//                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
//                    tag12 = TAG;
//                    Log.d(tag12, "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
//                    break;
//                //Camera was disconnected due to use by higher priority user.
//                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
//                    tag13 = TAG;
//                    Log.d(tag13, "KSY_STREAMER_CAMERA_ERROR_EVICTED");
//                    break;
//                default:
//                    tag14 = TAG;
//                    Log.d(tag14, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
//                    break;
//            }
//            switch (what) {
//                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
//                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
//                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
//                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
//                    break;
//                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
//                    mStreamer.stopCameraPreview();
////                    stopLive.setImageResource(R.drawable.raw);
//                    mMainHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            startCameraPreviewWithPermCheck();
//
////                            stopLive.setImageResource(R.drawable.raw2);
//                            mStreamer.startStream();
//                        }
//                    }, 5000);
//                    break;
//                //重连
//                default:
//                    stopLive.setImageResource(R.drawable.raw);
//
//                    Toast.makeText(PreviewActivity.this, "当前网络质量不佳...", Toast.LENGTH_SHORT).show();
//                    mStreamer.stopStream();
//                    mMainHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            stopLive.setImageResource(R.drawable.raw2);
//                            mStreamer.startStream();
//                        }
//                    }, 5000);
//                    break;
//            }

            switch (what) {
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mStreamer.stopCameraPreview();
                    break;
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_CLOSE_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_OPEN_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_FORMAT_NOT_SUPPORTED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_WRITE_FAILED:
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN: {
//                    handleEncodeError();
                    if (mRecording) {
                        stopStream();

//                        Toast.makeText(PreviewActivity.this, "当前网络质量不佳...", Toast.LENGTH_SHORT).show();

                        mMainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startStream();
                            }
                        }, 3000);
                    }

                }
                break;
                default:
                    if (mStreamer.getEnableAutoRestart()) {
                        stopLive.setImageResource(R.drawable.raw2);
                        state.setImageResource(R.drawable.live);
                        stopLive.postInvalidate();
                        mRecording = false;
                    } else {
                        stopStream();
                        Toast.makeText(PreviewActivity.this, "当前网络质量不佳...", Toast.LENGTH_SHORT).show();
                        mMainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startStream();

                            }
                        }, 3000);
                    }
                    break;

            }
        }
    };

    private void startCameraPreviewWithPermCheck() {
        state.setVisibility(View.VISIBLE);
        state.setImageResource(R.drawable.ready);
        mCameraPreview = (GLSurfaceView) findViewById(R.id.camera_preview);
        // 创建KSYStreamer实例
        mStreamer = new KSYStreamer(PreviewActivity.this);
        // 设置预览View
        mStreamer.setDisplayPreview(mCameraPreview);
        mStreamer.setVideoCodecId(AVConst.CODEC_ID_AVC);

        // 设置预览分辨率, 当一边为0时，SDK会根据另一边及实际预览View的尺寸进行计算
        mStreamer.setPreviewResolution(480, 0);
        // 设置预览帧率
        mStreamer.setPreviewFps(15);
    }

    //start streaming
    private void startStream() {
        mStreamer.startStream();
        stopLive.setImageResource(R.drawable.raw2);
        state.setImageResource(R.drawable.live);
        stopLive.postInvalidate();
        mRecording = true;
    }

    private void stopStream() {
        // stop stream
        mStreamer.stopStream();
        stopLive.setImageResource(R.drawable.raw);
        state.setImageResource(R.drawable.ready);
        stopLive.postInvalidate();
        mRecording = false;
    }

}
