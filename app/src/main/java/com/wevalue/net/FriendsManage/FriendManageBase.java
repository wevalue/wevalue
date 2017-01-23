package com.wevalue.net.FriendsManage;

import android.content.Context;

import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.HashMap;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/11
 * 类说明：好友操作的基类方法
 */

public class FriendManageBase {

    /*添加好友的方法*/
    public static void AddFriend(Context context, String frienduserid, WZHttpListener wzHttpListener) {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(context));
        map.put("frienduserid", frienduserid);
        NetworkRequest.postRequest(RequestPath.POST_ADDFRIEND, map, wzHttpListener);
    }

    /*删除好友的方法*/
    public static void DelFriend(Context context, String frienduserid, WZHttpListener wzHttpListener) {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(context));
        map.put("frienduserid", frienduserid);
        NetworkRequest.postRequest(RequestPath.POST_DELFRIEND, map, wzHttpListener);
    }

    /*添加关注的方法*/
    public static void AddFocus(Context context, String fansid, WZHttpListener wzHttpListener) {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(context));
        map.put("focususerid", fansid);
        NetworkRequest.postRequest(RequestPath.POST_ADDFOCUES, map, wzHttpListener);
    }

    /*取消关注的方法*/
    public static void DelFocus(Context context, String fansid, WZHttpListener wzHttpListener) {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(context));
        map.put("focususerid", fansid);
        NetworkRequest.postRequest(RequestPath.POST_DELFOCUES, map, wzHttpListener);
    }
}
