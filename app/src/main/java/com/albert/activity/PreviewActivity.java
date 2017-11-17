package com.albert.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.albert.uitl.CameraHintView;
import com.albert.uitl.InfoVO;
import com.albert.uitl.MixtureTextView;
import com.albert.uitl.RecyclerViewAdapter;
import com.albert.uitl.VerticalSeekBar;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rabtman.wsmanager.WsManager;
import com.rabtman.wsmanager.listener.WsStatusListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.CAMERA;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static android.view.View.VISIBLE;
import static com.ksyun.media.streamer.kit.StreamerConstants.VIDEO_RESOLUTION_720P;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    //    private WebSocketConnection mConnect = new WebSocketConnection();
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
    private View mDanmuView;
    private View mPicView;
    private ImageView tuijian;

    private boolean beauty = true;
    private boolean muteABoolean = true;
    private boolean mRecording = false;
    private boolean mIsFileRecording = false;
    private boolean danmu = true;

    boolean n = true;


    private String id;
    private String url;

    private Dialog myDialog;
    private Dialog escDialog;
    private EditText editTextDialog;
    private Button startDialog;
    private ImageButton dissDialog;
    private CheckBox roomA;
    private CheckBox roomB;
    private CheckBox roomC;
    private TextView gwcMoney;
    private TextView peopleNum;

    private ImageButton dissBtnEsc;
    private Button psBtnEsc;
    private Button ntBtnEsc;


//    private MyDialog dialog;

    private ImageButton startLiveBtn;
    private GLSurfaceView mCameraPreview;
    private View mGwcView;
    private int SampleTimes;
    private SharedPreferences.Editor editor;

    private List<InfoVO> infList = new ArrayList<>();

    private String roomid;
    private String phonN;
    private String zhuboNum;


    private MixtureTextView danmuText;

    private okhttp3.WebSocket webSocket;
    private int people;
    private SharedPreferences sp;
    private OkHttpClient clinet;
    private Request request;
    private String danmuURL;
    private int codes;
    private String rs;
    private static final int MSG_ID_1 = 10;
    private static final int MSG_ID_2 = 20;
    private static final int MSG_ID_3 = 21;
    private static final int MSG_ID_4 = 80;
    private static final int MSG_ID_5 = 90;

    private int msType;
    private int money21;
    private String moneGou;
    private RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    Bitmap tuijiantupian;
    private Message msg1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;//权限请求码


    private Handler mMainHandler = new Handler();
    //
    public Thread thread;
    private Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mPicView = findViewById(R.id.pic_view);
            recyclerView = (RecyclerView) findViewById(R.id.danmu_item_rcl);
            LinearLayoutManager layoutManager = new LinearLayoutManager(PreviewActivity.this,
                    LinearLayoutManager.VERTICAL, false);
            layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//            layoutManager.setReverseLayout(false);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new RecyclerViewAdapter(PreviewActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.smoothScrollToPosition(infList.size());
            super.handleMessage(msg);

//
            switch (msg.what) {
                case MSG_ID_1:
//                    Log.e("Web===", infList.size() + "");
                    adapter.addList(infList);
                    adapter.notifyItemInserted(infList.size() - 1);
                    recyclerView.setAdapter(adapter);
                    float moneGou2 = Float.parseFloat(moneGou);
                    gwcMoney.setText(String.valueOf(moneGou2 + money21));
                    break;
                case MSG_ID_2:
                    Log.e("Web===", people + "");
                    peopleNum.setText(people + "");
                    break;
                case MSG_ID_3:
                    adapter.addList(infList);
                    adapter.notifyItemInserted(infList.size() - 1);
                    recyclerView.setAdapter(adapter);
                    break;
                case MSG_ID_4:
                    mPicView.setVisibility(View.INVISIBLE);
                    break;
                case MSG_ID_5:
                    mPicView.setVisibility(View.VISIBLE);
                    tuijian.setImageBitmap(tuijiantupian);
                    break;
            }


//
            mHandler1.removeCallbacks(msg.getCallback());

        }


    };
    private WsManager wsManager;
    private Bitmap bm;
    private String imgUrl;


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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_preview);

        bindID();

        startCameraPreviewWithPermCheck();

        mStreamer.setOnInfoListener(mOnInfoListener);
        mStreamer.setOnErrorListener(mOnErrorListener);


        mCameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                //聚焦和变焦功能
                CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
                cameraTouchHelper.setCameraCapture(mStreamer.getCameraCapture());
                mCameraPreview.setOnTouchListener(cameraTouchHelper);

                // set CameraHintView to show focus rect and zoom ratio
                cameraTouchHelper.setCameraHintView(mCameraHintView);
                cameraTouchHelper.setEnableZoom(true);
                cameraTouchHelper.setEnableTouchFocus(true);

                mBeautyChooseView.setVisibility(View.INVISIBLE);
                return true;
            }
        });

//        connect();

    }


    /**
     * 获取直播间交易金额
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */

    private void requestData() {
        sp = getSharedPreferences("data", MODE_PRIVATE);
        id = sp.getString("roomid", "");

        final String roomMoney = "https://fuwu.legouzb.weadd.cn/server/appsev.ashx?Command=getamount&fangjianhao=" + id;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(roomMoney, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                Toast.makeText(PreviewActivity.this, "没有获取到金额", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String money = obj.getString("amount");
                    gwcMoney.setText(money + "");
                    moneGou = (String) gwcMoney.getText();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    /**
     * 绑定各控件的ID
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */
    private void bindID() {

        mCameraHintView = (CameraHintView) findViewById(R.id.camera_hint);
        startLiveBtn = (ImageButton) findViewById(R.id.camera_start_pr_layout);
        peautyBtn = (ImageButton) findViewById(R.id.beauty_preview_layout);
        changedCaBtn = (ImageButton) findViewById(R.id.changedCa_preview_layout);
        mBeautyChooseView = findViewById(R.id.beauty_choose);
        mDanmuView = findViewById(R.id.danmu_view);
        tuijian = (ImageView) findViewById(R.id.jieshao_pic);


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
        gwcMoney = (TextView) findViewById(R.id.gwc_top_layout);
        peopleNum = (TextView) findViewById(R.id.people_top_layout);


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

        escDialog = new Dialog(PreviewActivity.this, R.style.dialog);
        escDialog.setContentView(R.layout.dialog_esc);
        dissBtnEsc = (ImageButton) escDialog.findViewById(R.id.closeBtn_dialog_esc);
        psBtnEsc = (Button) escDialog.findViewById(R.id.intentBtn_dialog_esc);
        ntBtnEsc = (Button) escDialog.findViewById(R.id.nBtn_dialog_esc);


        Window dialogWindow2 = escDialog.getWindow();
        WindowManager.LayoutParams lp2 = dialogWindow2.getAttributes();
        dialogWindow2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        lp2.width = 950;

        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        lp.width = 950;


        startLiveBtn.setOnClickListener(this);
        peautyBtn.setOnClickListener(this);
        changedCaBtn.setOnClickListener(this);

        peautyBtnL.setOnClickListener(this);
        changedCaBtnL.setOnClickListener(this);
        colseS.setOnClickListener(this);
        dm.setOnClickListener(this);
        stopLive.setOnClickListener(this);

    }


    /**
     * 开始直播弹出的Dialog
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */
    private void haveDialog() {
        myDialog = new Dialog(PreviewActivity.this, R.style.dialog);
        myDialog.setContentView(R.layout.dialog_chose);
        editTextDialog = (EditText) myDialog.findViewById(R.id.roomID_dialog);
        startDialog = (Button) myDialog.findViewById(R.id.intentBtn_dialog);
        dissDialog = (ImageButton) myDialog.findViewById(R.id.closeBtn_dialog);
        roomA = (CheckBox) myDialog.findViewById(R.id.roomA_dialog);
        roomB = (CheckBox) myDialog.findViewById(R.id.roomB_dialog);
        roomC = (CheckBox) myDialog.findViewById(R.id.roomC_dialog);
        roomA.setChecked(true);
        zhuboNum = "A";
        roomA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (roomA.isChecked()) {
                    roomB.setChecked(false);
                    roomC.setChecked(false);
                    zhuboNum = "A";
                }
            }
        });
        roomB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (roomB.isChecked()) {
                    roomA.setChecked(false);
                    roomC.setChecked(false);
                    zhuboNum = "B";
                }
            }
        });
        roomC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (roomC.isChecked()) {
                    roomA.setChecked(false);
                    roomB.setChecked(false);
                    zhuboNum = "C";
                }
            }
        });


        startDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextDialog.getText().toString().equals("")) {
                    Toast.makeText(PreviewActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    editor.putString("roomid", String.valueOf(editTextDialog.getText().toString()));
                    editor.apply();
                    mDanmuView.setVisibility(View.VISIBLE);
                    requestData();
                    initCamera();
                    initDanmu();
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

    /**
     * 停止直播弹出的Dialog
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */
    private void stopDialog() {
        escDialog = new Dialog(PreviewActivity.this, R.style.dialog);
        escDialog.setContentView(R.layout.dialog_esc);
        dissBtnEsc = (ImageButton) escDialog.findViewById(R.id.closeBtn_dialog_esc);
        psBtnEsc = (Button) escDialog.findViewById(R.id.intentBtn_dialog_esc);
        ntBtnEsc = (Button) escDialog.findViewById(R.id.nBtn_dialog_esc);
        dissBtnEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escDialog.dismiss();
            }
        });
        psBtnEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStreamer.stopStream();
                mLiveingView.setVisibility(View.INVISIBLE);
                mPreviewView.setVisibility(View.VISIBLE);
                mStreamer.hideWaterMarkTime();
                mGwcView.setVisibility(View.INVISIBLE);
                mDanmuView.setVisibility(View.INVISIBLE);
                state.setImageResource(R.drawable.ready);
//                webSocket.close(1000, "");//停止直播后把sorcket通信关闭
                wsManager.sendMessage("{\"msgtype\":61,\"msgto\":1,\"msgdesp\":" + zhuboNum + "}");
                wsManager.stopConnect();
                infList.clear();//每次清除一下list中的数据
                escDialog.dismiss();
            }
        });
        ntBtnEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escDialog.dismiss();
            }
        });
        escDialog.show();
    }


    private void initCamera() {

        state.setImageResource(R.drawable.live);
        // 设置推流url（需要向相关人员申请，测试地址并不稳定！）
        sp = getSharedPreferences("data", MODE_PRIVATE);
        id = sp.getString("roomid", "");
        Log.e("id=====", id);
        url = "rtmp://58.56.9.249:1935/live/" + id + zhuboNum;
        mStreamer.setUrl(url);
        Log.e("url=====", url);
        // 设置推流分辨率，可以不同于预览分辨率（不应大于预览分辨率，否则推流会有画质损失）
        mStreamer.setTargetResolution(VIDEO_RESOLUTION_720P);
        // 设置推流帧率，当预览帧率大于推流帧率时，编码模块会自动丢帧以适应设定的推流帧率
        mStreamer.setTargetFps(15);
        // 设置视频码率，分别为初始平均码率、最高平均码率、最低平均码率，单位为kbps，另有setVideoBitrate接口，单位为bps
        mStreamer.setVideoKBitrate(600);
        // mStreamer.setVideoKBitrate(1000kbps);
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
        mStreamer.showWaterMarkTime(0.6f, 0.85f, 0.35f, Color.WHITE, 1.0f);
        mStreamer.startStream();//开始推流
        myDialog.dismiss();
    }

    /**
     * 各按钮点击事件的实现
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*开始直播按钮*/
            case R.id.camera_start_pr_layout:
                haveDialog();
                break;
            /*预览美颜按钮*/
            case R.id.beauty_preview_layout:
                if (beauty) {
                    if (n) {
                        mBeautyChooseView.setVisibility(View.VISIBLE);
                        initBeautyUI();
                        n = false;
                    } else {
                        mBeautyChooseView.setVisibility(View.VISIBLE);
                    }

                    beauty = false;
                } else {
                    mBeautyChooseView.setVisibility(View.INVISIBLE);
                    beauty = true;
                }

                break;
            /*预览页切换摄像头按钮*/
            case R.id.changedCa_preview_layout:
                changedCamera();
                break;
            /*直播中美颜按钮*/
            case R.id.beauty_live_layout:
                if (beauty) {
//                    mBeautyChooseView.setVisibility(View.VISIBLE);
//                    initBeautyUI();
                    if (n) {
                        mBeautyChooseView.setVisibility(View.VISIBLE);
                        mDanmuView.setVisibility(View.INVISIBLE);
                        initBeautyUI();
                        n = false;
                    } else {
                        mBeautyChooseView.setVisibility(View.VISIBLE);
                        mDanmuView.setVisibility(View.INVISIBLE);

                    }
                    beauty = false;
                } else {
                    mBeautyChooseView.setVisibility(View.INVISIBLE);
                    mDanmuView.setVisibility(View.VISIBLE);


                    beauty = true;

                }
                break;
            /*直播页切换摄像头按钮*/
            case R.id.changedCa_live_layout:
                changedCamera();
                break;
            /*关闭&打开声音按钮*/
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
            /*关闭&打开弹幕按钮*/
            case R.id.danm_live:

                if (danmu) {
                    mDanmuView.setVisibility(View.INVISIBLE);
                    danmu = false;
                    dm.setImageResource(R.drawable.dm);
                } else {
                    dm.setImageResource(R.drawable.dm2);
                    if (wsManager.isWsConnected()) {
                        mDanmuView.setVisibility(View.VISIBLE);
                    } else {
//                        initDanmu();
                        wsManager.startConnect();
                        mDanmuView.setVisibility(View.VISIBLE);
                    }

                    danmu = true;
                }
                break;
            /*停止直播按钮*/
            case R.id.camera_stop_li_layout:
                stopDialog();
                break;
            default:
                break;
        }

    }

    /**
     * 实现切换摄像头的方法
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */

    private void changedCamera() {
        mStreamer.switchCamera();
        mCameraHintView.hideAll();


    }


    /**
     * 美颜模块
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */
    private void initBeautyUI() {

        String[] items = new String[]{"美颜"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBeautySpinner.setAdapter(adapter);
        mBeautySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), ImgTexFilterMgt.KSY_FILTER_BEAUTY_SMOOTH);
                }
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
//        mStreamer.getImgTexFilterMgt().setFilter(adapter);
    }


    /**
     * 美颜seekbar数据变化
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */

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

    /**
     * 开启直播预览
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */
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
        /**
         * 设置编码模式(软编、硬编)，请根据白名单和系统版本来设置软硬编模式，不要全部设成软编或者硬编,白名单可以联系金山云商务:
         * StreamerConstants.ENCODE_METHOD_SOFTWARE
         * StreamerConstants.ENCODE_METHOD_HARDWARE
         */
//        mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
    }


    /**
     * 弹幕模块
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */
    private void initDanmu() {
        sp = getSharedPreferences("data", MODE_PRIVATE);
        phonN = sp.getString("userName", "");
        roomid = sp.getString("roomid", "");
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
//                Send message to the server:
//                String msg or ByteString byteString
                wsManager.sendMessage("{\"msgtype\":-1}");

            }
        };


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                Instantiate a WsManager object:
                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                        .pingInterval(10, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)//允许失败重试
                        .readTimeout(10000, TimeUnit.SECONDS)//设置读取超时时间
                        .connectTimeout(10000, TimeUnit.SECONDS)//设置连接超时时间
                        .build();
                wsManager = new WsManager.Builder(PreviewActivity.this)
                        .wsUrl("wss://rms.weadd.cn/" + roomid + "/" + phonN + "/anchor")
                        .needReconnect(true)
                        .client(okHttpClient)
                        .build();
//                Establish a connection with the server:

                wsManager.startConnect();

                wsManager.sendMessage("{\"msgname\":" + phonN + ",\"msgcontent\":\"\"rtmp://58.56.9.249:1935/live/\"" + id + zhuboNum + "\",\"msgtype\":60,\"msgto\":1,\"msgdesp\":\"A\"}");

//                Listens for server connection status:

                wsManager.setWsStatusListener(new WsStatusListener() {
                    @Override
                    public void onOpen(Response response) {
                        super.onOpen(response);
                    }

                    @Override
                    public void onMessage(final String text) {
//                        Toast.makeText(PreviewActivity.this, text, Toast.LENGTH_SHORT).show();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
                                JsonData(text);

//                            }
//                        }).start();

//
                    }

                    @Override
                    public void onReconnect() {
                        initDanmu();
                    }

                    @Override
                    public void onClosed(int code, String reason) {
                        super.onClosed(code, reason);
                    }

                    @Override
                    public void onFailure(Throwable t, Response response) {
                        super.onFailure(t, response);
                        initDanmu();
                    }


                });
//
//                Close the connection to the server:
                timer.schedule(task, 100);

            }


            private void JsonData(String text) {
                try {
                    JSONObject obj = new JSONObject(text);
                    if (obj.has("currentUserNum")) {
//                                Thread.sleep(3000);
                        people = obj.getInt("currentUserNum");

                        Message msg2 = mHandler1.obtainMessage();
                        msg2.what = MSG_ID_2;
                        msg2.obj = people;
                        mHandler1.sendMessage(msg2);
                    } else if (obj.has("msgtype")) {
                        msType = obj.getInt("msgtype");
                        editor.putString("mstyp", String.valueOf(msType));
                        editor.apply();
                        if (msType == 21) {
//                                    Log.e("Web===", text);
                            String imgUrl = obj.getString("msgface");
                            money21 = obj.getInt("msgdesp");
                            String goumaiMsg = obj.getString("msgcontent");
//                            URL url = new URL(imgUrl);
//                            HttpURLConnection imageconn = (HttpURLConnection) url.openConnection();
//                            imageconn.setReadTimeout(50000);
//                            InputStream inputStream = imageconn.getInputStream();
//                            Bitmap bm = BitmapFactory.decodeStream(inputStream);
                            Log.e("Web===", imgUrl);
                            InfoVO infoVO = new InfoVO();
                            infoVO.type = InfoVO.TYPE_TWO;
//                            infoVO.userPic = bm;
                            infoVO.userIfo1 = R.mipmap.ding;
                            infoVO.msg = goumaiMsg;
                            infList.add(infoVO);
                            moneGou = (String) gwcMoney.getText();
                            Log.e("Web===", infList.size() + "");
//                            inputStream.close();
                            Message msg3 = mHandler1.obtainMessage();
                            msg3.what = MSG_ID_3;
                            msg3.obj = moneGou;
                            msg3.obj = money21;
                            mHandler1.sendMessage(msg3);

                        } else if (msType == 20) {
                            String imgUrl = obj.getString("msgface");
                            String goumaiMsg = obj.getString("msgcontent");
//                            URL url = new URL(imgUrl);
//                            HttpURLConnection imageconn = (HttpURLConnection) url.openConnection();
//                            imageconn.setReadTimeout(50000);
//                            InputStream inputStream = imageconn.getInputStream();
//                            Bitmap bm = BitmapFactory.decodeStream(inputStream);
                            InfoVO infoVO22 = new InfoVO();
                            infoVO22.type = InfoVO.TYPE_TWO;
//                            infoVO22.userPic = bm;
                            infoVO22.userIfo1 = R.mipmap.ding;
                            infoVO22.msg = goumaiMsg;
                            infList.add(infoVO22);
//                            inputStream.close();


                        } else if (msType == 22) {
                            String imgUrl = obj.getString("msgface");
                            money21 = obj.getInt("msgdesp");
                            String goumaiMsg = obj.getString("msgcontent");
//                            URL url = new URL("http://img1.weibiz.cn/" + imgUrl);
//                            HttpURLConnection imageconn = (HttpURLConnection) url.openConnection();
//                            imageconn.setReadTimeout(50000);
//                            InputStream inputStream = imageconn.getInputStream();
//                            //压缩，用于节省BITMAP内存空间--解决BUG的关键步骤
//                            BitmapFactory.Options opts = new BitmapFactory.Options();
//                            opts.inSampleSize = 2;    //这个的值压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰
//
//                            Bitmap bm = BitmapFactory.decodeStream(inputStream);
//返回原图解码之后的bitmap对象
                            InfoVO infoVO22 = new InfoVO();
                            infoVO22.type = InfoVO.TYPE_TWO;
//                            infoVO22.userPic = bm;
                            infoVO22.userIfo1 = R.mipmap.ding;
                            infoVO22.msg = goumaiMsg;
                            infList.add(infoVO22);
                            moneGou = (String) gwcMoney.getText();
                            Log.e("Web===", infList.size() + "");
//                            inputStream.close();
                            Message msg3 = mHandler1.obtainMessage();
                            msg3.what = MSG_ID_3;
                            msg3.obj = moneGou;
                            msg3.obj = money21;
                            mHandler1.sendMessage(msg3);
                        } else if (msType == 10) {
                            imgUrl = obj.getString("msgface");
                            String userNme = obj.getString("msgname");
                            String msg = obj.getString("msgcontent");
                            int buyNum = obj.getInt("msgdesp");
//                            ResTou();
//                            URL url = new URL(imgUrl);
//                            HttpURLConnection imageconn = (HttpURLConnection) url.openConnection();
//                            imageconn.setReadTimeout(50000);
//                            InputStream inputStream = imageconn.getInputStream();
//                            bm = BitmapFactory.decodeStream(inputStream);

                            System.gc();
                            if (buyNum == 0) {
                                InfoVO inf = new InfoVO();
                                inf.type = InfoVO.TYPE_ONE;
//                                inf.userPic = bm;
                                inf.userName = userNme;
                                inf.userIfo1 = R.mipmap.fs1;
                                inf.userIfo2 = R.mipmap.fs2;
                                inf.msg = msg;
                                infList.add(inf);
                            } else /*if (buyNum >= 1)*/ {
                                InfoVO inf1 = new InfoVO();
                                inf1.type = InfoVO.TYPE_THREE;
//                                inf1.userPic = bm;
                                inf1.userName = userNme;
                                inf1.userIfo1 = R.mipmap.fs1;
                                inf1.userIfo2 = R.mipmap.fs4;
                                inf1.dunshou = "×" + buyNum;
                                inf1.msg = msg;
                                infList.add(inf1);
                            }
//                            inputStream.close();
                        } else if (msType == 30) {
                            String imgUrl = obj.getString("msgface");
                            String userNme = obj.getString("msgname");
                            String msg = obj.getString("msgcontent");
//                            URL url = new URL(imgUrl);
//                            HttpURLConnection imageconn = (HttpURLConnection) url.openConnection();
//                            imageconn.setReadTimeout(50000);
//                            InputStream inputStream = imageconn.getInputStream();
//                            Bitmap bm = BitmapFactory.decodeStream(inputStream);
                            InfoVO infoVO = new InfoVO();
                            infoVO.type = InfoVO.TYPE_ONE;
//                            infoVO.userPic = bm;
                            infoVO.userName = userNme;
                            infoVO.userIfo1 = R.mipmap.fs1;
                            infoVO.userIfo2 = R.mipmap.fs3;
                            infoVO.msg = msg;
                            infList.add(infoVO);
//                            inputStream.close();
                        } else if (msType == 31) {
                            String imgUrl = obj.getString("msgface");
                            String userNme = obj.getString("msgname");
                            String msg = obj.getString("msgcontent");
//                            URL url = new URL(imgUrl);
//                            HttpURLConnection imageconn = (HttpURLConnection) url.openConnection();
//                            imageconn.setReadTimeout(50000);
//                            InputStream inputStream = imageconn.getInputStream();
//                            Bitmap bm = BitmapFactory.decodeStream(inputStream);
                            InfoVO infoVO = new InfoVO();

                            infoVO.type = InfoVO.TYPE_FOR;
//                            infoVO.userPic = bm;
                            infoVO.userName = userNme;
                            infoVO.userIfo1 = R.mipmap.fs1;
                            infoVO.userIfo2 = R.mipmap.fs3;
                            infoVO.msg = msg;
                            infList.add(infoVO);
//                            inputStream.close();
                        } else if (msType == 90) {
                            String tuijianURL = obj.getString("msgcontent");
                            URL url = new URL("http://img1.weibiz.cn/" + tuijianURL);
                            HttpURLConnection imageconn = (HttpURLConnection) url.openConnection();
                            imageconn.setReadTimeout(50000);
                            InputStream inputStream = imageconn.getInputStream();
                            tuijiantupian = BitmapFactory.decodeStream(inputStream);
                            inputStream.close();
                            Message msg5 = mHandler1.obtainMessage();
                            msg5.what = MSG_ID_5;
                            msg5.obj = tuijiantupian;
                            mHandler1.sendMessage(msg5);
                        } else if (msType == 80) {
                            Message msg4 = mHandler1.obtainMessage();
                            msg4.what = MSG_ID_4;
                            msg4.arg1 = 10000;
                            mHandler1.sendMessage(msg4);

                        } else if (msType == -1) {
//                            Log.e("Web===", "我在呢-------------");
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(PreviewActivity.this, "我在呢....", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                        }

                    }
//

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg1 = mHandler1.obtainMessage();
                msg1.what = MSG_ID_1;
                msg1.obj = infList;
                mHandler1.sendMessage(msg1);
            }


        });
        thread.start();
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
    }

    /**
     * 断开重连机制
     *
     * @author zhangchunzhao
     * @date 2017/10/10 0010
     */

    //状态回调
    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {


        @Override
        public void onInfo(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Log.d("Web===", "KSY_STREAMER_CAMERA_INIT_DONE");
                    setCameraAntiBanding50Hz();
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                    Log.d("Web===", "KSY_STREAMER_OPEN_STREAM_SUCCESS");
                    break;
                case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                    Log.d("Web===", "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
                    Toast.makeText(PreviewActivity.this, "Network not good!",
                            Toast.LENGTH_SHORT).show();
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                    Log.d("Web===", "BW raise to " + msg1 / 1000 + "kbps");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                    Log.d("Web===", "BW drop to " + msg1 / 1000 + "kpbs");
                    break;
                default:
                    Log.d("Web===", "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };
    //错误回调
    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
                    Log.d("Web===", "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
                    Log.d("Web===", "KSY_STREAMER_ERROR_CONNECT_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
                    Log.d("Web===", "KSY_STREAMER_ERROR_PUBLISH_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
                    Log.d("Web===", "KSY_STREAMER_ERROR_CONNECT_BREAKED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Log.d("Web===", "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d("Web===", "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Log.d("Web===", "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d("Web===", "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Log.d("Web===", "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Log.d("Web===", "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Log.d("Web===", "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Log.d("Web===", "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Log.d("Web===", "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Log.d("Web===", "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                default:
                    Log.d("Web===", "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
//                    mStreamer.stopCameraPreview();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCameraPreviewWithPermCheck();

                        }
                    }, 5000);
                    break;
                default:
                    mStreamer.stopStream();
                    Toast.makeText(PreviewActivity.this, "当前网络质量不佳...", Toast.LENGTH_SHORT).show();
                    stopLive.setImageResource(R.drawable.raw);
                    state.setImageResource(R.drawable.ready);
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStreamer.startStream();
                            wsManager.sendMessage("{\"msgname\":" + phonN + ",\"msgcontent\":\"\"rtmp://58.56.9.249:1935/live/\"" + id + zhuboNum + "\",\"msgtype\":60,\"msgto\":1,\"msgdesp\":\"A\"}");
                            stopLive.setImageResource(R.drawable.raw2);
                            state.setImageResource(R.drawable.live);
                        }
                    }, 3000);
                    break;
            }
        }
    };


    // Example to handle camera related operation
    private void setCameraAntiBanding50Hz() {
        Camera.Parameters parameters = mStreamer.getCameraCapture().getCameraParameters();
        if (parameters != null) {
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            mStreamer.getCameraCapture().setCameraParameters(parameters);
        }
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
        mStreamer.stopStream();
        mStreamer.release();
        thread.interrupt();
        mHandler1.removeCallbacks(thread);

    }

    /**
     * @return if we have the camera permission
     */
    public boolean isCameraGranted() {
        return ContextCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED;
    }

}
