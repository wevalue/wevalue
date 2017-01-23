package com.wevalue.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wevalue.utils.LogUtils;

public class BaseFragment extends Fragment {

	/** Fragment当前状态是否可见 */
	protected boolean isVisible;
	String Tag = getClass().getSimpleName();

	public View init(LayoutInflater inflater, int layout) {
		View view = inflater.inflate(layout, null);
		view = inflater.inflate(layout, null, false);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		return view;
	}

	public void finish() {
		FragmentManager fManager = getFragmentManager();
		if (fManager != null) {
			fManager.popBackStack();
		}
	}

	public void startFragment(BaseFragment fragment) {

		try {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(android.R.id.content, fragment);
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			Toast.makeText(getActivity(), "exception-->" + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void startFragment(Fragment oldFragment, Fragment newFragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(oldFragment);
		ft.addToBackStack(null);// 设置结束后，需要返回本fragment刷新
		ft.commit();
		FragmentTransaction ft1 = getFragmentManager().beginTransaction();
		ft1.add(android.R.id.content, newFragment);
		ft1.commit();
	}

	public static void startFragment(FragmentTransaction ft, BaseFragment fragment) {
		ft.add(android.R.id.content, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.i(Tag, "------onCreate------");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtils.i(Tag, "------onCreateView------");
//		/** Android 沉浸式 */
//		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//			// 透明状态栏
//			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			// 透明导航栏
//			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//			SystemBarTintManager tintManager = news SystemBarTintManager(getActivity());
//			tintManager.setStatusBarTintEnabled(false);
//			// tintManager.setStatusBarTintResource(R.color.notifyBackground);// 通知栏所需颜色
//		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		LogUtils.i(Tag, "------onActivityCreated------");
	}

	@Override
	public void onStart() {
		LogUtils.i(Tag, "------onStart------");
		super.onStart();
	}

	@Override
	public void onPause() {
		LogUtils.i(Tag, "------onPause------");
		super.onPause();
	}

	@Override
	public void onResume() {
		LogUtils.i(Tag, "------onResume------");
		super.onResume();
	}

	@Override
	public void onStop() {
		LogUtils.i(Tag, "------onStop------");
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		LogUtils.i(Tag, "------onViewCreated------");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		LogUtils.i(Tag, "------onDestory------");
		super.onDestroy();
	}

	public void startActivity(Intent intent) {
		getActivity().startActivity(intent);
	}



}
