/** 
 * Project Name:I500Social <br>
 * File Name:DensityUtil.java <br>
 * Package Name:com.i500m.i500social.utils<br> 
 * Date:2015年8月31日 下午5:12:17 <br>
 * Company: 灵韬致胜（北京）科技发展有限公司<br>
 * Copyright @ 2015 灵韬致胜（北京）科技发展有限公司<br>
 */    
package com.wevalue.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Title:  DensityUtil<br>
 * Description:<br>
 * @since JDK 1.7 
 */
public class DensityUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
			context.getResources().getDisplayMetrics());
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int dpToPx(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
			context.getResources().getDisplayMetrics());
	}
	
	public static int spToPx(Context context, int sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
			context.getResources().getDisplayMetrics());
	}
	
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
			res.getDisplayMetrics());
	}
	
	public static int spToPx(Resources res, int sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
			res.getDisplayMetrics());
	}
}
