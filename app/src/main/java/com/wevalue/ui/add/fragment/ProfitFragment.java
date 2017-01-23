package com.wevalue.ui.add.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;

/**
 * Created by Administrator on 2016-06-27.
 */
public class ProfitFragment extends BaseFragment{


    private View view;
    private Context mContext;
    private MainActivity mainActivity;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

//            initView();

        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        view = LayoutInflater.from(mContext).inflate(R.layout.activity_launcher,null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
