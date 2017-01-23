//package com.wevalue.ui.world.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ScrollView;
//
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
//import com.wevalue.R;
//import com.wevalue.base.BaseFragment;
//import com.wevalue.ui.details.adapter.NoteDetailsZambiaAdapter;
//import com.wevalue.utils.ShowUtil;
//import com.wevalue.view.NoScrollListview;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * 帖子点赞列表的界面
// */
//public class ZambiaListFragment extends BaseFragment{
//
//    private View view;
//    private Context mContext;
//
//    private PullToRefreshScrollView prsv_ScrollView;
//    private NoScrollListview mNoScrollListview;
//    private NoteDetailsZambiaAdapter mAdapter;
//    private List<String> mList;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mContext = getActivity().getApplicationContext();
//        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_note_info_zambia,null);
//
//
//
//        initView();
//        return view;
//    }
//
//    /**初始化控件*/
//    private void initView() {
//
//        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
//        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
//
//        mList = news ArrayList<>();
//        mList.add("122");
//        mAdapter = news NoteDetailsZambiaAdapter(mContext,mList);
//
//        mAdapter.notifyDataSetChanged();
//        mNoScrollListview.setAdapter(mAdapter);
//
//
//        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
//        prsv_ScrollView.setOnRefreshListener(news PullToRefreshBase.OnRefreshListener2<ScrollView>() {
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
////                下拉
//
//                ShowUtil.showToast(getActivity(), "下拉==");
//                prsv_ScrollView.onRefreshComplete();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//
////                上拉
//                ShowUtil.showToast(getActivity(), "上拉-0-----");
//                prsv_ScrollView.onRefreshComplete();
//
//
//
//            }
//
//        });
//    }
//
//
//}
