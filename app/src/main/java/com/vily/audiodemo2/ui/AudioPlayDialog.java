package com.vily.audiodemo2.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vily.audiodemo2.R;
import com.vily.audiodemo2.TimeUtil;

import java.io.File;
import java.util.Timer;


/**
 * description :
 * Author : Vily
 * Date : 2018/08/07
 * Time : 15:07
 */

public class AudioPlayDialog {

    private Context mContext;
    private AlertDialog mDialog;
    private LayoutInflater mInflater;
    private View mView;
    private TextView mTv_start;
    private TextView mTv_finish;
    private AppCompatSeekBar mSeekBar;
    private TextView mTv_audio_play;
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private boolean isSeekBarChanging;
    private File mFile;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("--------getCurrentPosition："+mMediaPlayer.getCurrentPosition());
            if(msg.what==1){
                if(mMediaPlayer!=null){
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                }

                if(mMediaPlayer!=null && mSeekBar.getProgress()!=mMediaPlayer.getDuration()){
                    sendEmptyMessageDelayed(1,10);
                }

            }

        }
    };

    public AudioPlayDialog(Context context, File media) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

        this.mFile=media;

        init();

        initData();
        listener();
    }

    private void init() {
        mDialog = new AlertDialog.Builder(mContext, R.style.CalendarDialog).create();
        mView = mInflater.inflate(R.layout.audio_play_dialog, null);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        mDialog.show();
        //设置dialog弹出后会点击屏幕或物理返回键，dialog不消失
        mDialog.setCanceledOnTouchOutside(false);
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

        mTv_start = view.findViewById(R.id.tv_start);
        mTv_finish = view.findViewById(R.id.tv_finish);
        mSeekBar = view.findViewById(R.id.sb_audio);
        mTv_audio_play = view.findViewById(R.id.tv_audio_play);


    }

    public void show() {
        mDialog.show();
    }


    private void listener() {

        mTv_audio_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mHandler.removeCallbacksAndMessages(null);
                mMediaPlayer = null;
                mDialog.dismiss();

            }
        });


        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(mOnDismissLis!=null){
                    mOnDismissLis.onDismissLis("----");
                }
            }
        });



    }

    private OnDismissLis mOnDismissLis;

    public interface OnDismissLis {
        void onDismissLis(String path);
    }

    public void setOnDismissLis(OnDismissLis onDismissLis) {
        mOnDismissLis = onDismissLis;
    }


    private void initData() {


        System.out.println("-----------走进来了吗");

        final int currentPosition = 0;
        //实例化媒体播放器
        mMediaPlayer = new MediaPlayer();

        mSeekBar.setOnSeekBarChangeListener(new MySeekBar());


        if (mFile.exists()) {
            try {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
                mMediaPlayer.setDataSource(mFile.getAbsolutePath());//设置mp3数据源




                mMediaPlayer.prepareAsync();//数据缓冲
                /*监听缓存 事件，在缓冲完毕后，开始播放*/
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        mp.seekTo(currentPosition);

                        mSeekBar.setMax(mMediaPlayer.getDuration());
                        mHandler.sendEmptyMessage(1);
                        mTv_finish.setText(TimeUtil.getMinAndSecond(mMediaPlayer.getDuration()));
                    }
                });


            } catch (Exception e) {
                Toast.makeText(mContext, "播放错误", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                System.out.println(e);
            }
        } else {
            Toast.makeText(mContext, "文件错误", Toast.LENGTH_LONG).show();
        }
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {



        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {


            if(mMediaPlayer!=null){
                if(  progress>mMediaPlayer.getDuration()-50){

                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mHandler.removeCallbacksAndMessages(null);
                    mMediaPlayer = null;
                    mDialog.dismiss();

                }else{
                    mTv_start.setText(TimeUtil.getMinAndSecond(mMediaPlayer.getCurrentPosition()));
                    if(!mMediaPlayer.isPlaying()){

                        mMediaPlayer.start();
                        mMediaPlayer.seekTo(progress);
                    }
                }


            }


        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mMediaPlayer.seekTo(seekBar.getProgress());

        }

    }


}
