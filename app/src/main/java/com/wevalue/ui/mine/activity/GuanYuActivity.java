package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GuanYuActivity extends BaseActivity {
	
	private ImageView iv_back;
	private ImageView iv_goods_logo;
	private TextView tv_head_title,tv_addr, tv_jianjie;
	private Button but_lianxi;
	

	// 图片下载器
	private BitmapUtils mBitmap;
	private BitmapDisplayConfig bitmapDisplayConfig;
	private String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guanyu);


		mBitmap = new BitmapUtils(this);
		bitmapDisplayConfig =new BitmapDisplayConfig();
		bitmapDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
		// 图片太大时容易OOM。
		bitmapDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		bitmapDisplayConfig.setLoadingDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
		bitmapDisplayConfig.setLoadFailedDrawable(getResources().getDrawable(R.mipmap.default_head));
		mBitmap.configDiskCacheEnabled(true);

		initView();
		getGoodsInfo();
	}


	/**初始化控件*/
	private void initView() {
		iv_back =(ImageView) findViewById(R.id.iv_back);
		iv_goods_logo =(ImageView) findViewById(R.id.iv_goods_logo);
		tv_head_title = (TextView) findViewById(R.id.tv_head_title);
		tv_head_title.setText("关于我们");
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
		but_lianxi = (Button) findViewById(R.id.but_lianxi);
		
		
		but_lianxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" +tel));
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				}
			}
		});
		
		
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		

	}
	
	/**获取公司信息*/
	private void getGoodsInfo() {

		Map<String ,String> map = new HashMap<>();
		map.put("code", RequestPath.CODE);

		NetworkRequest.getRequest(RequestPath.POST_ABOUTUS, map, new WZHttpListener() {
			@Override
			public void onSuccess(String content, String isUrl) {
				try {
					JSONObject obj = new JSONObject(content);
					if(obj.getString("result").equals("1")){
						JSONObject data = obj.getJSONObject("data");
						mBitmap.display(iv_goods_logo, RequestPath.SERVER_PATH+data.getString("aboutuslogo"),bitmapDisplayConfig);
						tv_addr.setText(data.getString("aboutusaddress"));
						tv_jianjie.setText(data.getString("aboutusinfo"));
						tel = data.getString("aboutusphone");
					}else {

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
			@Override
			public void onFailure(String content) {
				LogUtils.e("--onFailure---content = "+content);
				ShowUtil.showToast(GuanYuActivity.this, content);
			}
		});

	}
}
