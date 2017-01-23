package com.wevalue.ui.world.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.AudioAndVideoBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.release.ReleaseNoteActivity;
import com.wevalue.ui.world.adapter.AudioAndVideoAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-06-30.
 */
public class GetvAudioAndVideoActivity extends BaseActivity implements View.OnClickListener {


    private Cursor cursor;
    private ImageView iv_back;
    private TextView tv_head_title, tv_head_right;
    private GridView gv_gridView;
    private List<AudioAndVideoBean> mDatas;
    private AudioAndVideoAdapter mAdapter;
    public static int TYPE;
    private int index = -1;
    private MediaPlayer mMediaPlayer;//媒体播放器
    private Handler mHandler = new Handler();
    private ProgressDialog mProgressDialog;
    private TextView tv_id_tishi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getaudio_and_video);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载...");

        initView();
        String type = getIntent().getStringExtra("type");
        mProgressDialog.show();
        if (type.equals("video")) {
            TYPE = 2;
            tv_head_title.setText("选择视频");
            tv_id_tishi.setText("请上传30s以内的视频，超过30s的视频请登录PC端上传mp.wzbz.cn");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    getVideoList();
                }
            }.start();

        } else if (type.equals("audio")) {
            TYPE = 1;
            tv_head_title.setText("选择音频");
            tv_id_tishi.setText("请上传5分钟以内的音频，超过5分钟的音频请登录PC端上传mp.wzbz.cn");
            mMediaPlayer = new MediaPlayer();
//        mMediaPlayer=MediaPlayer.create(PlayActivity.this, Uri.parse(fileurl));//加载res/raw的happyis.mp3文件;
//        mMediaPlayer=MediaPlayer.create(PlayActivity.this, Uri.parse(RequestPath.SERVER_PATH+fileurl));//加载res/raw的happyis.mp3文件

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    getAudioList();
                }
            }.start();

        }


    }

    private void initView() {
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_id_tishi = (TextView) findViewById(R.id.tv_id_tishi);
        tv_head_right.setText("确定");
        tv_head_right.setVisibility(View.VISIBLE);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_head_right.setOnClickListener(this);

        gv_gridView = (GridView) findViewById(R.id.gv_gridView);
        gv_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                mDatas.get(position).setIsClick(1);
                for (int i = 0; i < mDatas.size(); i++) {
                    if (i != position) {
                        if (1 == mDatas.get(i).getIsClick()) {
                            mDatas.get(i).setIsClick(0);
                            break;
                        }
                    }

                }
                mAdapter.notifyDataSetChanged();

                if (TYPE == 1) {//音频

                    try {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        } else {
                            play(mDatas.get(position).getUrl());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (TYPE == 2) {//视频
                    Intent intent = new Intent(GetvAudioAndVideoActivity.this, Play_videoActivity.class);
                    intent.putExtra("url", mDatas.get(position).getUrl());
                    startActivity(intent);
                }

            }
        });

    }


    private void play(String fileurl) throws IOException {

        LogUtils.e("fileurl = " + fileurl);
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(fileurl);
        mMediaPlayer.prepare();
        mMediaPlayer.start();//播放
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (TYPE == 1) {
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
        }
    }

    @Override
    protected void onDestroy() {
        if (TYPE == 1) {
            mMediaPlayer.release();
        }
        super.onDestroy();

    }

    /**
     * 获取本地音频数据
     */
    private void getAudioList() {
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            int fileNum = cursor.getCount();
            AudioAndVideoBean aavb;
            mDatas = new ArrayList<>();
            LogUtils.e("index =" + fileNum);
            for (int counter = 0; counter < fileNum; counter++) {
                aavb = new AudioAndVideoBean();
                LogUtils.e("---------------mp3-------title is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                LogUtils.e("---------------mp3-------url is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));

                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)), 1);

                aavb.setIsClick(0);
                aavb.setUrl(url);
                aavb.setFielsize(size);
//                aavb.setVideoImg(bitmap);
                aavb.setDuration(duration);
                aavb.setName(name);
                LogUtils.e(aavb.getName() + "--------");
                mDatas.add(aavb);
                cursor.moveToNext();
            }
            Collections.reverse(mDatas);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                    mAdapter = new AudioAndVideoAdapter(mDatas, GetvAudioAndVideoActivity.this);
                    mAdapter.notifyDataSetChanged();
                    gv_gridView.setAdapter(mAdapter);
                }
            });
//            mAdapter = news AudioAndVideoAdapter(mDatas, this);
//            mAdapter.notifyDataSetChanged();
//            gv_gridView.setAdapter(mAdapter);
        }

        cursor.close();

    }

    /**
     * 获取本地视频数据
     */
    private void getVideoList() {

        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
//        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            int fileNum = cursor.getCount();

            AudioAndVideoBean aavb;
            mDatas = new ArrayList<>();
            for (int counter = 0; counter < fileNum; counter++) {
                aavb = new AudioAndVideoBean();
                LogUtils.e("----------------------title is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
                LogUtils.e("----------------------url is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                LogUtils.e("----------------------size is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                LogUtils.e("----------------------shujian is: " + cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));

                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)), 1);

                aavb.setIsClick(0);
                aavb.setUrl(url);
                aavb.setFielsize(size);
                aavb.setVideoImg(bitmap);
                aavb.setDuration(duration);
                aavb.setName("");
                mDatas.add(aavb);

                cursor.moveToNext();
            }
            Collections.reverse(mDatas);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                    mAdapter = new AudioAndVideoAdapter(mDatas, GetvAudioAndVideoActivity.this);
                    mAdapter.notifyDataSetChanged();
                    gv_gridView.setAdapter(mAdapter);
                }
            });

        }
        cursor.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 2 && data != null) {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);

                cursor.moveToFirst();
                LogUtils.e("----------------------url is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                cursor.close();
                LogUtils.e("uri =" + uri.toString());
            } else if (requestCode == 3 && data != null) {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);

                cursor.moveToFirst();
                LogUtils.e("---------222-------------url is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                cursor.close();
                LogUtils.e("uri 222 =" + uri.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_head_right:
                if (index == -1) {
                    if (TYPE == 2) {
                        ShowUtil.showToast(this, "请选择视频文件!");
                    } else if (TYPE == 1) {
                        ShowUtil.showToast(this, "请选择音频文件!");
                    }

                    return;
                }
//                File file = news File(mDatas.get(index).getUrl());
                if (TYPE == 2) {
                    // TODO: 2016/12/12 修改上传视频的时间
                    if (mDatas.get(index).getDuration() / 1000 > 30) {
                        ShowUtil.showToast(this, "请上传30s以内的视频，超过30s的视频请登录PC端上传mp.wzbz.cn");
                        return;
                    }
                    Intent intent = new Intent(this, ReleaseNoteActivity.class);
                    intent.putExtra("fileUrl", mDatas.get(index).getUrl());
                    intent.putExtra("isSendType", 1);
                    startActivity(intent);

                } else if (TYPE == 1) {
                    if (mDatas.get(index).getDuration() / 1000 > 5 * 60) {
                        ShowUtil.showToast(this, "请上传5分钟以内的音频，超过5分钟的音频请登录PC端上传mp.wzbz.cn");
                        return;
                    }
                    Intent intent = new Intent(this, ReleaseNoteActivity.class);
                    intent.putExtra("fileUrl", mDatas.get(index).getUrl());
                    intent.putExtra("isSendType", 2);
                    startActivity(intent);
                }

                finish();
//                upFileData();
                break;

        }
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private long getFileSize(File file) {
        long size = 0;

        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return size;
    }


    private void upFileData() {

        File file = new File(mDatas.get(index).getUrl());

        String url = RequestPath.POST_SENDVIDEO;

        Map<String, Object> map = new HashMap<>();

        map.put("code", RequestPath.CODE);
//        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("type", "" + TYPE);
        map.put("files", file);


        NetworkRequest.postRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(GetvAudioAndVideoActivity.this, obj.getString("message"));
                        finish();
                    } else {
                        ShowUtil.showToast(GetvAudioAndVideoActivity.this, obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(GetvAudioAndVideoActivity.this, content);
            }
        });
    }
}
