package com.wevalue.model;

/**
 * 情绪实体类
 */
public class EmotionBean {
    String name;
    String id;
    String isClick;
    int imgint;

    public int getImgint() {
        return imgint;
    }

    public void setImgint(int imgint) {
        this.imgint = imgint;
    }

    public EmotionBean(String name, String isClick ,int imgint) {
        this.name = name;
        this.isClick = isClick;
        this.imgint = imgint;
    }

    public EmotionBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsClick() {
        return isClick;
    }

    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }
}
