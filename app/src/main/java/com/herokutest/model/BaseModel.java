package com.herokutest.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * root model
 * Created by xufangqiang on 2017/2/24.
 */

public class BaseModel implements Serializable {
    private String title;
    private ArrayList<TestModel> rows;

    public ArrayList<TestModel> getRows() {
        return rows;
    }

    public void setRows(ArrayList<TestModel> rows) {
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
