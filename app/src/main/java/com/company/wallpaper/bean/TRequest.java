package com.company.wallpaper.bean;



/**
 * Created by ysy on 2018/1/16.
 */

public class TRequest<T> {
    public T getwParam() {
        return wParam;
    }

    public String getwSign() {
        return wSign;
    }

    public String getwAction() {
        return wAction;
    }

    public String getwAgent() {
        return wAgent;
    }

    private static final String KEY = "sw1kd2dfFD323rfdlniji";

    public void setParam(T Param, String Action, String Agent) {
        wParam = Param;
        wAction = Action;
        wAgent = Agent;
//        setAgent();
    }

    public void setParamImg(T Param, String Action, String Agent) {
        wParam = Param;
        wAction = Action;
        wAgent = Agent;
//        setAgentImg();
    }

    public void setParamComment(T Param, String Action, String Agent) {
        wParam = Param;
        wAction = Action;
        wAgent = Agent;
//        setAgentComment();
    }

    private T wParam;
    private String wSign;
    private String wAction;
    private String wAgent;
//
//    private void setAgent() {
//        Gson gson = new Gson();
//        Log.i(FirstPageFragment.TAG, wAction + wAgent + gson.toJson(wParam) + KEY);
//        wSign = MD5Util.md5(wAction + wAgent + gson.toJson(wParam) + KEY);
//    }
//
//    private void setAgentImg() {
//        Gson gson = new Gson();
//        Log.i(FirstPageFragment.TAG, wAction + wAgent + gson.toJson(wParam) + KEY);
//        wSign = MD5Util.md5(wAction + wAgent + KEY);
//    }
//
//    private void setAgentComment() {
//        Gson gson = new Gson();
//        Log.i(FirstPageFragment.TAG, wAction + wAgent + gson.toJson(wParam) + KEY);
//        String str = wAction + wAgent + gson.toJson(wParam) + KEY;
//        String s = str.replaceAll(" ", "");
//        wSign = MD5Util.md5(s);
//    }
}
