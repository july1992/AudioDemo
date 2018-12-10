package com.vily.audiodemo2;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.download.UpLoadCallback;
import com.tamic.novate.request.NovateRequestBody;
import com.tamic.novate.util.Utils;
import com.vily.audiodemo2.ui.AudioDialog;
import com.vily.audiodemo2.ui.AudioPlayDialog;
import com.vily.audiodemo2.ui.CommonSoundItemView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CommonSoundItemView mPp_sound_item_view;
    private ImageView mPp_iv_abandon_sound;
    private Button mBtn;
    private AudioDialog mAudioDialog;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_test);
        mPp_sound_item_view = findViewById(R.id.pp_sound_item_view);
        mPp_iv_abandon_sound =  findViewById(R.id.pp_iv_abandon_sound);
        mBtn = findViewById(R.id.btn);

//        mPp_sound_item_view.setOnClickListener(this);
        mPp_iv_abandon_sound.setOnClickListener(this);
        mBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        String[] pp = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        ActivityCompat.requestPermissions(MainActivity.this, pp, 10);

        String[] pp2 = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(MainActivity.this, pp2,20);

        switch (view.getId()){
            case R.id.pp_iv_abandon_sound :
                mPp_sound_item_view.clearData();
                mPp_sound_item_view.setVisibility(View.GONE);
                mPp_iv_abandon_sound.setVisibility(View.GONE);
                mBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.btn :

                mAudioDialog = new AudioDialog(MainActivity.this);

                mAudioDialog.setOnDismissLis(new AudioDialog.OnDismissLis() {
                    @Override
                    public void onDismissLis(String path) {
                        if(!TextUtils.isEmpty(path)){

                            mPath=path;
                            System.out.println("----------path:"+path);
                            AudioEntity entity = new AudioEntity();
                            entity.setUrl(path);
                            int duration = AudioPlaybackManager.getDuration(path);
                            if (duration <= 0) {
                                System.out.println("-----------录音时间 <= 0");

                                File tempFile = new File(path);
                                if (tempFile.exists()) {
                                    tempFile.delete();
                                    return;
                                }
                            } else {
                                entity.setDuration(duration / 1000);
                                mPp_sound_item_view.setSoundData(entity);
                                mPp_sound_item_view.setVisibility(View.VISIBLE);
                                mPp_iv_abandon_sound.setVisibility(View.VISIBLE);
                                mBtn.setVisibility(View.INVISIBLE);

                            }
                        }
                    }
                });
                break;
        }
    }

    public void upload(View view) {

        Novate mNovate = new Novate.Builder(this)
                .baseUrl("http://192.168.1.104:8089/vily2/test/")
                .build();

        System.out.println("-----------mpath:"+mPath);

        if (!TextUtils.isEmpty(mPath)) {
            // 上传音频
            File audioFile = new File(mPath);
            RequestBody audioRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), audioFile);
            NovateRequestBody novateRequestBody = Utils.createNovateRequestBody(audioRequestBody, new UpLoadCallback() {
                @Override
                public void onProgress(Object tag, final int progress, long speed, boolean done) {

                }
            });
            MultipartBody.Part parts  = MultipartBody.Part.createFormData("files", audioFile.getName(), novateRequestBody);


            String url="http://192.168.1.104:8089/vily2/test/";
            Novate novate = new Novate.Builder(this)
                    .baseUrl(url)
                    .build();
            MyAPI myAPI = mNovate.create(MyAPI.class);
            novate.call(myAPI.test(parts), new BaseSubscriber<Object>() {
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {

                }
            });
        }
    }

    public void play(View view) {
        String url = "/storage/emulated/0/Music/1534238039925.mp3";

        File media = new File(url);//由于是练习，就把mp3名称固定了
        AudioPlayDialog audioPlayDialog=new AudioPlayDialog(MainActivity.this,media);



    }
}
