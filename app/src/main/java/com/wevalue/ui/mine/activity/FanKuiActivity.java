package com.wevalue.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.BitmapToBase64;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.utils.ZipBitmapUtil;
import com.wevalue.view.ActionSheetDialog;
import com.wevalue.view.ActionSheetDialog.OnSheetItemClickListener;
import com.wevalue.view.ActionSheetDialog.SheetItemColor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 意见反馈界面
 */
public class FanKuiActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_back;
    private TextView tv_head_right;
    private ImageView iv_img_1;
    private ImageView iv_img_2;
    private ImageView iv_img_3;
    private ImageView iv_img_delete_1;
    private ImageView iv_img_delete_2;
    private ImageView iv_img_delete_3;
    private TextView tv_add_img, tv_head_title;
    private TextView tv_huojiang_list;
    private EditText et_tel, et_context, et_name;
    private RelativeLayout rl_img_1, rl_img_2, rl_img_3;
    private LinearLayout ll_img_list;

    private String imgBase64_1 = "";
    private String imgBase64_2 = "";
    private String imgBase64_3 = "";
    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_kui);


        WindowManager wm = this.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText("提交");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_img_1 = (ImageView) findViewById(R.id.iv_img_1);
        iv_img_2 = (ImageView) findViewById(R.id.iv_img_2);
        iv_img_3 = (ImageView) findViewById(R.id.iv_img_3);
        iv_img_delete_1 = (ImageView) findViewById(R.id.iv_img_delete_1);
        iv_img_delete_2 = (ImageView) findViewById(R.id.iv_img_delete_2);
        iv_img_delete_3 = (ImageView) findViewById(R.id.iv_img_delete_3);
        rl_img_1 = (RelativeLayout) findViewById(R.id.rl_img_1);
        rl_img_2 = (RelativeLayout) findViewById(R.id.rl_img_2);
        rl_img_3 = (RelativeLayout) findViewById(R.id.rl_img_3);
        ll_img_list = (LinearLayout) findViewById(R.id.ll_img_list);

        tv_add_img = (TextView) findViewById(R.id.tv_add_img);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_huojiang_list = (TextView) findViewById(R.id.tv_huojiang_list);
        tv_head_title.setText("意见反馈");
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_context = (EditText) findViewById(R.id.et_context);
        et_name = (EditText) findViewById(R.id.et_name);

        iv_back.setOnClickListener(this);
        tv_add_img.setOnClickListener(this);
        iv_img_delete_1.setOnClickListener(this);
        iv_img_delete_2.setOnClickListener(this);
        iv_img_delete_3.setOnClickListener(this);
        tv_huojiang_list.setOnClickListener(this);


        iv_img_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_img_1.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.add_picture).getCurrent().getConstantState()) {

                    LogUtils.e("第一个被点击了");

                } else {
                    LogUtils.e("他们图片不一样");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_img:
                addImg();
                break;
            case R.id.tv_huojiang_list:
                Intent intent = new Intent(FanKuiActivity.this, WebActivity.class);
                intent.putExtra("url", RequestPath.GET_WEIZHIREWARD);
                intent.putExtra("isWho", 4);
                startActivity(intent);
                break;
            case R.id.iv_img_delete_1:
                rl_img_1.setVisibility(View.GONE);
                iv_img_1.setImageResource(R.mipmap.add_picture);
                iv_img_delete_1.setVisibility(View.GONE);
                imgBase64_1 = "";
                break;
            case R.id.iv_img_delete_2:
                rl_img_2.setVisibility(View.GONE);
                iv_img_2.setImageResource(R.mipmap.add_picture);
                iv_img_delete_2.setVisibility(View.GONE);
                imgBase64_2 = "";
                break;
            case R.id.iv_img_delete_3:
                rl_img_3.setVisibility(View.GONE);
                iv_img_3.setImageResource(R.mipmap.add_picture);
                iv_img_delete_3.setVisibility(View.GONE);
                imgBase64_3 = "";
                break;
            case R.id.tv_head_right:
                postOpinion();
                break;

        }

    }


    /**
     * 提交反馈
     */
    private void postOpinion() {
        String content = et_context.getText().toString().trim();
        String tel = et_tel.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ShowUtil.showToast(FanKuiActivity.this, "您还没输入宝贵的意见!");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ShowUtil.showToast(FanKuiActivity.this, "请输入您的姓名!");
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            ShowUtil.showToast(FanKuiActivity.this, "请输入您的联系电话!");
            return;
        }

        String url = RequestPath.POST_ADDADVICE;

        Map<String, Object> map = new HashMap<>();

        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("content", content);
        map.put("name", name);
        map.put("phone", tel);
        map.put("adviceimg1", imgBase64_1);
        map.put("adviceimg2", imgBase64_2);
        map.put("adviceimg3", imgBase64_3);

        NetworkRequest.postRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(FanKuiActivity.this, obj.getString("message"));
                        finish();
                    } else {
                        ShowUtil.showToast(FanKuiActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(FanKuiActivity.this, content);
            }
        });


    }

    /**
     * 选择拍照或相册
     */
    private void addImg() {

        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("相机拍照", SheetItemColor.Grey,
                        new OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

                            }
                        }).addSheetItem("相册选择", SheetItemColor.Grey, new OnSheetItemClickListener() {
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
                        if (iv_img_1.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.add_picture).getCurrent().getConstantState()) {

                            iv_img_1.setImageBitmap(bitmap);
                            iv_img_delete_1.setVisibility(View.VISIBLE);
                            imgBase64_1 = BitmapToBase64.bitmapToBase64(bitmap);
                            rl_img_1.setVisibility(View.VISIBLE);

                        } else if (iv_img_2.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.add_picture).getCurrent().getConstantState()) {

                            iv_img_2.setImageBitmap(bitmap);
                            iv_img_delete_2.setVisibility(View.VISIBLE);
                            imgBase64_2 = BitmapToBase64.bitmapToBase64(bitmap);
                            rl_img_2.setVisibility(View.VISIBLE);

                        } else if (iv_img_3.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.add_picture).getCurrent().getConstantState()) {

                            iv_img_3.setImageBitmap(bitmap);
                            iv_img_delete_3.setVisibility(View.VISIBLE);
                            imgBase64_3 = BitmapToBase64.bitmapToBase64(bitmap);
                            rl_img_3.setVisibility(View.VISIBLE);

                        }

                        saveMyBitmap(getSDPath() + "/微值/" + PHOTO_FILE_NAME, bitmap);

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
                                bitmap = BitmapFactory.decodeFile(path);
                                bitmap = ZipBitmapUtil.reduce(bitmap, width, height, true);
                                if (iv_img_1.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.add_picture).getCurrent().getConstantState()) {
                                    iv_img_1.setImageBitmap(bitmap);
                                    iv_img_delete_1.setVisibility(View.VISIBLE);
                                    imgBase64_1 = BitmapToBase64.bitmapToBase64(bitmap);
                                    rl_img_1.setVisibility(View.VISIBLE);

                                } else if (iv_img_2.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.add_picture).getCurrent().getConstantState()) {

                                    iv_img_2.setImageBitmap(bitmap);
                                    iv_img_delete_2.setVisibility(View.VISIBLE);
                                    imgBase64_2 = BitmapToBase64.bitmapToBase64(bitmap);
                                    rl_img_2.setVisibility(View.VISIBLE);

                                } else if (iv_img_3.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.add_picture).getCurrent().getConstantState()) {

                                    iv_img_3.setImageBitmap(bitmap);
                                    iv_img_delete_3.setVisibility(View.VISIBLE);
                                    imgBase64_3 = BitmapToBase64.bitmapToBase64(bitmap);
                                    rl_img_3.setVisibility(View.VISIBLE);
                                }
                                saveMyBitmap(getSDPath() + "/微值/" + PHOTO_FILE_NAME, bitmap);
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
    public void saveMyBitmap(String bitName, Bitmap mBitmap) {
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
