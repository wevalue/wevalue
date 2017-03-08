package com.wevalue.ui.world.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-07-06.
 */
public class PlayActivity extends BaseActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{



    private ImageView iv_back;
    private TextView tv_head_title;

    private MediaPlayer mMediaPlayer=null;//媒体播放器
    private AudioManager mAudioManager=null;//声音管理器
    private Button mPlayButton=null;
    private Button mPauseButton=null;
    private Button mStopButton=null;
    private SeekBar mSoundSeekBar=null;
    private int maxStreamVolume;//最大音量
    private int currentStreamVolume;//当前音量
    private int setStreamVolume;//设置的音量
    private String fileurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
    }

    private void initView() {
        fileurl = getIntent().getStringExtra("url");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        iv_back.setOnClickListener(this);
        tv_head_title.setText("播放界面");

        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer=MediaPlayer.create(PlayActivity.this, Uri.parse(fileurl));//加载res/raw的happyis.mp3文件;
//        mMediaPlayer=MediaPlayer.create(PlayActivity.this, Uri.parse(RequestPath.SERVER_PATH+fileurl));//加载res/raw的happyis.mp3文件
        mAudioManager=(AudioManager)this.getSystemService(AUDIO_SERVICE);

        mPlayButton=(Button)findViewById(R.id.Play);
        mPauseButton=(Button)findViewById(R.id.Pause);
        mStopButton=(Button)findViewById(R.id.Stop);
        mSoundSeekBar=(SeekBar)findViewById(R.id.SoundSeekBar);
        mPlayButton.setOnClickListener(this);
        mPauseButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);

        maxStreamVolume=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentStreamVolume=mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundSeekBar.setMax(maxStreamVolume);
        mSoundSeekBar.setProgress(currentStreamVolume);
        mSoundSeekBar.setOnSeekBarChangeListener(this);
    }

    private void play()throws IOException {

        LogUtils.e("fileurl = "+fileurl);
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(fileurl);
        mMediaPlayer.prepare();
        mMediaPlayer.start();//播放
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        try {
            switch (v.getId()) {
                case R.id.Play:
                    play();
                    break;
                case R.id.Pause:
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }

                    break;
                case R.id.Stop:
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    try {
                        mMediaPlayer.prepare();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMediaPlayer.seekTo(0);
                    break;
                case R.id.iv_back:
                    finish();
                    break;
            }
        }catch (Exception e) {//抛出异常
            LogUtils.e( "播放界面的错误="+e.toString());
        }
    }

    //进度条变化
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub
        System.out.println("progress:"+ String.valueOf(progress));
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

       mMediaPlayer.release();

        super.onDestroy();
    }



    private void getFile(){

        String url =  RequestPath.GET_GETVIDEO;

        Map<String,String> map = new HashMap<>();

        map.put("code",RequestPath.CODE);
//        map.put("userid", SharedPreferencesUtil.getUid(this));

        NetworkRequest.getRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("result").equals("0")){
//                        ShowUtil.showToast(PlayActivity.this, obj.getString("message"));
//                        finish();
                        JSONArray data = obj.getJSONArray("data");
                        if(data!=null&&data.length()>0){
                            for (int i = 0; i <data.length() ; i++) {
                                JSONObject b = data.getJSONObject(i);
                                if(!TextUtils.isEmpty(b.getString("notevideo"))){

                                    String str= b.getString("notevideo");
                                    fileurl =RequestPath.SERVER_PATH+str;

                                }

                            }
                        }


                    }else {
                        ShowUtil.showToast(PlayActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(PlayActivity.this, content);
            }
        });


    }
}
