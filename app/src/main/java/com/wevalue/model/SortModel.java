package com.wevalue.model;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/9
 * 类说明：排序模板
 */

public class SortModel {

    private String name;//显示用户姓名
    private String userId;//用户的id
    private String sortLetters;//显示数据拼音的首字母
    private String icon;//用户头像的路径
    private String jianjie;//个人简介信息
    private String userlevel;//用户的等级水平
    private String isFriend;//是否为好友
    private String isFans;//是否是粉丝
    private String isFocuse;//是否关注

    public SortModel() {
    }

    public SortModel(String name, String userId, String icon, String jianjie, String isFocuse, String userlevel, String isFriend) {
        this.name = name;
        this.userId = userId;
        this.icon = icon;
        this.jianjie = jianjie;
        this.isFocuse = isFocuse;
        this.userlevel = userlevel;
        this.isFriend = isFriend;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(String userlevel) {
        this.userlevel = userlevel;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getIsFans() {
        return isFans;
    }

    public void setIsFans(String isFans) {
        this.isFans = isFans;
    }

    public String getIsFocuse() {
        return isFocuse;
    }

    public void setIsFocuse(String isFocuse) {
        this.isFocuse = isFocuse;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
