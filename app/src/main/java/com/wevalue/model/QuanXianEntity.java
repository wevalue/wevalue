package com.wevalue.model;

import java.util.List;

/**
 * Created by Administrator on 2016-11-19.
 */

public class QuanXianEntity {


    /**
     * result : 0
     * message :
     * data : [{"money":"1","number":"10"},{"money":"2","number":"22"},{"money":"3","number":"35"},{"money":"0","number":"0"},{"money":"0","number":"0"}]
     */

    private int result;
    private String message;
    /**
     * money : 1
     * number : 10
     */

    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String money;
        private String number;
        private boolean isClick ;

        public boolean getIsClick() {
            return isClick;
        }

        public void setIsClick(boolean isClick) {
            this.isClick = isClick;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
