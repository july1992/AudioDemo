package com.vily.audiodemo2.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.vily.audiodemo2.AudioRecordManager;


public class RecordAudioView extends AppCompatButton {

    private static final String TAG = "RecordAudioView";

    private Context context;
    private IRecordAudioListener recordAudioListener;
    private AudioRecordManager audioRecordManager;
    private boolean isCanceled;
    private float downPointY;
    private static final float DEFAULT_SLIDE_HEIGHT_CANCEL = 150;
    private boolean isRecording=false;
    private long mDown=0;


    public RecordAudioView(Context context) {
        super(context);
        initView(context);
    }

    public RecordAudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecordAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        audioRecordManager = AudioRecordManager.getInstance();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        super.onTouchEvent(event);
        if(recordAudioListener != null){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    long interval=2000;
                    System.out.println("--------mdown:"+mDown);

                    System.out.println("--------curr:"+ System.currentTimeMillis());
                    if(mDown!=0){
                        interval = System.currentTimeMillis() - mDown;
                    }
                    mDown = System.currentTimeMillis();
                    if(interval<2000){

                        Toast.makeText(getContext(),"2次按下间隔不能太短",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    System.out.println("-----------interval"+interval);
                    setSelected(true);
                    downPointY = event.getY();
                    recordAudioListener.onFingerPress();
                    startRecordAudio();
                    break;
                case MotionEvent.ACTION_UP:
                    setSelected(false);
                    onFingerUp();
                    break;
                case MotionEvent.ACTION_MOVE:
                    onFingerMove(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isCanceled = true;
                    onFingerUp();
                    break;
                default:

                    break;
            }
        }
        return true;
    }

    /**
     * 手指抬起,可能是取消录制也有可能是录制成功
     */
    private void onFingerUp(){
        if(isRecording){
            if(isCanceled){
                isRecording = false;
                audioRecordManager.cancelRecord();
                recordAudioListener.onRecordCancel();
            }else{
                stopRecordAudio();
            }
        }
    }

    private void onFingerMove(MotionEvent event){
        float currentY = event.getY();
        isCanceled = checkCancel(currentY);
        if(isCanceled){
            recordAudioListener.onSlideTop();
        }else{
            recordAudioListener.onFingerPress();
        }
    }

    private boolean checkCancel(float currentY){
        return downPointY - currentY >= DEFAULT_SLIDE_HEIGHT_CANCEL;
    }

    /**
     * 检查是否ready录制,如果已经ready则开始录制
     */
    private void startRecordAudio() throws RuntimeException {
        boolean isPrepare = recordAudioListener.onRecordPrepare();
        if(isPrepare){
            String audioFileName = recordAudioListener.onRecordStart();

            Log.i(TAG, "startRecordAudio: 已经准备就绪");
            //准备就绪开始录制
            try{
                audioRecordManager.init(audioFileName);
                audioRecordManager.startRecord();
                isRecording = true;
            }catch (Exception e){
                this.recordAudioListener.onRecordCancel();
            }
        }
    }

    /**
     * 停止录音
     */
    private void stopRecordAudio() throws RuntimeException {
        if(isRecording){
            Log.i(TAG, "stopRecordAudio: 停止录制");
            try {
                isRecording = false;
                audioRecordManager.stopRecord();
                this.recordAudioListener.onRecordStop();
            }catch (Exception e){
                this.recordAudioListener.onRecordCancel();
            }
        }
    }

    /**
     * 需要设置IRecordAudioStatus,来监听开始录音结束录音等操作,并对权限进行处理
     * @param recordAudioListener
     */
    public void setRecordAudioListener(IRecordAudioListener recordAudioListener) {
        this.recordAudioListener = recordAudioListener;
    }

    public void invokeStop(){
        onFingerUp();
    }

    public interface IRecordAudioListener {
        boolean onRecordPrepare();
        String onRecordStart();
        boolean onRecordStop();
        boolean onRecordCancel();
        void onSlideTop();
        void onFingerPress();
    }
}
