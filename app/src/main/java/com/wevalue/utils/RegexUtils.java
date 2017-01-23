package com.wevalue.utils;

import java.util.regex.Pattern;

/**
 * Title: RegexUtils<br>
 * Description: TODO 正则表达示<br>
 * Depend : TODO
 *
 * @author xuzhuchao
 * @Modified by
 * @CreateDate 2015年11月27日下午4:37:53
 * @copyright 灵韬致胜（北京）科技发展有限公司
 * @license http://www.i500m.com
 * @link xuzhuchao@iyangpin.com
 * @Version
 * @Revision
 * @ModifiedDate
 * @since JDK 1.7
 */
public class RegexUtils {
    /**
     * 密码
     */
    public static boolean pwdRegex(String pwd) {
        // 验证通过返回true否则返回false
        return Pattern.matches("^(?![^a-zA-Z]+$)(?!\\D+$).{6,12}$", pwd);
    }


    /**
     * 需要包含字母和数字的正则验证
     */
    public static boolean isLetterDigitAndChinese(String str) {
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{6,16})$";
        return str.matches(regex);
    }

    /**
     * 手机号
     */
    public static boolean etPhoneRegex(String etPhone) {
        // 验证通过返回true否则返回false
        return Pattern.matches("^[1][3-8]+\\d{9}$", etPhone);
    }

    /**
     * 身份证号  弃用！！！！！！！！！！！！！！！！！！！！！！！
     */
    public static boolean identity_car(String identity_card) {
        // 验证通过返回true否则返回false
        return Pattern.matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))+$", identity_card);
    }

//    /*身份证号正则验证*/   "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$"
    /*^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$
*/

    public static boolean identity_card(String identity_card) {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$", identity_card);
    }


    /**
     * 名字
     */
    public static boolean etName(String name) {
        // 验证通过返回true否则返回false
        return Pattern.matches("^[a-zA-Z0-9\u4e00-\u9fa5]{2,10}+$", name);
    }

    /**
     * 邮箱正则
     */
    public static boolean etEmail(String email) {
        // 验证通过返回true否则返回false
        return Pattern.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$", email);
    }

    // 验证金额
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(0(?:[.](?:[1-9]\\d?|0[1-9]))|[1-9]\\d*(?:[.]\\d{1,2}|$))$"); // 判断小数点后一位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }


}