package com.wevalue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.wevalue.WeValueApplication;

public class SharedPreferencesUtil {
    /**
     * Title: clearSharedPreferencesInfo<br>
     * Description: 删除指定sp中的数据<br>
     * Depend : TODO <br>
     *
     * @param context
     * @param itemName
     * @since JDK 1.7
     */
    // 清除缓存数据
    public static void clearSharedPreferencesInfo(Context context, String itemName) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences(itemName, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.clear().commit();
    }


    /*保存用户是否是大V  0 不是 1 是*/
    public static void setUserv(Context context, String s) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("userv", s).commit();
    }
    //获取用户是否是大V  0 不是 1 是
    public static String getUserv(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("userv", "");
    }

    /**
     * 保存热门城市
     */
    public static void setHotCity(Context context, String hotcity) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString("hotcity", hotcity).commit();
    }

    /**
     * 获取热门城市
     */
    public static String getHotCity(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("hotcity", "");
    }

    /**
     * 保存用户的登录方式
     */
    public static void setLoginWay(Context context, String loginWay) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString("loginWay", loginWay).commit();
    }

    /**
     * 获取用户的登录方式
     */
    public static String getLoginWay(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("loginWay", "");
    }

    /**
     * 保存用户的id
     */
    public static void setUid(Context context, String uid) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("uid", uid).commit();
    }

    /**
     * 获取用户的id
     */
    public static String getUid(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("uid", "");
    }

    /**
     * 保存用户的手机号
     */
    public static void setMobile(Context context, String mobile) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("mobile", mobile).commit();
    }

    /**
     * 获取用户的手机号
     */
    public static String getMobile(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("mobile", "");
    }

    /**
     * 保存用户上次登录的账号
     */
    public static void setZhangHao(Context context, String ZhangHao) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("ZhangHao", ZhangHao).commit();
    }

    /**
     * 获取用户上次登录的账号
     */
    public static String getZhangHao(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("ZhangHao", "");
    }

    /**
     * 保存用户的昵称
     */
    public static void setNickname(Context context, String nickname) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("nickname", nickname).commit();
    }

    /**
     * 获取用户的昵称
     */
    public static String getNickname(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("nickname", "");
    }

    /**
     * 保存用户的头像
     */
    public static void setAvatar(Context context, String avatar) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("avatar", avatar).commit();
    }

    /**
     * 获取用户的头像
     */
    public static String getAvatar(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("avatar", "");
    }

    /**
     * 保存用户二维码
     */
    public static void setQR_code(Context context, String avatar) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("QR_code", avatar).commit();
    }

    /**
     * 获取用户二维码
     */
    public static String getQR_code(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("QR_code", "");
    }

    /**
     * 保存用户的性别sex
     */
    public static void setSex(Context context, String sex) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("sex", sex).commit();
    }

    /**
     * 获取用户的性别sex
     */
    public static String getSex(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("sex", "");
    }

    /**
     * 保存用户积分
     */
    public static void setScore(Context context, String score) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("score", score).commit();
    }

    /**
     * 获取用户积分
     */
    public static String getScore(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("score", "");
    }

    /**
     * 保存上次获取城市列表的时间
     */
    public static void setGetAddrTime(Context context, long GetAddrTime) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong("GetAddrTime", GetAddrTime).commit();
    }

    /**
     * 获取上次获取城市列表的时间
     */
    public static long getGetAddrTime(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getLong("GetAddrTime", -1);
    }

    /**
     * 保存用户等级
     */
    public static void setUserleve(Context context, String userleve) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("userleve", userleve).commit();
    }

    /**
     * 获取用户等级
     */
    public static String getUserleve(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("userleve", "");
    }

    /**
     * 保存用户等级int型
     */
    public static void setUserLevelInt(Context context, int userleve) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("userleveint", userleve).commit();
    }

    /**
     * 获取用户等级int型
     */
    public static int getUserUserLevelInt(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getInt("userleveint", 0);
    }

    /**
     * 保存用户类型
     *///1.个人 2.组织
    public static void setUsertype(Context context, String Usertype) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Usertype", Usertype).commit();
    }

    /**
     * 获取类型
     */
    public static String getUsertype(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("Usertype", "");
    }

    /**
     * 保存用户简介
     */
    public static void setUserInfo(Context context, String userinfo) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("userinfo", userinfo).commit();
    }

    /**
     * 获取用户简介
     */
    public static String getUserInfo(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("userinfo", "");
    }

    /**
     * 保存邮箱
     */
    public static void setEmail(Context context, String Email) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Email", Email).commit();
    }

    /**
     * 获取获取邮箱
     */
    public static String getEmail(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("Email", "");
    }

    /**
     * 保存登录密码设置状态
     */
    public static void setLoginPswStatus(Context context, String LoginPswStatus) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("LoginPswStatus", LoginPswStatus).commit();
    }

    /**
     * 获取登录密码设置状态
     */
    public static String getLoginPswStatus(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("LoginPswStatus", "");
    }

    /**
     * 保存支付密码设置状态
     */
    public static void setPayPswStatus(Context context, String PayPswStatus) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("PayPswStatus", PayPswStatus).commit();
    }

    /**
     * 获取支付密码设置状态
     */
    public static String getPayPswStatus(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("PayPswStatus", "");
    }

    /**
     * 保存支付密码密保问题一
     */
    public static void setPayPswWenTi_1(Context context, String PayPswWenTi_1) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("PayPswWenTi_1", PayPswWenTi_1).commit();
    }

    /**
     * 获取支付密码密保问题一
     */
    public static String getPayPswWenTi_1(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("PayPswWenTi_1", "");
    }

    /**
     * 保存支付密码密保问题二
     */
    public static void setPayPswWenTi_2(Context context, String PayPswWenTi_2) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("PayPswWenTi_2", PayPswWenTi_2).commit();
    }

    /**
     * 获取支付密码密保问题二
     */
    public static String getPayPswWenTi_2(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("PayPswWenTi_2", "");
    }

    /**
     * 保存用户选择的频道
     */
    public static void setUserlike(Context context, String Userlike) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Userlike", Userlike).commit();
    }

    /**
     * 获取用户选择的频道
     */
    public static String getUserlike(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("Userlike", "");
    }

    /**
     * 保存所有的频道
     */
    public static void setAllChannel(Context context, String allchannel) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("allchannel", allchannel).commit();
    }

    /**
     * 获取所有的频道
     */
    public static String getAllChannel(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("allchannel", "");
    }

    /**
     * 保存用户微值号
     */
    public static void setUsernumber(Context context, String Usernumber) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Usernumber", Usernumber).commit();
    }

    /**
     * 获取用户微值号
     */
    public static String getUsernumber(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("Usernumber", "");
    }

//	/** 保存省市区*/
//	public static void setAllCity(Context context, String PayPswStatus) {
//		SharedPreferences sp = context.getSharedPreferences("System", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.putString("PayPswStatus", PayPswStatus).commit();
//	}
//	
//	/** 获取省市区*/
//	public static String getAllCity(Context context) {
//		SharedPreferences sp = context.getSharedPreferences("System", Context.MODE_PRIVATE);
//		return sp.getString("PayPswStatus", "");
//	}
//	/** 保存用户的密码 */
//	public static void setPassword(Context context, String pwd) {
//		SharedPreferences sp = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.putString("password", pwd).commit();
//	}
//
//	/** 获取用户的密码 */
//	public static String getPassword(Context context) {
//		SharedPreferences sp = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//		return sp.getString("password", "");
//	}


    /**
     * 保存城市ID
     */
    public static void setCityId(Context context, String city_id) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("city_id", city_id).commit();
    }

    /**
     * 获取城市ID
     */
    public static String getCityId(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("city_id", "");
    }

    /**
     * 保存城市Name
     */
    public static void setCityName(Context context, String cityName) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("cityName", cityName).commit();
    }

    /**
     * 获取城市Name
     */
    public static String getCityName(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("cityName", "");
    }

    /**
     * 保存省Name
     */
    public static void setProvinceName(Context context, String provinceName) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("provinceName", provinceName).commit();
    }

    /**
     * 获取省Name
     */
    public static String getProvinceName(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("provinceName", "");
    }

    /**
     * 保存用户所在区县
     */
    public static void setCounty(Context context, String provinceName) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("countyName", provinceName).commit();
    }

    /**
     * 获取用户所在区县
     */
    public static String getCounty(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("countyName", "");
    }

    /**
     * 保存用户所在的纬度
     */
    public static void setLatitude(Context context, String lat) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("lat", lat).commit();
    }

    /**
     * 获取用户所在的纬度
     */
    public static String getLatitude(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("lat", "");
    }

    /**
     * 保存用户所在的经度
     */
    public static void setLongitude(Context context, String lng) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("lng", lng).commit();
    }

    /**
     * 获取用户所在的经度
     */
    public static String getLongitude(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("lng", "");
    }

    /**
     * 保存用户所在的地址
     */
    public static void setAddress(Context context, String address) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("address", address).commit();
    }

    /**
     * 获取用户所在的地址
     */
    public static String getAddress(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("address", "");
    }

    /**
     * 保存用户屏幕的高度
     */
    public static void setPictureProportionHeight(Context context, String proportionHeight) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("proportionHeight", proportionHeight).commit();
    }

    /**
     * 获取用户屏幕的高度
     */
    public static String getPictureProportionHeight(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("proportionHeight", "");
    }

    /**
     * 保存用户是否第一次启动
     */
    public static void setIsFristStart(Context context, String booleanValue) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("isFristStart", booleanValue).commit();
    }

    /**
     * 获取用户是否第一次启动
     */
    public static String getIsFristStart(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("isFristStart", "");
    }

    /**
     * 保存是否接收推送
     */
    public static void setReceivePush(Context context, String isReceivePush) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("isReceivePush", isReceivePush).commit();
    }

    /**
     * 获取是否接收推送
     */
    public static String getReceivePush(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("isReceivePush", "true");
    }

    /**
     * 保存App下载路径
     */
    public static void setApppath(Context context, String appPath) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("appPath", appPath).commit();
    }

    /**
     * 获取App下载路径
     */
    public static String getApppath(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("appPath", "");
    }

    //保存世界模块的json串
    public static void setContent(Context context, String type, String content) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(type, content).commit();
    }

    //获取世界模块的json串
    public static String getContent(Context context, String type) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString(type, "");
    }

    //保存所有城市的列表
    public static void setAllCity(Context context, String content) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("allcity", content).commit();
    }

    //获取所有城市的列表
    public static String getAllCity(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("allcity", "");
    }

    //保存所有城市的列表
    public static void setDetailUserid(Context context, String content) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("detailuserid", content).commit();
    }

    //获取所有城市的列表
    public static String getDetailUserid(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("detailuserid", "");
    }

    //保存所有城市的列表
    public static void setDeviceid(Context context, String content) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("deviceid", content).commit();
    }

    //获取所有城市的列表
    public static String getDeviceid(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("deviceid", "");
    }


    //保存碎银数量
    public static void setSuiYinCount(Context context, String content) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("suiYinCount", content).commit();
    }

    //获取碎银数量
    public static String getSuiYinCount(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("suiYinCount", "0");
    }

    /*保存用户可提现碎银数量*/
    public static void setUserCanWithDraw(Context context, String s) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("CanWithDraw", s).commit();
    }

    //获取用户可提现碎银数量
    public static String getUserCanWithDraw(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("CanWithDraw", "");
    }

    //保存极光推送绑定状态
    public static void setJupushTagSet(Context context, Boolean b) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("jupushtag", b).commit();
    }

    //获取极光推送绑定状态
    public static Boolean getJupushTagSet(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getBoolean("jupushtag", false);
    }

    //保存当前定位城市
    public static void setLocationCity(Context context, String cityname) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("locationCity", cityname).commit();
    }

    //获取当前定位城市
    public static String getLocationCity(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("locationCity", "地区");
    }

    //保存用户偏好城市
    public static void setUserLikeCity(Context context, String cityname) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UserLikeCity", cityname).commit();
    }

    //获取用户偏好城市
    public static String getUserLikeCity(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("UserLikeCity", "地区");
    }

    //保存上次展示的城市
    public static void setLastCity(Context context, String cityname) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("LastCity", cityname).commit();
    }

    //获取上次保存的城市
    public static String getLastCity(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("System", Context.MODE_PRIVATE);
        return sp.getString("LastCity", "");
    }

    //保存用户的认证状态
    //0 未认证   1已经认证   2认证中   3认证失败
    public static void setUerAuthentic(Context context, String s) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UerAuthentic", s).commit();
    }

    //获取用户的认证状态
    public static String getUerAuthentic(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("UerAuthentic", "");
    }

    /*保存组织名称*/
    public static void setZuZzhiname(Context context, String s) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("ZuZzhiname", s).commit();
    }

    //获取用户的认证状态
    public static String getZuZzhiname(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("ZuZzhiname", "");
    }

    /*保存用户免费帖子数量*/
    public static void setUserPower(Context context, String s) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UserPower", s).commit();
    }

    //获取用户免费帖子数量
    public static String getUserPower(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("UserPower", "");
    }
    /*保存用户删除的帖子id*/
    public static void setUserDelNoteId(Context context, String s) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("DelNoteId", s).commit();
    }

    //获取用户删除的帖子id
    public static String getUserDelNoteId(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("DelNoteId", "");
    }
    /*保存用户登录token*/
    public static void setUserToken(Context context, String s) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UserToken", s).commit();
    }

    //获取用户登录token
    public static String getUserToken(Context context) {
        SharedPreferences sp = WeValueApplication.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sp.getString("UserToken", "noToken");
    }
}
