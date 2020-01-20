package com.company.wallpaper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by ysy on 2018/1/29.
 */

public class PatternUtils {
    /**
     * 手机号验证
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
//        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        if (str == null) {
            return false;
        }
        String regExp = "^\\d{11}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 6位验证码验证
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isCode(String str) throws PatternSyntaxException {
        String regExp = "^\\d{6}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 银行卡验证（长度大于16位）
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isBankCard(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile("\\d{16,19}");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 密码验证
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isPassword(String str) throws PatternSyntaxException {
        String regExp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 身份证验证
     *
     * @param IDNumber
     * @return
     */
    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        } else {
            return true;
        }
//        if (IDNumber.length() == 10) {
//            return true;
//        }
//        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
//        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" + "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
//        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
//        //^开头
//        //[1-9] 第一位1-9中的一个      4
//        //\\d{5} 五位数字           10001（前六位省市县地区）
//        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
//        //\\d{2}                    91（年份）
//        //((0[1-9])|(10|11|12))     01（月份）
//        //(([0-2][1-9])|10|20|30|31)01（日期）
//        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
//        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
//        //$结尾
//        //假设15位身份证号码:410001910101123  410001 910101 123
//        //^开头
//        //[1-9] 第一位1-9中的一个      4
//        //\\d{5} 五位数字           10001（前六位省市县地区）
//        //\\d{2}                    91（年份）
//        //((0[1-9])|(10|11|12))     01（月份）
//        //(([0-2][1-9])|10|20|30|31)01（日期）
//        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
//        //$结尾
//        boolean matches = IDNumber.matches(regularExpression);
//        //判断第18位校验值
//        if (matches) {
//            if (IDNumber.length() == 18) {
//                try {
//                    char[] charArray = IDNumber.toCharArray();
//                    //前十七位加权因子
//                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
//                    //这是除以11后，可能产生的11位余数对应的验证码
//                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
//                    int sum = 0;
//                    for (int i = 0; i < idCardWi.length; i++) {
//                        int current = Integer.parseInt(String.valueOf(charArray[i]));
//                        int count = current * idCardWi[i];
//                        sum += count;
//                    }
//                    char idCardLast = charArray[17];
//                    int idCardMod = sum % 11;
//                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
//                        return true;
//                    } else {
//                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() + "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
//                        return false;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("异常:" + IDNumber);
//                    return false;
//                }
//            }
//        }
//        return matches;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str) {
        if (str.trim().equals("")) {
            return true;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
        }
        return false;
    }
}
