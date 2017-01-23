package com.wevalue.net.payment;

import java.util.HashMap;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/5
 * 类说明：支付成功的接口回调
 */

public interface PayInterface {
    void paySucceed(HashMap map);

    void payStart(String startType);

    void payFail(String failString);
}
