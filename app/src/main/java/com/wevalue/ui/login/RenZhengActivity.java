package com.wevalue.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.login.adapter.RenZhengImgAdapter;
import com.wevalue.utils.BitmapToBase64;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PictureHelper;
import com.wevalue.utils.RegexUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.utils.ZipBitmapUtil;
import com.wevalue.view.ActionSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016-07-20.
 */
public class RenZhengActivity extends BaseActivity implements View.OnClickListener {
    private ProgressDialog mProgressDialog;
    private LinearLayout ll_is_zuzhi;
    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_head_right;
    private TextView tv_prompt_content;
    private TextView tv_lianxiren;
    private TextView tv_determine_but;
    private EditText et_zuzhi_name;//组织名
    private EditText et_true_name;//联系人名
    private EditText et_ID_number;//身份证号
    private EditText et_tel_number;//手机号
    private EditText et_mailbox;//邮箱

    private GridView gv_gridView;
    private RenZhengImgAdapter mImgAdapter;
    private List<Bitmap> mImgList;
    public static List<String> mImgBase64List = new ArrayList<>();


    private String isWho;
    private long suijishu;
    private static final String PHOTO_FILE_NAME = "wevalue_img.jpg";
    private Bitmap bitmap;
    private int width;
    private int height;
    private String houxurenzheng = "0";
    private ProgressBar pb_pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng);
        houxurenzheng = getIntent().getStringExtra("houxurenzheng");
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        initView();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在上传认证信息...");
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        ll_is_zuzhi = (LinearLayout) findViewById(R.id.ll_is_zuzhi);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_lianxiren = (TextView) findViewById(R.id.tv_lianxiren);
        tv_prompt_content = (TextView) findViewById(R.id.tv_prompt_content);
        tv_determine_but = (TextView) findViewById(R.id.tv_determine_but);

        et_zuzhi_name = (EditText) findViewById(R.id.et_zuzhi_name);
        et_true_name = (EditText) findViewById(R.id.et_true_name);
        et_ID_number = (EditText) findViewById(R.id.et_ID_number);
        et_tel_number = (EditText) findViewById(R.id.et_tel_number);
        et_mailbox = (EditText) findViewById(R.id.et_mailbox);
        et_mailbox.setVisibility(View.GONE);

        gv_gridView = (GridView) findViewById(R.id.gv_gridView);

        iv_back.setOnClickListener(this);
        tv_determine_but.setOnClickListener(this);
        tv_head_right.setText(R.string.Determine);
        tv_head_right.setOnClickListener(this);
        tv_head_right.setVisibility(View.VISIBLE);
        isWho = getIntent().getStringExtra("isWho");
        if (isWho.equals("1")) {//个人认证
            tv_head_title.setText("个人认证");
            tv_prompt_content.setHint("（身份证正面照片、反面照片、本人手持身份证正面照片，并确保本人头像及身份证信息清晰完整）");
        } else if (isWho.equals("2")) {//组织认证
            ll_is_zuzhi.setVisibility(View.VISIBLE);
            String zhuzhi = getIntent().getStringExtra("zuzhiName");
            if (!TextUtils.isEmpty(zhuzhi)) {
                et_zuzhi_name.setText(zhuzhi);
                et_zuzhi_name.setFocusable(false);
                et_zuzhi_name.setFocusableInTouchMode(false);
            } else {
                if (!TextUtils.isEmpty(SharedPreferencesUtil.getNickname(this))) {
                    et_zuzhi_name.setText(SharedPreferencesUtil.getNickname(this));
                    et_zuzhi_name.setFocusable(false);
                    et_zuzhi_name.setFocusableInTouchMode(false);
                }
            }
            tv_head_title.setText("组织认证");
            tv_prompt_content.setHint("（营业执照、其他平台账号证明资料、联系人身份证正面照片、反面照片、本人手持身份证正面照片，并确保本人头像及身份证信息清晰完整。）");
            tv_lianxiren.setText("联系人");
            et_true_name.setHint("请输入联系人");
            et_ID_number.setHint("请输入联系人的身份证号码");
        } else if (isWho.equals("3")) {

        }
        mImgList = new ArrayList<>();
        mImgAdapter = new RenZhengImgAdapter(mImgList, RenZhengActivity.this, isWho, new RenZhengImgAdapter.DelPic() {
            @Override
            public void delPic(int position) {
                mImgList.remove(position);
                mImgAdapter.setmDatas(mImgList);
                mImgAdapter.notifyDataSetChanged();
            }
        });
        gv_gridView.setAdapter(mImgAdapter);

        gv_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mImgList.size()) {
                    //选择图片
                    addImg();
                } else {

                }
            }
        });
        pb_pb = (ProgressBar) findViewById(R.id.pb_pb);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mImgBase64List.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                RenZhengActivity.mImgBase64List.clear();
                finish();
                break;
            case R.id.tv_head_right:
                LogUtils.e("mImgBase64List = " + mImgBase64List.size());
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    returnData();
                }
                break;
        }
    }


    private void returnData() {

        String zuzhiName = et_zuzhi_name.getText().toString().trim();
        String trueName = et_true_name.getText().toString().trim();
        String IDnumber = et_ID_number.getText().toString().trim();
        String telNumber = et_tel_number.getText().toString().trim();
        String mailbox = et_mailbox.getText().toString().trim();

        if (TextUtils.isEmpty(zuzhiName) && isWho.equals("2")) {
            ShowUtil.showToast(this, "请输入组织名称!");
            return;
        }
        if (TextUtils.isEmpty(trueName)) {
            if (isWho.equals("2")) {
                ShowUtil.showToast(this, "请输入联系人名字!");
            } else {
                ShowUtil.showToast(this, "请输入您的名字!");
            }
            return;
        }
        if (TextUtils.isEmpty(IDnumber) || !RegexUtils.identity_card(IDnumber)) {
            if (isWho.equals("2")) {
                ShowUtil.showToast(this, "请正确输入联系人身份证号!");
            } else {
                ShowUtil.showToast(this, "请正确输入您的身份证号!");
            }
            return;
        }
        if (TextUtils.isEmpty(telNumber) || !RegexUtils.etPhoneRegex(telNumber)) {
            ShowUtil.showToast(this, "请输入正确的联系电话!");
            return;
        }
//        if (TextUtils.isEmpty(mailbox) || !RegexUtils.etEmail(mailbox)) {
//            ShowUtil.showToast(this, "请输入正确的邮箱号!");
//            return;
//        }
        if (isWho.equals("1")) {
            if (mImgBase64List.size() < 3) {
                ShowUtil.showToast(this, "添加3张照片");
                return;
            }
        } else if (isWho.equals("2")) {
            if (mImgBase64List.size() < 4) {
                ShowUtil.showToast(this, "至少添加4张照片");
                return;
            }
        }
        mProgressDialog.show();
        if ("1".equals(houxurenzheng)) {
            HashMap map = new HashMap();
            map.put("code", RequestPath.CODE);
            map.put("userid", SharedPreferencesUtil.getUid(this));
            map.put("usertype", isWho);
            map.put("truephone", telNumber);
            map.put("trueemail", mailbox);
            map.put("useridcard", IDnumber);
            //注册时没有认证    后续进行的认证
            switch (isWho) {
                case "1":
                    map.put("username", trueName);
                    break;
                case "2":
                    map.put("orgname", zuzhiName);
                    map.put("username", trueName);
                    break;
            }
            goRenZheng(map);
        } else {
            //注册流程时进行的认证
            Intent intent = new Intent();
            if (isWho.equals("1")) {
                intent.putExtra("trueName", trueName);
                intent.putExtra("IDnumber", IDnumber);
                intent.putExtra("telNumber", telNumber);
                intent.putExtra("mailbox", mailbox);
            } else if (isWho.equals("2")) {
                intent.putExtra("zuzhiName", zuzhiName);
                intent.putExtra("trueName", trueName);
                intent.putExtra("IDnumber", IDnumber);
                intent.putExtra("telNumber", telNumber);
                intent.putExtra("mailbox", mailbox);
            }
            setResult(RESULT_OK, intent);
            finish();
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
                .addSheetItem("相机拍照", ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                int code = ContextCompat.checkSelfPermission(RenZhengActivity.this,
                                        Manifest.permission.CAMERA);
                                if (code != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(RenZhengActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            100);
                                } else {

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    suijishu = System.currentTimeMillis();
                                    // 判断存储卡是否可以用，可用进行存储
//                                        if (hasSdcard()) {

                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera",

                                                    suijishu + PHOTO_FILE_NAME)));
//                                        }
                                    startActivityForResult(intent, 1);
                                }

                            }
                        }).addSheetItem("相册选择", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                suijishu = System.currentTimeMillis();

                LogUtils.e("Build.VERSION", String.valueOf(android.os.Build.VERSION.SDK_INT));
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    //系统版本大于4.4
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 3);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                }

            }
        }).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 同意
                    LogUtils.e("-----onRequestPermissionsResult  // 同意-----  ");

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    suijishu = System.currentTimeMillis();
                    // 判断存储卡是否可以用，可用进行存储
//                                        if (hasSdcard()) {

                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera",

                                    suijishu + PHOTO_FILE_NAME)));
//                                        }
                    startActivityForResult(intent, 1);
                } else {
                    //拒绝
                    LogUtils.e(" ----onRequestPermissionsResult // //拒绝-----  ");

                    AlertDialog.Builder builder = new AlertDialog.Builder(RenZhengActivity.this);
                    builder.setTitle("帮助");
                    builder.setMessage("当前应用缺少必要权限。" +
                            "请点击“设置”-“权限”-打开所需权限。" +
                            "最后点击两次后退按钮，即可返回。");

                    // 拒绝, 退出应用
                    builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//							setResult(10102);
//							finish();
                        }
                    });

                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    });

                    builder.show();

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    File tempFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera", suijishu + PHOTO_FILE_NAME);
                    LogUtils.e("123321", tempFile.getAbsolutePath());
                    try {
                        //FileInputStream is = news FileInputStream(tempFile);
                        Bitmap b = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                        bitmap = ZipBitmapUtil.reduce(b, width, height, true);
//                        bitmap =ZipBitmapUtil.rotate(bitmap,-90);
                        LogUtils.e("log", b.getByteCount() + "size");
                        LogUtils.e("log", bitmap.getByteCount() + "size");
                        mImgList.add(bitmap);
                        mImgAdapter.setmDatas(mImgList);
                        mImgAdapter.notifyDataSetChanged();
                        String imgURL = saveMyBitmap(getSDPath() + "/微值/" + suijishu + PHOTO_FILE_NAME, bitmap);
                        String imgBase64 = BitmapToBase64.bitmapToBase64(bitmap);
                        mImgBase64List.add(imgBase64);
                        LogUtils.e("log", imgURL + "---------");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    if (data == null) {
                        return;
                    }
                    Uri uri = data.getData();
                    try {
                        String[] pojo = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(uri, pojo, null, null, null);
                        if (cursor != null) {
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
                                bitmap = ZipBitmapUtil.reduce(bitmap, width, height, false);
//                                bitmap = ZipBitmapUtil.rotate(bitmap, 90);
                                mImgList.add(bitmap);
                                mImgAdapter.setmDatas(mImgList);
                                mImgAdapter.notifyDataSetChanged();
                                String imgURL = saveMyBitmap(getSDPath() + "/微值/" + suijishu + PHOTO_FILE_NAME, bitmap);
                                String imgBase64 = BitmapToBase64.bitmapToBase64(bitmap);
                                mImgBase64List.add(imgBase64);
                                LogUtils.e("log", imgURL + "---------");
                            }
                        }

                    } catch (Exception e) {

                    }
                    break;
                case 3:
                    //系统版本大于4.4
                    Uri suri = data.getData();
                    if (data == null) {
                        return;
                    }
                    try {
                        String path = PictureHelper.getPath(this, suri);
//                        String[] pojo = {MediaStore.Images.Media.DATA};
//                        Cursor cursor = managedQuery(uri, pojo, null, null, null);
//                        if (cursor != null) {
                        if (true) {
//                            int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                            cursor.moveToFirst();
                            /***
                             * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
                             * 如果是图片格式的话，那么才可以
                             */
                            if (path.endsWith("jpg") || path.endsWith("png") || path.endsWith("jpeg") ||
                                    path.endsWith("JPG") || path.endsWith("PNG") || path.endsWith("JPEG")) {
                                bitmap = BitmapFactory.decodeFile(path);
                                bitmap = ZipBitmapUtil.reduce(bitmap, width, height, false);
//                                bitmap = ZipBitmapUtil.rotate(bitmap, 90);
                                mImgList.add(bitmap);
                                mImgAdapter.setmDatas(mImgList);
                                mImgAdapter.notifyDataSetChanged();
                                String imgURL = saveMyBitmap(getSDPath() + "/微值/" + suijishu + PHOTO_FILE_NAME, bitmap);
                                String imgBase64 = BitmapToBase64.bitmapToBase64(bitmap);
                                mImgBase64List.add(imgBase64);
                                LogUtils.e("log", imgURL + "---------");
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
    public String saveMyBitmap(String bitName, Bitmap mBitmap) {
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
        return f.getAbsolutePath();
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

    /*认证的方法*/
    private void goRenZheng(HashMap map) {
        if (RenZhengActivity.mImgBase64List != null && RenZhengActivity.mImgBase64List.size() > 0) {
            for (int i = 0; i < RenZhengActivity.mImgBase64List.size(); i++) {
                map.put("usertrueimg" + (i + 1), RenZhengActivity.mImgBase64List.get(i));
            }
        }
        NetworkRequest.postRequest(RequestPath.POST_SETTRUEINFO, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
//                mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String result = jsonObject.getString("result");
                    String message = jsonObject.getString("message");
                    if (result.equals("1")) {
                        SharedPreferencesUtil.setUerAuthentic(RenZhengActivity.this, "2");

                        RenZhengActivity.mImgBase64List.clear();
                        finish();
                    }
                    ShowUtil.showToast(RenZhengActivity.this, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
//                mProgressDialog.dismiss();
                ShowUtil.showToast(RenZhengActivity.this, "网络不佳请稍后再试...");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RenZhengActivity.mImgBase64List.clear();
    }
}
