package com.wevalue.model;

/**
 * Created by Administrator on 2016-06-12.
 */
public class MailListEntity {

    public String userName;
    public String userPhone;

    public MailListEntity(){

    }
    public MailListEntity(String name, String userPhone){
        this.userPhone = userPhone;
        this.userName = name;
    }

    public String getuserPhone() {
        return userPhone;
    }

    public void setuserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }


//    [
//    {
//        "userPhone" : "13700000000",
//            "userName" : "KateBell"
//    },
//    {
//        "userPhone" : "13700000001",
//            "userName" : "DanielHiggins"
//    },
//    {
//        "userPhone" : "13700000002",
//            "userName" : "JohnAppleseed"
//    },
//    {
//        "userPhone" : "13700000003",
//            "userName" : "AnnaHaro"
//    },
//    {
//        "userPhone" : "13700000004",
//            "userName" : "HankM.Zakroff"
//    },
//    {
//        "userPhone" : "13700000005",
//            "userName" : "DavidTaylor"
//    }
//    ]


    @Override
    public String toString() {
        return
                "{\"userName\":" + "\""+userName +"\"," +"\"userPhone\":\"" + userPhone +"\"}";
    }
}
