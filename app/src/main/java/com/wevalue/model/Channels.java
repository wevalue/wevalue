package com.wevalue.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/14
 * 类说明：
 */

public class Channels extends RealmObject {
    @PrimaryKey
    private String id;//": 1,//帖子类型id
    private String classname;//": "推荐",//帖子类型名称
    private String remark;//": "推荐推荐推荐",//帖子类型备注
    private String classorder;//": 1//帖子类型排序
    private String isLike;//是否为用户喜欢的频道

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getClassorder() {
        return classorder;
    }

    public void setClassorder(String classorder) {
        this.classorder = classorder;
    }
}
