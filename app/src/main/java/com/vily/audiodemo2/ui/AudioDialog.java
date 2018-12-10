package com.vily.audiodemo2.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vily.audiodemo2.R;
import com.vily.audiodemo2.StringUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * description :
 * Author : Vily
 * Date : 2018/08/07
 * Time : 15:07
 */

public class AudioDialog implements View.OnClickListener, RecordAudioView.IRecordAudioListener {

    private Context mContext;
    private AlertDialog mDialog;
    private LayoutInflater mInflater;
    private View mView;
    private ImageView mIvAbandonSound;
    private CommonSoundItemView mSoundItemView;
    private RelativeLayout mRecordBtn;
    private RecordAudioView mIv_recording;

    private RecordAudioView recordAudioView;
    private String audioFileName;
    private ImageView ivClose;
    private TextView tvRecordTips;
    private RelativeLayout layoutCancelView;
    private String[] recordStatusDescription;
//    private View contentView;
//    private View recordAudioContent;
    private TextView tvReplyName;
    private LineWaveVoiceView mHorVoiceView;
//    private View emptyView;

    public static final long DEFAULT_MAX_RECORD_TIME = 60000;
    public static final long DEFAULT_MIN_RECORD_TIME = 2000;
    protected static final int DEFAULT_MIN_TIME_UPDATE_TIME = 1000;



    public AudioDialog(Context context) {
        mContext = context;
        mInflater= LayoutInflater.from(context);
        mainHandler = new Handler();
        init();

        listener();
    }

    private void init() {
        mDialog = new AlertDialog.Builder(mContext, R.style.CalendarDialog).create();
        mView = mInflater.inflate(R.layout.commentbox_dialog, null);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        mDialog.show();
        //设置dialog弹出后会点击屏幕或物理返回键，dialog不消失
        mDialog.setCanceledOnTouchOutside(true);
//
        window.setContentView(mView);


        //获得window窗口的属性
        WindowManager.LayoutParams params = window.getAttributes();
        //设置窗口宽度为充满全屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        //设置窗口高度为包裹内容
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);

        initView(mView);
    }

    private void initView(View view) {

        recordAudioView =  view.findViewById(R.id.iv_recording);
        recordAudioView.setRecordAudioListener(this);
        ivClose = view.findViewById(R.id.close_record);
        ivClose.setOnClickListener(this);
        tvRecordTips =  view.findViewById(R.id.record_tips);
        layoutCancelView =  view.findViewById(R.id.pp_layout_cancel);
//        contentView = view.findViewById(R.id.record_content);
//        recordAudioContent = view.findViewById(R.id.layout_record_audio);
        mHorVoiceView = view.findViewById(R.id.horvoiceview);

        recordStatusDescription = new String[]{"按住录音","上滑动取消"};
    }

    public void show(){
        mDialog.show();
    }


    private void listener() {


    }

    private OnDismissLis mOnDismissLis;
    public interface OnDismissLis{
        void  onDismissLis(String path);
    }

    public void setOnDismissLis(OnDismissLis onDismissLis) {
        mOnDismissLis = onDismissLis;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.close_record){
            onBackPressed();
        }
    }

    @Override
    public boolean onRecordPrepare() {

        return true;
    }
    private long maxRecordTime = DEFAULT_MAX_RECORD_TIME;
    private long minRecordTime = DEFAULT_MIN_RECORD_TIME;
    private Timer timer;
    private TimerTask timerTask;
    private Handler mainHandler;
    private long recordTotalTime;
//    private EnterRecordAudioEntity entity;

    @Override
    public String onRecordStart() {
        recordTotalTime = 0;
        initTimer();
        timer.schedule(timerTask,0,DEFAULT_MIN_TIME_UPDATE_TIME);
//        audioFileName = CommonApp.getContext().getExternalCacheDir()+ File.separator + createAudioName();
        audioFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .getAbsolutePath() + File.separator+ createAudioName();
        System.out.println("-----------onRecordStart : audioFileName:"+audioFileName);
        mHorVoiceView.startRecord();
        return audioFileName;
    }

    @Override
    public boolean onRecordStop() {
        if(recordTotalTime >= minRecordTime){
            timer.cancel();
            onBackPressed();
            System.out.println("-------audioFileName:"+audioFileName);
            //录制完成发送EventBus通知
//            switch (entity.getSourceType()){
//                case AUDIO_FEED:

//                    MainThreadEvent event1 = new MainThreadEvent(EventBusConfig.SOUND_FEED_RECORD_FINISH,audioFileName);
//                    EventBus.getDefault().post(event1);
//                    break;
//                default:
//                    break;
//            }
        }else{
            onRecordCancel();
        }
        return false;
    }

    @Override
    public boolean onRecordCancel() {
        if(timer != null){
            timer.cancel();
        }
        updateCancelUi();
        return false;
    }
    private void updateCancelUi(){
        mHorVoiceView.setVisibility(View.INVISIBLE);
        tvRecordTips.setVisibility(View.VISIBLE);
        layoutCancelView.setVisibility(View.INVISIBLE);
        tvRecordTips.setText(recordStatusDescription[0]);
        mHorVoiceView.stopRecord();
        deleteTempFile();
    }

    private void deleteTempFile(){
        //取消录制后删除文件
        if(audioFileName != null){
            File tempFile = new File(audioFileName);
            if(tempFile.exists()){
                tempFile.delete();
            }
        }
    }
    @Override
    public void onSlideTop() {
        mHorVoiceView.setVisibility(View.INVISIBLE);
        tvRecordTips.setVisibility(View.INVISIBLE);
        layoutCancelView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFingerPress() {
        mHorVoiceView.setVisibility(View.VISIBLE);
        tvRecordTips.setVisibility(View.VISIBLE);
        tvRecordTips.setText(recordStatusDescription[1]);
        layoutCancelView.setVisibility(View.INVISIBLE);
    }

    private void initTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //每隔1000毫秒更新一次ui

                        recordTotalTime += 1000;
                        updateTimerUI();
                    }
                });
            }
        };
    }
    private void updateTimerUI(){
        if(recordTotalTime >= maxRecordTime){
            recordAudioView.invokeStop();
        }else{
            String string = String.format(" 倒计时 %s ", StringUtil.formatRecordTime(recordTotalTime,maxRecordTime));
            mHorVoiceView.setText(string);
        }
    }


    public void onBackPressed() {
        mHorVoiceView.stopRecord();
        mainHandler.removeCallbacksAndMessages(null);
        if(mOnDismissLis!=null){
            mOnDismissLis.onDismissLis(audioFileName);
        }
        mDialog.dismiss();

    }


    public String createAudioName(){
        long time = System.currentTimeMillis();
//        String fileName = time + ".amr";
//        String fileName = time + ".wav";
        String fileName = time + ".mp3";
        return fileName;
    }
}
