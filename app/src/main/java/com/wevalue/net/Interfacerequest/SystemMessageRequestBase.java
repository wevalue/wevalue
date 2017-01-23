package com.wevalue.net.Interfacerequest;

import android.content.Context;

import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统消息接口请求方法
 */
public class SystemMessageRequestBase {

    private Context mContext;
    public static SystemMessageRequestBase systemMessageBase;
    public SystemMessageRequestBase(Context context){
        mContext = context;
    }

    public static SystemMessageRequestBase initSystemMessageBase(Context context){

        if(systemMessageBase!=null){
            return systemMessageBase;
        }else {
            systemMessageBase= new SystemMessageRequestBase(context);
            return systemMessageBase;
        }

    }


    /**获取站内消息列表**/
    public void getUserstemess(String messtype,String pageindex,WZHttpListener wZHttpListener){

        String url = RequestPath.GET_USERSITEMESS;
        Map<String,String> map = new HashMap<>();
        map.put("code",RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("pagenum","10");
        map.put("pageindex",pageindex);
        map.put("messtype",messtype);

        NetworkRequest.getRequest(url,map,wZHttpListener);

    }

    /**获取站内消息详情**/
    public void getUserasitemess(String messid ,WZHttpListener wZHttpListener){

        String url = RequestPath.GET_USERSITEMESS;
        Map<String,String> map = new HashMap<>();
        map.put("code",RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("messid",messid);

        NetworkRequest.getRequest(url,map,wZHttpListener);

    }





}
