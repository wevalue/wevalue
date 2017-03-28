package com.wevalue.utils;

/**
 * 
 * Title:  ButtontimeUtil<br>
 * Description: TODO  防止按钮连续点击工具类<br>
 * Depend : TODO 
 * @author xuzhuchao
 * @Modified by 
 * @CreateDate 2015年11月18日下午2:45:29
 * @copyright 灵韬致胜（北京）科技发展有限公司
 * @license   http://www.i500m.com
 * @link      xuzhuchao@iyangpin.com
 * @Version 
 * @Revision 
 * @ModifiedDate 
 * @since JDK 1.7
 */
public class ButtontimeUtil {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1300) {
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
}