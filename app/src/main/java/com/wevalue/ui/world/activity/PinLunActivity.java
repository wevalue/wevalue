package com.wevalue.ui.world.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuhua on 2016/8/15.
 */
public class PinLunActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {

    private ImageView img_touxiang;
    private ImageView img_tu;
    private TextView tv_adddimg;
    private TextView tv_send_note;
    private TextView tv_back;
    private TextView tv_nickname;
    private TextView tv_head_title;
    private NoteRequestBase mNoteRequestBase;
    private String noteId;
    private String commstate = "0";
    private EditText ed_pinglun;
    private File imgfile = null;


    private static final String PHOTO_FILE_NAME = "logo.jpg";
    /**
     * 拍照
     */
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    /**
     * 从相册中选择
     */
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private Bitmap bitmap;
    private int width;
    private int height;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinluninfo);

        WindowManager wm = this.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        if (getIntent() != null) {
            noteId = getIntent().getStringExtra("noteId");
        } else {
            noteId = "1";
        }
        initView();


    }

    private void initView() {
        img_touxiang = (ImageView) findViewById(R.id.img_touxiang);
        img_tu = (ImageView) findViewById(R.id.img_tu);
        tv_adddimg = (TextView) findViewById(R.id.tv_adddimg);
        ed_pinglun = (EditText) findViewById(R.id.ed_pinglun);

        tv_send_note = (TextView) findViewById(R.id.tv_send_note);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
/**标题控件*/
        tv_send_note.setText("发布");
        tv_head_title.setText("评论信息");
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_back.setText("取消");

        img_touxiang.setOnClickListener(this);
        tv_send_note.setOnClickListener(this);
        img_tu.setOnClickListener(this);
        tv_adddimg.setOnClickListener(this);
        tv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_touxiang:
                break;
            case R.id.img_tu:
                img_touxiang.setImageResource(R.mipmap.add_picture);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_adddimg:
                addImg();
                break;
            case R.id.tv_send_note:
                postNoteComment();
                break;
        }
    }

    /**
     * 回复评论接口
     */
    public void postNoteComment() {
        String url = RequestPath.POST_ADDCOMMENT;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("noteid", noteId);
        map.put("commcontent", ed_pinglun.getText().toString().trim());
        map.put("commstate", commstate);
        map.put("replyuserid", "");
        map.put("replycommid", "");

        if (imgfile != null) {
            map.put("noteimg", imgfile);
        }
        NetworkRequest.postRequest(url, map, this);

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

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

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
                        img_touxiang.setImageBitmap(bitmap);

                        imgfile = saveMyBitmap(getSDPath() + "/微值/" + PHOTO_FILE_NAME, bitmap);

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
                                img_touxiang.setImageBitmap(bitmap);
                                imgfile = saveMyBitmap(getSDPath() + "/微值/" + PHOTO_FILE_NAME, bitmap);
                            }
                        }

                    } catch (Exception e) {

                    }
                    break;
            }
        }
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

    @Override
    public void onSuccess(String content, String isUrl) {

        try {
            JSONObject json = new JSONObject(content);
            if (json.getString("result").equals("1")) {
                ShowUtil.showToast(this, json.getString("message"));
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(String content) {

    }
}
