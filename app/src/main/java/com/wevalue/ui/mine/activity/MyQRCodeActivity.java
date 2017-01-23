package com.wevalue.ui.mine.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.utils.SharedPreferencesUtil;


/**我的二维码界面**/
public class MyQRCodeActivity extends BaseActivity implements OnClickListener {

	private TextView tv_head_title;
	private TextView tv_nickname;
	private TextView tv_dengji;
	private TextView tv_sex;
	private TextView tv_user_addr;

	
	private ImageView iv_back;
	private ImageView iv_user_photo;
	private ImageView iv_QR_code;




	// 图片下载器
	private BitmapUtils mBitmap;
	private BitmapDisplayConfig bitmapDisplayConfig;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_qr_code);


		mBitmap = new BitmapUtils(this);
		bitmapDisplayConfig =new BitmapDisplayConfig();
		bitmapDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
		// 图片太大时容易OOM。
		bitmapDisplayConfig.setBitmapConfig(Bitmap.Config.ARGB_8888);
		bitmapDisplayConfig.setLoadingDrawable(getResources().getDrawable(R.mipmap.default_head));
		bitmapDisplayConfig.setLoadFailedDrawable(getResources().getDrawable(R.mipmap.default_head));
		mBitmap.configDiskCacheEnabled(true);
		initView();
	
	}


	private void initView() {


		tv_head_title = (TextView) findViewById(R.id.tv_head_title);
		tv_user_addr = (TextView) findViewById(R.id.tv_user_addr);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_dengji = (TextView) findViewById(R.id.tv_dengji);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_head_title.setText("我的二维码");
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_user_photo = (ImageView) findViewById(R.id.iv_user_photo);
		iv_QR_code = (ImageView) findViewById(R.id.iv_QR_code);

		// 图片的下载
		mBitmap.display(iv_user_photo, RequestPath.SERVER_PATH + SharedPreferencesUtil.getAvatar(this),bitmapDisplayConfig);
		mBitmap.display(iv_QR_code, RequestPath.SERVER_PATH + SharedPreferencesUtil.getQR_code(this),bitmapDisplayConfig);


		tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
		tv_user_addr.setText(SharedPreferencesUtil.getProvinceName(this)+" "+ SharedPreferencesUtil.getCityName(this));
		tv_dengji.setText(SharedPreferencesUtil.getUserleve(this));
		tv_sex.setText(SharedPreferencesUtil.getSex(this));

		iv_back.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

	
}
