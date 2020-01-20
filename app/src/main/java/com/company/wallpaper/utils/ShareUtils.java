package com.company.wallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.company.wallpaper.bean.ListHistoryBean;
import com.company.wallpaper.bean.UserInfoBean;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
public class ShareUtils {
    private static String LOCALFILE_CONFIG = "wallpaper_config";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(LOCALFILE_CONFIG, 0);
        editor = sharedPreferences.edit();
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static  void putSearch(String content){
        ListHistoryBean search_history = new Gson().fromJson(ShareUtils.getString("search_history"), ListHistoryBean.class);
        if (search_history == null || search_history.getList() == null) {
            search_history = new ListHistoryBean(new ArrayList<>());
        }
        if (search_history.getList().indexOf(content) == -1) {
            search_history.getList().add(content);
        }
        ShareUtils.putString("search_history", new Gson().toJson(search_history));
    }
    public static  void clearSearch(){
        ShareUtils.putString("search_history", "");
    }
    public static void putToken(String Token) {
        if (Token != null && !Token.isEmpty()) {
            putString("token", Token);
        }
    }
    public static void putUserInfo(UserInfoBean bean) {
        if (bean != null) {
            putString("userInfo", new Gson().toJson(bean));
        }
    }

    public static UserInfoBean getUserInfo() {
        if (getString("userInfo") == null || getString("userInfo").equals("")) {
            return null;
        } else {
            UserInfoBean userInfo = new Gson().fromJson(getString("userInfo"), UserInfoBean.class);
            if (userInfo.getUserId() == 0) {
                clearUserInfo();
                return null;
            }
            return userInfo;
        }
    }

    public static void clearToken() {
        putString("token", "");
    }


    public static String getToken() {
        if (getString("token") == null) {
            return "";
        } else {
            return getString("token");
        }
    }

    public static void clearUserInfo() {
        putString("userInfo", "");
    }



    public static int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static boolean getIBoolean(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public static boolean getIBoolean(String key, boolean defalut) {
        return sharedPreferences.getBoolean(key, defalut);
    }

    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void deleteValue(String key) {
        editor.remove(key);
        editor.commit();
    }

    public static void putLong(String key, Long L) {
        editor.putLong(key, L);
        editor.commit();
    }

    public static Long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }
    /******对象序列化本地存取 begin******/
    /**
     * desc:保存对象
     *
     * @param context
     * @param key
     * @param obj     要保存的对象，只能保存实现了serializable的对象
     *                modified:
     */
    public static void setObject(Context context, String key, Object obj) {
        try {
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            editor.putString(key, bytesToHexString);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param context
     * @param key
     * @return modified:
     */
    public static Object getObject(Context context, String key) {
        try {

            if (sharedPreferences.contains(key)) {
                String string = sharedPreferences.getString(key, "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(string);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //所有异常返回null
        return null;

    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * desc:将16进制的数据转为数组
     * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
     *
     * @param data
     * @return modified:
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }
    /******对象序列化本地存取 end******/
}
