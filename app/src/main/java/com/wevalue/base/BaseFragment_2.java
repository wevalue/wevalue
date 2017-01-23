package com.wevalue.base;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wevalue.R;

public abstract  class BaseFragment_2 extends Fragment {

//	public static int DelayTime = 500;
//	private View rootView, contentView;
//	private LayoutInflater mInflater = LayoutInflater.from(App.getInstance());
//	protected Handler mHandler = news Handler(Looper.getMainLooper());
//	protected ProgressDialog dialog;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstanceState) {
//		if (null == rootView) {
//			rootView = inflater.inflate(R.layout.layout_base_fragment,
//					container, false);
//			if (null != contentView) {
//				((ViewGroup) rootView).addView(contentView);
//			}
//		}
//		return rootView;
//	}
//
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//			if (null == contentView) {
//				dialog = ProgressDialog.show(getActivity(), null, null);
//				contentView = mInflater.inflate(getContentID(), null);
//				initViews(contentView);
//				if (null != rootView) {
//					((ViewGroup) rootView).addView(contentView);
//				}
//			}
//			loadDatas();
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		((ViewGroup) rootView).removeView(rootView);
//	}

	public abstract int getContentID();

	public abstract void initViews(View contentView);

	public abstract void loadDatas();



}
