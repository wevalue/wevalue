package com.wevalue.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 判断网络状态
 */
public class NetStatusUtil {

	public static boolean getWifiStatus(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifiState = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (wifiState.equals(State.CONNECTED)) {
			return true;
		}
		return false;
	}

	public static boolean get3GStatus(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobileState = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (mobileState.equals(State.CONNECTED)) {
			return true;
		}
		return false;
	}

	public static boolean getStatus(Context context) {

		if (getWifiStatus(context) || get3GStatus(context)) {
			return true;
		}
		return false;
	}
}
