package com.wevalue.net.requestbase;



/**
 * Created by Administrator on 2016-06-24.
 */
  public  interface   WZHttpListener {

    /**
     * 请求成功.
     * @param content the content
     */
    void onSuccess(String content, String isUrl);

    ;


    /**
     * 请求失败.
     * @param content the content
     */
    void onFailure(String content);


}
