package com.wevalue.ui.world.fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.NoteBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.world.activity.ShiftCityActivity;
import com.wevalue.ui.world.adapter.WorldListAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;
import java.util.List;
/**
 * Created by K on 2016/9/21.
 */
public class MyType_BeijingFragment extends BaseFragment implements WZHttpListener {
    private View view;
    private Context mContext;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    private WorldListAdapter mHAdapter;
    private List<NoteBean.NoteEntity> mHListData;
    private List<NoteBean.NoteEntity> mListData_jiage;
    private int viewIndex = -1;
    private WorldFragment mWorldFragment;
    private NoteRequestBase mNoteRequestBase;
    private int pageindex = 1;
    TextView shiftCity;
    public MyType_BeijingFragment() {
    }
    @SuppressLint("ValidFragment")
    public MyType_BeijingFragment(int index, WorldFragment worldFragment) {
        viewIndex = index;
        mWorldFragment = worldFragment;
    }
    MainActivity mainActivity;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            mNoteRequestBase.getNoteListData(String.valueOf(pageindex), "1", "", SharedPreferencesUtil.getDeviceid(getActivity()), this);
        } else {
            //相当于Fragment的onPause
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mainActivity = (MainActivity) this.getActivity();
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(getActivity());
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_my_type_beijing, null);
        shiftCity = (TextView) view.findViewById(R.id.textshiftcity);
        shiftCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShiftCityActivity.class);
                startActivity(intent);
                finish();
            }
        });
        LogUtils.e("viewIndex =onCreateView= " + viewIndex);
        //根据父窗体getActivity()为fragment设置手势识别
        initView();
        LogUtils.e("轮播  onCreateView");
        return view;
    }
    /**
     * 初始化控件
     */
    private void initView() {
        LogUtils.e("轮播  -  initView");
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setFocusable(false);
        mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
                intent.putExtra("noteId", mHListData.get(position).getNoteid());
                startActivity(intent);
            }
        });
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                下拉
                pageindex = 1;
//                mNoteRequestBase.getNoteListData(String.valueOf(pageindex), "1", "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_BeijingFragment.this);
//                myViewPagerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtils.e("log", "onPullUp");
//                上拉
                pageindex++;
//                mNoteRequestBase.getNoteListData(String.valueOf(pageindex), "1", "",SharedPreferencesUtil.getDeviceid(getActivity()) ,MyType_BeijingFragment.this);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && requestCode == 101) {
            }
        }
    }
    @Override
    public void onSuccess(String content, String isUrl) {
        Gson gson = new Gson();
        NoteBean noteBean = gson.fromJson(content, NoteBean.class);
        prsv_ScrollView.onRefreshComplete();
        if (noteBean.getResult().equals("1")) {
            mListData_jiage = noteBean.data_jiage;
            if (pageindex > 1) {
                if (noteBean.getData().size() > 0) {
                    mHListData.addAll(noteBean.data);
                    if (mListData_jiage != null && mListData_jiage.size() > 2) {
                        mHListData.add(0, mListData_jiage.get(0));
                        mHListData.add(1, mListData_jiage.get(1));
                        mHListData.add(2, mListData_jiage.get(2));
                    }
                    mHAdapter.setmDatas(mHListData);
                    mHAdapter.notifyDataSetChanged();
                } else {
                    ShowUtil.showToast(getActivity(), "没有更多数据了");
                }
            } else {
                if (!MainActivity.isEditChannel && mHAdapter != null && mHListData != null && mHListData.size() > 0) {
                    mHListData = noteBean.getData();
                    if (mListData_jiage != null && mListData_jiage.size() > 2) {
                        mHListData.add(0, mListData_jiage.get(0));
                        mHListData.add(1, mListData_jiage.get(1));
                        mHListData.add(2, mListData_jiage.get(2));
                    }
                    mHAdapter.setmDatas(mHListData);
                    LogUtils.e("mHlistDatas.size = " + mHListData.size());
                    LogUtils.e("noteBean.getData()= " + noteBean.getData().size());
                    mHAdapter.notifyDataSetChanged();
                } else {
                    mHListData = noteBean.getData();
                    if (mListData_jiage != null && mListData_jiage.size() > 2) {
                        mHListData.add(0, mListData_jiage.get(0));
                        mHListData.add(1, mListData_jiage.get(1));
                        mHListData.add(2, mListData_jiage.get(2));
                    }
//                    mHAdapter = news WorldListAdapter(mHListData, mainActivity);
                    mHAdapter.notifyDataSetChanged();
                    mNoScrollListview.setAdapter(mHAdapter);
                }
            }
        } else {
            LogUtils.e("轮播  -  onSuccess   else {");
        }
    }
    @Override
    public void onFailure(String content) {
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        isPause = true;
        LogUtils.e("轮播  -  onDestroy");
    }
}