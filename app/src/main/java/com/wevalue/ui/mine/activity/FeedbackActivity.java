package com.wevalue.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.mine.adapter.FeedbackImgAdapter;
import com.wevalue.utils.BitmapToBase64;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.RegexUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.utils.ZipBitmapUtil;
import com.wevalue.view.ActionSheetDialog;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * * 意见反馈界面
 * Created by Administrator on 2016-09-24.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {


    private ImageView iv_back;
    private TextView tv_head_right, tv_head_title;
    private EditText et_context;
    private EditText et_true_name;
    private EditText et_tel_fba;
    private TextView tv_huojiang_list;


    private GridView gv_gridView_fba;
    private FeedbackImgAdapter mAdapter;
    private List<Bitmap> mBitmaps = new ArrayList<>();
    private List<String> mFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, StatisticsConsts.event_feedback);
    }

    private void initView() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_huojiang_list = (TextView) findViewById(R.id.tv_huojiang_list);
        tv_head_title.setText("意见反馈");
        tv_head_right.setText("提交");
        tv_head_right.setVisibility(View.VISIBLE);

        et_context = (EditText) findViewById(R.id.et_context);
        et_true_name = (EditText) findViewById(R.id.et_true_name);
        et_tel_fba = (EditText) findViewById(R.id.et_tel_fba);

        gv_gridView_fba = (GridView) findViewById(R.id.gv_gridView_fba);

        gv_gridView_fba.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBitmaps.size() == position) {
                    addImg();
                } else {

                }
            }
        });

        mAdapter = new FeedbackImgAdapter(mBitmaps, this);
        mAdapter.notifyDataSetChanged();
        gv_gridView_fba.setAdapter(mAdapter);

        iv_back.setOnClickListener(this);
        tv_head_right.setOnClickListener(this);
        tv_huojiang_list.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_huojiang_list:
                Intent intent = new Intent(FeedbackActivity.this, WebActivity.class);
                intent.putExtra("url", RequestPath.GET_WEIZHIREWARD);
                intent.putExtra("isWho", 4);
                startActivity(intent);
                break;
            case R.id.tv_head_right:
                if (!ButtontimeUtil.isFastDoubleClick()) {
                    postOpinion();
                }
                break;

        }
    }


    /**
     * 提交反馈
     */
    private void postOpinion() {
        String content = et_context.getText().toString().trim();
        String tel = et_tel_fba.getText().toString().trim();
        String name = et_true_name.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ShowUtil.showToast(FeedbackActivity.this, "提交内容为空!");
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            ShowUtil.showToast(FeedbackActivity.this, "请填写联系人信息，方便我们联系您!");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ShowUtil.showToast(FeedbackActivity.this, "请填写联系人信息，方便我们联系您!");
            return;
        }
        if (!RegexUtils.etPhoneRegex(tel) && !RegexUtils.etEmail(tel)) {
            ShowUtil.showToast(FeedbackActivity.this, "请填写正确的手机号或邮箱，方便我们联系您！");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("code", RequestPath.CODE);
        params.put("userid", SharedPreferencesUtil.getUid(this));
        params.put("content", content);
        params.put("phone", tel);
        params.put("name", name);
        for (int i = 0; i < mFiles.size(); i++) {
            params.put("adviceimg" + (i + 1), mFiles.get(i));
        }
        String url = RequestPath.POST_ADDADVICE;
        NetworkRequest.postRequest(url, params, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(FeedbackActivity.this, obj.getString("message"));
                        finish();
                        MobclickAgent.onEvent(FeedbackActivity.this, StatisticsConsts.event_feedbackDone);

                    } else {
                        ShowUtil.showToast(FeedbackActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(FeedbackActivity.this, content);
            }
        });


    }


    public void deleteImg(int index) {
        if (mBitmaps.size() > 0) {
            mBitmaps.remove(index);
            mFiles.remove(index);
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 选择拍照或相册
     */
    private void addImg() {

        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

                            }
                        }).addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

            }
        }).show();

    }

    private static final String PHOTO_FILE_NAME = "logo.jpg";
    /**
     * 拍照
     */
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    /**
     * 从相册中选择
     */
    private static final int PHOTO_REQUEST_GALLERY = 2;

    private Uri uri;
    private File tempFile;
    private Bitmap bitmap;

    /**
     * @author fengyaru TODO 简单描述该方法的实现功能
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
     * Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                return;
            }
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
                    try {

                        bitmap = data.getParcelableExtra("data");
                        mBitmaps.add(bitmap);
                        mFiles.add(BitmapToBase64.bitmapToBase64(bitmap));

                        mAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case PHOTO_REQUEST_GALLERY:
                    uri = data.getData();
                    try {
                        String[] pojo = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(uri, pojo, null, null, null);
                        if (cursor != null) {
//		                        ContentResolver cr = this.getContentResolver();
                            int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            String path = cursor.getString(colunm_index);
                            /***
                             * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
                             * 如果是图片格式的话，那么才可以
                             */
                            if (path.endsWith("jpg") || path.endsWith("png") || path.endsWith("jpeg") ||
                                    path.endsWith("JPG") || path.endsWith("PNG") || path.endsWith("JPEG")) {
//		                            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//		                            bitmap = comp(bitmap);
                                bitmap = ZipBitmapUtil.getimage(path);
                                mBitmaps.add(bitmap);
                                mFiles.add(BitmapToBase64.bitmapToBase64(bitmap));
                                mAdapter.notifyDataSetChanged();
//                                saveMyBitmap(getSDPath() + "/dbcooper/" + PHOTO_FILE_NAME, bitmap);

                            }
                        }

                    } catch (Exception e) {

                    }
                    break;
            }
        }
    }


    /**
     * Title:  saveMyBitmap<br>
     * Description: TODO  保存图片<br>
     *
     * @param bitName
     * @param mBitmap
     * @since JDK 1.7
     */
    public File saveMyBitmap(String bitName, Bitmap mBitmap) {
        File f = new File(bitName);
        File path = new File(getSDPath() + "/微值/");
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * Title: getSDPath<br>
     * Description: TODO 获取SD卡路径<br>
     *
     * @return
     * @since JDK 1.7
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();

    }

}
