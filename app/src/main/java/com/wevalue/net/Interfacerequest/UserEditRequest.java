package com.wevalue.net.Interfacerequest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.WeValueApplication;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息修改
 */
public class UserEditRequest {


    private Context mContext;
    public static UserEditRequest userEditRequest;

    public UserEditRequest(Context context) {
        mContext = context;
    }

    public static UserEditRequest initUserEditRequest(Context context) {

        if (userEditRequest != null) {
            return userEditRequest;
        } else {
            userEditRequest = new UserEditRequest(context);
            return userEditRequest;
        }

    }

    /**
     * 开启或关闭免密支付
     *  @param money 开启金额上限 比如 5块以下免密支付
     * @param type  0 关闭， 1- 开启
     * @param wzHttpListener
     */
    public  void openOnepay(String money, boolean type, WZHttpListener wzHttpListener) {
        String url = RequestPath.POST_OPENONEPAY;
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(WeValueApplication.applicationContext));
        map.put("money", money);
        if (type) map.put("type", "1");
        else map.put("type", "0");
        NetworkRequest.postRequest(url, map,wzHttpListener);
    }


    /**
     * 修改城市
     */
    public void setCityData(final String provinceName, final String cityName, final TextView tv_addr) {

        String url = RequestPath.POST_UPDATEUSERINFO;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("userprovince", provinceName);
        map.put("usercity", cityName);
        NetworkRequest.postRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(mContext, obj.getString("message"));
                        if (provinceName.equals(cityName)) {
                            tv_addr.setText(provinceName);
                        } else {
                            tv_addr.setText(provinceName + " " + cityName);
                        }
                    } else {
                        ShowUtil.showToast(mContext, obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(mContext, content);
            }
        });

    }

    /**
     * 修改性别
     */
    public void returnData(String sex, final String sex_2, final Activity activity, final TextView tv_sex) {

        if (sex.equals(sex_2)) {
            activity.finish();
        } else {
            String url = RequestPath.POST_UPDATEUSERINFO;
            Map<String, Object> map = new HashMap<>();
            map.put("code", RequestPath.CODE);
            map.put("userid", SharedPreferencesUtil.getUid(activity));
            map.put("usersex", sex_2);
            NetworkRequest.postRequest(url, map, new WZHttpListener() {
                @Override
                public void onSuccess(String content, String isUrl) {
                    try {
                        JSONObject obj = new JSONObject(content);

                        if (obj.getString("result").equals("1")) {
                            ShowUtil.showToast(mContext, obj.getString("message"));
                            tv_sex.setText(sex_2);
                        } else {
                            ShowUtil.showToast(mContext, obj.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String content) {
                    ShowUtil.showToast(mContext, content);
                }
            });
        }

    }

    /**
     * 修改用户头像
     */
    public void editAvatar(final String baseStr, final Bitmap newbiBitmap, final ImageView iv_avatar) {
        String url = RequestPath.POST_UPDATE_USERFACE;
        //请求服务器 上传头像
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("userface", baseStr);

        NetworkRequest.postRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(mContext, obj.getString("message"));
                        iv_avatar.setImageBitmap(newbiBitmap);
                        SharedPreferencesUtil.setAvatar(mContext, obj.getString("url"));
                    } else {
                        ShowUtil.showToast(mContext, obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(mContext, content);
            }
        });

    }


}
