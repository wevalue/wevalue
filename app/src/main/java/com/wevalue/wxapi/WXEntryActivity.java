package com.wevalue.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wevalue.R;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {
    IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        //注册API
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
            //获取微信传回的code
            final String code = newResp.code;
//            Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
            HashMap map = new HashMap();
            map.put("appid", Constants.APP_ID);
            map.put("secret", Constants.APP_SECRET);
            map.put("code", code);
            map.put("grant_type", "authorization_code");
//            {
//                "access_token":"ACCESS_TOKEN",
//                    "expires_in":7200,
//                    "refresh_token":"REFRESH_TOKEN",
//                    "openid":"OPENID",
//                    "scope":"SCOPE"
//            }
            NetworkRequest.getRequestForNoSecurity(RequestPath.GET_WXOPENID, map, new WZHttpListener() {
                @Override
                public void onSuccess(String content, String isUrl) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String access_token = jsonObject.getString("access_token");
                        String openid = jsonObject.getString("openid");
                        HashMap map = new HashMap();
                        map.put("access_token", access_token);
                        map.put("openid", openid);
                        NetworkRequest.getRequestForNoSecurity(RequestPath.GET_WXUSERINFO, map, new WZHttpListener() {
                            @Override
                            public void onSuccess(String content, String isUrl) {
                                try {
                                    JSONObject object = new JSONObject(content);
                                    String nickname = object.getString("nickname");
                                    String openid = object.getString("openid");
                                    String headimgurl = object.getString("headimgurl");
                                    Intent intent = new Intent();
                                    intent.setAction("com.wevalue.wxlogin");
                                    intent.putExtra("request", "1");
                                    intent.putExtra("nickname", nickname);
                                    intent.putExtra("openid", openid);
                                    intent.putExtra("headimgurl", headimgurl);
                                    sendBroadcast(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    sendErrorCodeBroadCast();
                                }
                            }

                            @Override
                            public void onFailure(String content) {
                                sendErrorCodeBroadCast();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sendErrorCodeBroadCast();
                    }
                }
                @Override
                public void onFailure(String content) {
                    sendErrorCodeBroadCast();
                }
            });
        }
    }

    private void sendErrorCodeBroadCast() {
        Intent intent = new Intent();
        intent.setAction("com.wevalue.wxlogin");
        intent.putExtra("request", "0");
        sendBroadcast(intent);
    }


    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
//            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
//            ShowUtil.showToast(this,obj.extInfo);
            Intent intent = new Intent();
            intent.setAction("com.wevalue.wxshare");
            intent.putExtra("extInfo", obj.extInfo);
            sendBroadcast(intent);
        }
    }
}
