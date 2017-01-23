package com.wevalue.ui.mine.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.Area;
import com.wevalue.model.City;
import com.wevalue.model.Province;
import com.wevalue.net.Interfacerequest.UserEditRequest;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.login.RenZhengActivity;
import com.wevalue.ui.login.RenZhengZhongActivity;
import com.wevalue.ui.login.TypeChoiceActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.utils.BitmapToBase64;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.utils.ZipBitmapUtil;
import com.wevalue.view.ActionSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 我的信息
 */
public class MyInfoActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_back;
    private ImageView iv_avatar;
    private ImageView iv_QR_code;
    private TextView tv_head_right;
    private TextView tv_head_title;
    private TextView tv_nickname;
    private TextView tv_sex, tv_tel, tv_email;
    ;
    private TextView tv_addr;
    private TextView tv_jianjie;
    private TextView tv_wevalue_number;
    private TextView tv_user_type;
    private TextView tv_dengji;

    private RelativeLayout rl_nickname;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_jianjie;
    private RelativeLayout rl_addr;
    private RelativeLayout rl_dengji;
    private RelativeLayout rl_channel;

    private LinearLayout ll_binding_tel;
    private LinearLayout ll_binding_email;

    private String userTel;//获取记录在本地的用户手机号
    private String userEmail;//获取记录在本地的用户邮箱

    /**
     * 修改资料
     */
    private static final int MODIFY_USER_INFO = 4;
    /**
     * 修改性别
     */
    private static final int MODIFY_USER_SEX = 5;


    // 图片下载器
    private BitmapUtils mBitmap;
    private BitmapDisplayConfig bitmapDisplayConfig;
    private RelativeLayout rl_zhanghu;
    private RelativeLayout rl_QR;
    private String sex;
    private String sex_2;


    private DbUtils dbUtils;
    private ArrayList<Province> provinceList;
    private List<City> cityList;
    private ArrayList<ArrayList<String>> cityItems = new ArrayList<>();
    private List<Area> areaList;
    private OptionsPickerView pvOptions;
    private UserEditRequest mUserEditRequest;
    private RelativeLayout rl_myAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        userTel = SharedPreferencesUtil.getMobile(this);
        userEmail = SharedPreferencesUtil.getEmail(this);
        dbUtils = DbUtils.create(this, "WeValue.db");


        mBitmap = new BitmapUtils(this);
        bitmapDisplayConfig = new BitmapDisplayConfig();
        bitmapDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
        // 图片太大时容易OOM。
        bitmapDisplayConfig.setBitmapConfig(Config.RGB_565);
        bitmapDisplayConfig.setLoadingDrawable(getResources().getDrawable(R.mipmap.default_head));
        bitmapDisplayConfig.setLoadFailedDrawable(getResources().getDrawable(R.mipmap.default_head));
        mBitmap.configDiskCacheEnabled(true);

        mUserEditRequest = UserEditRequest.initUserEditRequest(this);
        initView();
        initAddressPicker();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText("认证");
        tv_head_right.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        iv_QR_code = (ImageView) findViewById(R.id.iv_QR_code);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_head_title.setText("我的信息");
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_wevalue_number = (TextView) findViewById(R.id.tv_wevalue_number);
        tv_user_type = (TextView) findViewById(R.id.tv_user_type);
        tv_dengji = (TextView) findViewById(R.id.tv_dengji);

        rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_jianjie = (RelativeLayout) findViewById(R.id.rl_jianjie);
        rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
        rl_zhanghu = (RelativeLayout) findViewById(R.id.rl_zhanghu);
        rl_QR = (RelativeLayout) findViewById(R.id.rl_QR);
        rl_dengji = (RelativeLayout) findViewById(R.id.rl_dengji);
        rl_channel = (RelativeLayout) findViewById(R.id.rl_channel);

        ll_binding_tel = (LinearLayout) findViewById(R.id.ll_binding_tel);
        ll_binding_email = (LinearLayout) findViewById(R.id.ll_binding_email);


        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_dengji.setText(SharedPreferencesUtil.getUserleve(this));
        sex = SharedPreferencesUtil.getSex(this);
        tv_sex.setText(sex);

        if (SharedPreferencesUtil.getUsertype(this).equals("1")) {
            tv_user_type.setText("个人");
        } else {
            tv_user_type.setText("组织");
        }
        // 图片的下载
        mBitmap.display(iv_avatar, RequestPath.SERVER_PATH + SharedPreferencesUtil.getAvatar(this), bitmapDisplayConfig);
        mBitmap.display(iv_QR_code, RequestPath.SERVER_PATH + SharedPreferencesUtil.getQR_code(this), bitmapDisplayConfig);

        iv_avatar.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_jianjie.setOnClickListener(this);
        rl_addr.setOnClickListener(this);
        rl_QR.setOnClickListener(this);
        rl_zhanghu.setOnClickListener(this);
        rl_dengji.setOnClickListener(this);
        rl_channel.setOnClickListener(this);

        ll_binding_tel.setOnClickListener(this);
        ll_binding_email.setOnClickListener(this);
        rl_myAvatar = (RelativeLayout) findViewById(R.id.rl_myAvatar);
        rl_myAvatar.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_jianjie.setText(SharedPreferencesUtil.getUserInfo(this));
        tv_addr.setText(SharedPreferencesUtil.getProvinceName(this) + " " + SharedPreferencesUtil.getCityName(this));
        tv_wevalue_number.setText(SharedPreferencesUtil.getUsernumber(this));

        String tel = SharedPreferencesUtil.getMobile(this);
        String email = SharedPreferencesUtil.getEmail(this);
        if (TextUtils.isEmpty(tel)) {
            tv_tel.setText("未绑定手机号");
        } else {
            tv_tel.setText(tel.substring(0, 3) + "****" + tel.substring(7, tel.length()));
        }

        LogUtils.e("tel = " + tel + "-----email =" + email);

        if (TextUtils.isEmpty(email)) {
            tv_email.setText("未绑定邮箱");
        } else {
            tv_email.setText(email);
        }

    }

    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.tv_head_right:
                String renzhengState = SharedPreferencesUtil.getUerAuthentic(this);
                if (!TextUtils.isEmpty(renzhengState)) {
                    switch (renzhengState) {
                        case "0":
                            it = new Intent(MyInfoActivity.this, RenZhengActivity.class);
                            it.putExtra("houxurenzheng", "1");
                            it.putExtra("isWho", SharedPreferencesUtil.getUsertype(MyInfoActivity.this));
                            break;
                        case "1":
                            it = new Intent(MyInfoActivity.this, RenZhengZhongActivity.class);
                            it.putExtra("isWho", SharedPreferencesUtil.getUsertype(MyInfoActivity.this));
                            it.putExtra("renzhengState", "1");
                            break;
                        case "2":
                            it = new Intent(MyInfoActivity.this, RenZhengZhongActivity.class);
                            it.putExtra("isWho", SharedPreferencesUtil.getUsertype(MyInfoActivity.this));
                            it.putExtra("renzhengState", "2");
                            break;
                        case "3":
                            it = new Intent(MyInfoActivity.this, RenZhengZhongActivity.class);
                            it.putExtra("isWho", SharedPreferencesUtil.getUsertype(MyInfoActivity.this));
                            it.putExtra("renzhengState", "3");
                            break;
                    }
                    startActivity(it);
                }
                break;
            case R.id.rl_myAvatar://头像栏的线性布局
                Head_portrait();
                break;
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.iv_avatar://头像
                String[] url = {SharedPreferencesUtil.getAvatar(this)};
                Intent intent = new Intent(MyInfoActivity.this, ImgShowActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("imgUrl", url);
                startActivity(intent);
                break;
            case R.id.rl_sex://性别

                choiceSex();
                break;
            case R.id.rl_QR://二维码
                it = new Intent(this, MyQRCodeActivity.class);
                startActivity(it);
                break;
            case R.id.rl_nickname://昵称
                it = new Intent(this, ModifyDataActivity.class);
                it.putExtra("isWho", "1");
                it.putExtra("nickname", tv_nickname.getText().toString());
                startActivityForResult(it, MODIFY_USER_INFO);
                break;
            case R.id.rl_addr://地址
                pvOptions.show();
                break;
            case R.id.rl_dengji://等级
                it = new Intent(this, GradeActivity.class);
                startActivity(it);
                break;
            case R.id.rl_channel://我的频道

                it = new Intent(this, TypeChoiceActivity.class);
                startActivity(it);
                break;
            case R.id.rl_jianjie://简介
                it = new Intent(this, ModifyDataActivity.class);
                it.putExtra("isWho", "2");
                it.putExtra("nickname", tv_jianjie.getText().toString());
                startActivityForResult(it, MODIFY_USER_SEX);
                break;
            case R.id.ll_binding_tel://绑定手机号
                it = new Intent(this, BindingTelEmailActivity.class);
                it.putExtra("who", "tel");
                startActivity(it);
                break;
            case R.id.ll_binding_email://绑定邮箱
                it = new Intent(this, BindingTelEmailActivity.class);
                it.putExtra("who", "email");
                startActivity(it);
                break;
        }
    }


    /**
     * 初始化 车轮控件
     */
    private void initAddressPicker() {
        try {
            cityList = new ArrayList<>();
            cityList = dbUtils.findAll(Selector.from(City.class));
            areaList = new ArrayList<>();
            areaList = dbUtils.findAll(Selector.from(Area.class));
            provinceList = new ArrayList<Province>();
            provinceList = (ArrayList) dbUtils.findAll(Selector.from(Province.class));

            if (null != provinceList && provinceList.size() > 0) {
                for (int i = 0; i < provinceList.size(); i++) {
                    // 设置市级
                    ArrayList<String> city_2 = new ArrayList<String>();
                    String id = provinceList.get(i).getProvinceid();
                    String name = provinceList.get(i).getProvincename();

                    for (int j = 0; j < cityList.size(); j++) {
                        String pid = cityList.get(j).getpId();

                        if (id.equals(pid)) {
//                            if (name.equals(cityList.get(j).getCityname())) {
//                                for (int k = 0; k < areaList.size(); k++) {
//                                    if (id.equals(areaList.get(k).getpId())) {
//                                        city_2.add(areaList.get(k).getDistrictname());
//                                    }
//                                }
//
//                            } else {
                            city_2.add(cityList.get(j).getCityname());
//                            }

                        }
                    }

                    cityItems.add(city_2);
                }
            }


        } catch (DbException e) {
            e.printStackTrace();
        }

        //选项选择器
        pvOptions = new OptionsPickerView(this);

        //二级联动效果
        pvOptions.setPicker(provinceList, cityItems, true);
        //设置选择的二级单位
        pvOptions.setLabels("", "");
//		pvOptions.setTitle("选择店铺地址");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的二级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                //返回的分别是二个级别的选中位置
                String provinceName = provinceList.get(options1).getProvincename();
                String city;
                city = cityItems.get(options1).get(options2);
                if (provinceName.equals(city)) {
                    tv_addr.setText(provinceName);
                } else {
                    tv_addr.setText(provinceName + " " + city);
                }
                mUserEditRequest.setCityData(provinceName, city, tv_addr);

            }
        });


    }


    /**
     * 选择性别
     */
    private void choiceSex() {

        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("男", ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                sex_2 = "男";
                                mUserEditRequest.returnData(sex, sex_2, MyInfoActivity.this, tv_sex);

                            }
                        }).addSheetItem("女", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                sex_2 = "女";
                mUserEditRequest.returnData(sex, sex_2, MyInfoActivity.this, tv_sex);

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
    /**
     * 结果
     */
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private Uri uri;
    private File tempFile;
    private Bitmap bitmap;

    /**
     * Title:  Head_portrait<br>
     * Description: 显示选择头像dialog Action<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    public void Head_portrait() {
        new ActionSheetDialog(MyInfoActivity.this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("相机拍照", ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                int code = ContextCompat.checkSelfPermission(MyInfoActivity.this,
                                        Manifest.permission.CAMERA);
                                if (code!= PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(MyInfoActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            100);
                                } else{
                                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                    // 判断存储卡是否可以用，可用进行存储
                                    if (hasSdcard()) {
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(),

                                                        PHOTO_FILE_NAME)));
                                    }
                                    startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
                                }



                            }
                        }).addSheetItem("相册选择", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

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
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    // 判断存储卡是否可以用，可用进行存储
                    if (hasSdcard()) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(),

                                        PHOTO_FILE_NAME)));
                    }
                    startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

                } else {
                    //拒绝
                    LogUtils.e(" ----onRequestPermissionsResult // //拒绝-----  ");

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoActivity.this);
                    builder.setTitle("帮助");
                    builder.setMessage("当前应用缺少必要权限。" +
                            "请点击“设置”-“权限”-打开所需权限。" +
                            "最后点击两次后退按钮，即可返回。");

                    // 拒绝, 退出应用
                    builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
//							setResult(10102);
//							finish();
                        }
                    });

                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
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


    /**
     * Title: hasSdcard<br>
     * Description: TODO 判断SD卡是否可用<br>
     *
     * @since JDK 1.7
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author fengyaru TODO 简单描述该方法的实现功能
     * @see FragmentActivity#onActivityResult(int, int,
     * Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                ShowUtil.showToast(MyInfoActivity.this, "未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {

                bitmap = data.getParcelableExtra("data");
                final Bitmap newbiBitmap = ZipBitmapUtil.toRoundCorner(bitmap, 0);
                final String baseStr = BitmapToBase64.bitmapToBase64(newbiBitmap);

                mUserEditRequest.editAvatar(baseStr, newbiBitmap, iv_avatar);

                File photoFile = new File(getSDPath() + "/微值/" + PHOTO_FILE_NAME);
                if (photoFile.exists()) {
                    File newFile = new File(getCacheDir(), photoFile.getName());
                }
                saveMyBitmap(getSDPath() + "/微值/" + PHOTO_FILE_NAME, newbiBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == MODIFY_USER_INFO) {
            if (data != null) {
                String nickname = data.getStringExtra("nickname");
                tv_nickname.setText(nickname);
                LogUtils.e("log", "nickname = " + nickname);
                SharedPreferencesUtil.setNickname(this, nickname);
            }

        } else if (requestCode == MODIFY_USER_SEX) {
            if (data != null) {
                String jianjie = data.getStringExtra("nickname");
                tv_jianjie.setText(jianjie);
                LogUtils.e("log", "jianjie = " + jianjie);
                SharedPreferencesUtil.setUserInfo(this, jianjie);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Title: crop<br>
     * Description: TODO 剪切图片<br>
     *
     * @param uri
     * @since JDK 1.7
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
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

    /**
     * Title: saveMyBitmap<br>
     * Description: TODO 保存图片<br>
     * Depend : TODO <br>
     *
     * @param bitName
     * @param mBitmap
     * @author lijie
     * @Modified by
     * @CreateDate 2015年10月10日 上午10:18:14
     * @link lijie@iyangpin.com
     * @Version
     * @since JDK 1.7
     */
    public static void saveMyBitmap(String bitName, Bitmap mBitmap) {
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
}
