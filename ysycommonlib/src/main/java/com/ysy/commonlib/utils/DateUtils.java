package com.ysy.commonlib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yushengyang.
 * Date: 2018/12/11.
 */

public class DateUtils {

    public static long Date2ms(String _data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(_data);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }


}
