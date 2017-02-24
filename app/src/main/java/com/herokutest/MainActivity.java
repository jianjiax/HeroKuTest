package com.herokutest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.herokutest.adapter.TestAdapter;
import com.herokutest.http.GsonRequest;
import com.herokutest.model.BaseModel;
import com.herokutest.model.TestModel;
import com.herokutest.widget.RefreshLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private final String TEST_URL = "http://thoughtworks-ios.herokuapp.com/facts.json";

    private RefreshLayout refresh_rl;

    private ListView main_lv;

    private TestAdapter testAdapter;

    private ArrayList<TestModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        refresh_rl = (RefreshLayout) findViewById(R.id.refresh_rl);
        main_lv = (ListView) findViewById(R.id.main_lv);
        testAdapter = new TestAdapter(this, dataList);
        main_lv.setAdapter(testAdapter);

        request(TEST_URL, new TypeToken<BaseModel>() {
        }.getType(), new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel response) {
                dataList = response.getRows();
                testAdapter.setData(dataList);
            }
        });
    }

    private void request(String url, Type type, Response.Listener listener) {
        String paramStr;
        GsonRequest request = new GsonRequest(url, type, listener);
        addToRequestQueue(request, false, refresh_rl);
    }
}
