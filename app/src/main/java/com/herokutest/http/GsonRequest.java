package com.herokutest.http;

/**
 * Created by Kain on 2015/12/4.
 */

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.herokutest.AppApplication;
import com.herokutest.exception.CustomException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 */
public class GsonRequest<T> extends Request<T> {
    private Gson mGson;
    private Map<String, String> params;
    private Response.Listener<T> listener;
    private Type type;
    private String url;

    private static final String exceptionStr = "com.youngt.maidanfan.http.CustomException:";

    /**
     * Make a GET request and return a parsed object from JSON.
     * without errorListener
     *
     * @param url  URL of the request to make
     * @param type Relevant class object, for Gson's reflection
     */
    public GsonRequest(String url,
                       Type type,
                       Response.Listener<T> listener) {

        super(Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String s;
                String errorStr = error.getMessage();
                if (TextUtils.isEmpty(errorStr)
                        || error.getCause() instanceof UnknownHostException
                        || error.getCause() instanceof RuntimeException
                        || error.getCause() instanceof ConnectException) {
                    s = "网络异常";
                } else if (error.getCause() instanceof JsonSyntaxException) {
                    s = "";
                } else {
                    if (errorStr.indexOf(":") != -1) {
                        s = errorStr.substring(errorStr.indexOf(":") + 1);
                    } else {
                        s = errorStr.replace(exceptionStr, "");
                    }
                }
                if (!TextUtils.isEmpty(s)) {
                    Toast.makeText(AppApplication.getInstance(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.listener = listener;
        this.url = url;
        this.type = type;
        mGson = new Gson();
    }

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url           URL of the request to make
     * @param type          Relevant class object, for Gson's reflection
     * @param listener      for success listener
     * @param errorListener for error listener
     */
    public GsonRequest(String url,
                       Type type,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {

        super(Method.GET, url, errorListener);
        this.listener = listener;
        this.url = url;
        this.type = type;
        mGson = new Gson();
    }

    /**
     * Make a POST request and return a parsed object from JSON.
     * without errorListener
     *
     * @param url  URL of the request to make
     * @param type Relevant class object, for Gson's reflection （or Class）
     */
    public GsonRequest(String url,
                       Type type,
                       Map<String, String> params,
                       Response.Listener<T> listener) {

        super(Method.POST, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String s;
                String errorStr = error.getMessage();
                if (TextUtils.isEmpty(errorStr)
                        || error.getCause() instanceof UnknownHostException
                        || error.getCause() instanceof RuntimeException
                        || error.getCause() instanceof ConnectException) {
                    s = "网络异常";
                } else if (error.getCause() instanceof JsonSyntaxException) {
                    s = "";
                } else {
                    if (errorStr.indexOf(":") != -1) {
                        s = errorStr.substring(errorStr.indexOf(":") + 1);
                    } else {
                        s = errorStr.replace(exceptionStr, "");
                    }
                }
                if (!TextUtils.isEmpty(s)) {
                    Toast.makeText(AppApplication.getInstance(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });
//        this.clazz = clazz;
        this.type = type;
        this.params = params;
        this.url = url;
        this.listener = listener;
        mGson = new Gson();
    }

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url           URL of the request to make
     * @param type          Relevant class object, for Gson's reflection （or Class）
     * @param listener      for success listener
     * @param errorListener for error listener
     */
    public GsonRequest(String url,
//                       Class<T> clazz,
                       Type type,
                       Map<String, String> params,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
//        this.clazz = clazz;
        this.type = type;
        this.params = params;
        this.url = url;
        this.listener = listener;
        mGson = new Gson();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers1 = new HashMap<String, String>();
        return headers1;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return params;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    private boolean isSuccess = false;

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String msg = "网络异常";
        String code = "0";
        try {
            Log.e("GsonRequest", new String(response.data));
            String jsonStr = new String(response.data);
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.has("code")) {
                code = jsonObject.getString("code");
                msg = jsonObject.getString("msg");
            }
            Log.e("gsonurl", url + "+++code = " + code);
            if (code.equals("0")) {
                isSuccess = true;
                return (Response<T>) Response.success(mGson.fromJson(jsonStr, type),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new ParseError(new CustomException(msg)));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            if (isSuccess) {
                return Response.error(new ParseError(new CustomException("暂无数据")));
            } else {
                return Response.error(new ParseError(new CustomException("解析异常")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(new CustomException("解析异常")));
        }
    }
}