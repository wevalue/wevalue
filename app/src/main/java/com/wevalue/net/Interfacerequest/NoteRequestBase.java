package com.wevalue.net.Interfacerequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 帖子相关接口请求类
 */
public class NoteRequestBase {
    private Context mContext;
    public static NoteRequestBase noteRequestBase;

    public NoteRequestBase(Context context) {
        mContext = context;
    }

    public static NoteRequestBase getNoteRequestBase(Context context) {
        if (noteRequestBase != null) {
            return noteRequestBase;
        } else {
            noteRequestBase = new NoteRequestBase(context);
            return noteRequestBase;
        }
    }

    /**
     * 获取我发布的帖子接口
     */
    public void getNoteListData(String pageindex, String notezone, String status, WZHttpListener wZHttpListener) {
        String url = RequestPath.GET_GETNOTES;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("pagenum", "10");
        map.put("pageindex", pageindex);
        map.put("status", status);
        map.put("notezone", notezone);
        NetworkRequest.getRequest(url, map, wZHttpListener);
    }

    /**
     * 世界中获取所有帖子列表接口
     */
    public void getNoteListData(String getDataTime,String pageindex, String noteclass, String keywords, String deviceid, WZHttpListener wZHttpListener) {
        String url = RequestPath.GET_GETNOTE;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("pagenum", "10");
        map.put("deviceid", deviceid);
        map.put("pageindex", pageindex);
        map.put("noteclass", noteclass);
        map.put("requesttime", getDataTime);
        if (!TextUtils.isEmpty(keywords)) {
            map.put("keywords", keywords);
        }
        NetworkRequest.getRequest(url, map, wZHttpListener);
    }

    public void getNoteListDataForCity(String getDataTime,String pageindex, String noteclass, String cityname, String deviceid, WZHttpListener wZHttpListener) {
        String url = RequestPath.GET_GETNOTE;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("pagenum", "10");
        map.put("deviceid", deviceid);
        map.put("pageindex", pageindex);
        map.put("noteclass", noteclass);
        map.put("requesttime", getDataTime);
        if (!TextUtils.isEmpty(cityname)) {
            map.put("cityname", cityname);
        }
        NetworkRequest.getRequest(url, map, wZHttpListener);
    }

    /**
     * 世界中获取推荐中的所有帖子列表接口
     */
    public void getNoteListData(String getDataTime,String pageindex, String lunboindex, String noteclass, String keywords, String deviceid, WZHttpListener wZHttpListener) {
        String url = RequestPath.GET_GETNOTE;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("pagenum", "10");
        map.put("deviceid", deviceid);
        map.put("pageindex", pageindex);
        map.put("noteclass", noteclass);
        map.put("lunboindex", lunboindex);
        map.put("requesttime", getDataTime);
        if (!TextUtils.isEmpty(keywords)) {
            map.put("keywords", keywords);
        }
        NetworkRequest.getRequest(url, map, wZHttpListener);
    }

    /**
     * 帖子详情接口
     */
    public void getNoteInfo(String noteid, String repostid, WZHttpListener wZHttpListener) {
        String url = RequestPath.GET_NOTEINFO;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("noteid", noteid);
        map.put("repostid", repostid);
        NetworkRequest.getRequest(url, map, wZHttpListener);
    }

    /**
     * 帖子详情的 点赞,转发等 列表接口请求方法
     */
    public void getNoteInfoRepostlist(String url, String noteid, String repostid, int index, WZHttpListener wZHttpListener) {

        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteid);
        map.put("pagenum", "10");
        map.put("pageindex", String.valueOf(index));
        map.put("repostid", repostid);
        NetworkRequest.getRequest(url, map, wZHttpListener);
    }

    /**
     * 点赞，取消点赞
     */
    public void postNoteLike(Activity activity, String noteid, String repostid, WZHttpListener wZHttpListener) {
        if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(activity))) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            return;
        }

        String url = RequestPath.POST_EDITNOTEZAN;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteid);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("repostid", repostid);
        LogUtils.e("userid" + SharedPreferencesUtil.getUid(mContext), "noteid" + noteid);
        NetworkRequest.postRequest(url, map, wZHttpListener);
    }

    /**
     * 添加情绪接口
     */
    public void postNoteEmotion(String repostid, String noteid, String content, WZHttpListener wzHttpListener) {
        String url = RequestPath.POST_ADD_MOOD;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteid);
        map.put("content", content);
        map.put("repostid", repostid);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        NetworkRequest.postRequest(url, map, wzHttpListener);

    }


    /**
     * 转发接口
     */
    public void postZhuangFa(String uprepostid, String isself, String noteid, String repostcontent, String isfree, WZHttpListener wzHttpListener) {
        String url = RequestPath.POST_ZHUANFA;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteid);
        map.put("uprepostid", uprepostid);
        map.put("repostcontent", repostcontent);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("isfree", isfree);
        map.put("isself", isself);
        NetworkRequest.postRequest(url, map, wzHttpListener);
    }

    /**
     * 删除信息接口
     */
    public void postDelete(String noteid, WZHttpListener wzHttpListener) {
        String url = RequestPath.POST_DELNOTE;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("noteid", noteid);
        NetworkRequest.postRequest(url, map, wzHttpListener);

    }
    /*长按添加不喜欢接口
    */



    /**
     * 帖子举报接口
     */
    public void postJubao(String noteid,String repostid, WZHttpListener wzHttpListener) {
        String url = RequestPath.POST_ADDADVICE;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("noteid", noteid);
        map.put("repostid", repostid);
        NetworkRequest.postRequest(url, map, wzHttpListener);
    }
}
