package com.wevalue.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wevalue.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by xox on 2017/3/2.
 */

public class AudioView extends RelativeLayout implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private String url = "https://mp.wzbz.cn/upload/social/audio/201702252202186444361.mp3";
    private Context context;

    public AudioView(Context context) {
        super(context);
        initPlayerView(context);
    }

    public AudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPlayerView(context);
    }

    private void initPlayerView(Context context) {
        this.context = context;
        setOnClickListener(this);
        //重置 mediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        } else {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setOnCompletionListener(completionListener);
    }

    /**
     * 播放视频
     **/
    public void player(String url) {
        try {
            this.url = url;
             /* 重置多媒体 */
            mediaPlayer.reset();
            // 通过Uri解析一个网络地址
            Uri uri = Uri.parse(url);
            mediaPlayer.setDataSource(context, uri);
            /* 是否单曲循环 */
            mediaPlayer.setLooping(false);
            /* 准备播放 */
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //mediaPlayer.reset();
        }
    };

    /**
     * 停止 重置 释放
     **/
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
        }
    }

    /**
     * 暂停
     **/
    public void pauseMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    /*****/
    MediaPlayer.OnPreparedListener listener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

        }
    };
}
