package com.ysy.commonlib.utils;

import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lixingjun on 7/1/16.
 */
public class StringUtils {

    /**
     * 获取当前时间 如：2016-07-01 18:01:10
     *
     * @return
     */
    public static String getFormatNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }

    public static boolean isToday(String time) {
        return time.split(" ")[0].equals(getFormatNowDate().split(" ")[0]);
    }

    /*
     * 将时间戳转换为时间
     */
    public static Date stampToDate(String s, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断字符串是否没有值
     *
     * @param s
     * @return
     */
    public static boolean isEmptyString(String s) {
        boolean ret = false;
        if ((s == null) || (s.length() == 0))
            ret = true;
        return ret;
    }

    /**
     * 字符串转int
     *
     * @param str
     * @return
     */
    public static int str2int(String str) {
        int val = 0;
        try {
            val = Integer.parseInt(str);
        } catch (NumberFormatException e) {

        }
        return val;
    }


    public static Spanned htmlFontClor(Map<String, String> map) {
        return Html.fromHtml(convertToHtml("<font color='#145A14'>text</font>"));
    }

    public static String convertToHtml(String htmlString) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<![CDATA[");
        stringBuilder.append(htmlString);
        stringBuilder.append("]]>");
        return stringBuilder.toString();
    }

    public static SpannableStringBuilder changeStringColor(String Content, String ColorRGB, int Start, int End) {
        SpannableStringBuilder builder = new SpannableStringBuilder(Content);
        ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(Color.parseColor(ColorRGB));
        builder.setSpan(blueColorSpan, Start, End, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 随机生成字符串
     *
     * @param length 字符串的长度
     * @param source 规定的字符
     * @return 随机字符串
     */
    public static String createRandomString(int length, String source) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int position = random.nextInt(source.length());
            builder.append(source.charAt(position));
        }
        return builder.toString();
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static String getAlignContent(String str) {
        return ToDBC(str);
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {// 全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 保留两位小数
     *
     * @param num
     * @return
     */
    public static String DoubleFormat(double num) {
        if (isIntegerForDouble(num)) {
            return ((int) num) + "";
        }
        return String.format("%.2f", num);
    }

    /**
     * 保留一位小数
     *
     * @param num
     * @return
     */
    public static String DoubleOneFormat(double num) {
        if (isIntegerForDouble(num)) {
            return ((int) num) + "";
        }
        return String.format("%.1f", num);
    }

    /**
     * 判断double是否是整数
     *
     * @param obj
     * @return
     */
    public static boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj - Math.floor(obj) < eps;
    }

    public static String LargeCountFormat(int count) {
        if (count > 10000) {
            return StringUtils.DoubleOneFormat((double) count / 10000) + "万";
        } else {
            return count + "";
        }
    }

    public static String timeToString(long time) {
        int sec;
        int minute;
        int hour;
        int day;
        int minuteTime = 60;
        int hourTime = minuteTime * 60;
        int dayTime = hourTime * 24;
        sec = (int) (time % 60);
        minute = (int) ((time / minuteTime) % 60);
        hour = (int) ((time / hourTime) % 24);
        day = (int) (time / dayTime);
        if (day > 0) {
            return day + "天" + hour + "小时";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟" + sec + "秒";
        }
        return sec + "秒";
    }
}
