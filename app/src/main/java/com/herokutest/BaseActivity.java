package com.herokutest;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.herokutest.widget.RefreshLayout;

/**
 * Created by xufangqiang on 2017/1/1.
 */

public class BaseActivity extends AppCompatActivity {

    private String REQUEST_TAG = "default";

    private RequestQueue requestQueue;

    private ActionBar actionBar;

    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initActionBar();
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setActionBarTitle(int resId) {
        setActionBarTitle(getString(resId));
    }

    protected void setActionBarTitle(String str) {
        actionBar.setTitle(str);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // return button
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue(boolean allowCancel, boolean hasPb) {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time

        if (requestQueue == null) {
            synchronized (BaseActivity.class) {
                if (requestQueue == null) {
                    requestQueue = Volley
                            .newRequestQueue(getApplicationContext());
                    requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                        @Override
                        public void onRequestFinished(Request<Object> request) {
                            if (refreshLayout != null) {
                                refreshLayout.setLoading(false);
                                refreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }
            }
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, RefreshLayout refreshLayout) {
        addToRequestQueue(req, true, refreshLayout, true, null);
    }

    public <T> void addToRequestQueue(Request<T> req, boolean hasPb, RefreshLayout refreshLayout) {
        addToRequestQueue(req, hasPb, refreshLayout, true, null);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req, boolean hasPb,
                                      RefreshLayout refreshLayout, boolean allowCancel, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? REQUEST_TAG : tag);
        req.setTag(REQUEST_TAG);
        this.refreshLayout = refreshLayout;
        getRequestQueue(allowCancel, hasPb).add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}

