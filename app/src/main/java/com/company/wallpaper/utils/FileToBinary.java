package com.company.wallpaper.utils;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.company.wallpaper.R;
import com.company.wallpaper.listener.DownloadListener;
import com.company.wallpaper.ui.VideoActivity;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ysy.commonlib.utils.DialogUtils;

import org.apache.commons.codec.EncoderException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yushengyang.
 * Date: 2019-11-29.
 */


public class FileToBinary {

    public static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 网络文件转换为byte二进制
     *
     * @param @param  url
     * @param @return
     * @param @throws IOException    设定文件
     * @return byte[]    返回类型
     * @throws
     * @Title: toByteArray
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static byte[] toByteArray(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        DataInputStream in = new DataInputStream(conn.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * @param @param  url
     * @param @return
     * @param @throws IOException    设定文件
     * @return byte[]    返回类型
     * @throws IOException
     * @throws MalformedURLException 网络文件转换为本地文件
     * @throws
     * @Title: toByteArray
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static void toBDFile(String urlStr, String bdUrl) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        DataInputStream in = new DataInputStream(conn.getInputStream());
        byte[] data = toByteArray(in);
        in.close();
        FileOutputStream out = new FileOutputStream(bdUrl);
        out.write(data);
        out.close();
    }

    /**
     * 直接获取网络文件的md5值
     * @param urlStr
     * @return
     */
//    public static String getMd5ByUrl(String urlStr){
//        String md5 = null;
//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            DataInputStream in = new DataInputStream(conn.getInputStream());
//            md5 = DigestUtils.md5Hex(in);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return md5;
//    }

    /**
     * 获取网络文件的输入流
     *
     * @param urlStr
     * @return
     */
    public static InputStream getInputStreamByUrl(String urlStr) {
        DataInputStream in = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = new DataInputStream(conn.getInputStream());
        } catch (IOException e) {
            Log.e("url转换输入流失败,错误信息{}", e.getMessage());
        }
        return in;
    }

    /**
     * 获取网络文件，暂存为临时文件
     * @param url
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public static File getFileByUrl(String url) throws UnknownHostException, IOException{
        File tmpFile = File.createTempFile("temp", ".tmp");//创建临时文件
        toBDFile(url, tmpFile.getCanonicalPath());
        return tmpFile;
    }

    /**
     * 获取时长
     * @param url
     * @return
     * @throws IOException
     */
    public static void getDuration(String url,onVideoSizeListener listener) {
        // Multi image selector form an Activity
//        File file = new File(new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+"wallpaperVideo"), "video.mp4");
//        DownUtils.download(url, file.getAbsolutePath(), new DownloadListener() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onProgress(int progress) {
//
//            }
//
//            @Override
//            public void onFinish(String path) {
//
//            }
//
//            @Override
//            public void onFail(String errorInfo) {
//                Log.e("VideoDur","error:"+errorInfo);
//            }
//        });


    }

    public interface onVideoSizeListener{
        void getSize(long size);
    }
}