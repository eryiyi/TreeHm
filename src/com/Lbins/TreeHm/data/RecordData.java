package com.Lbins.TreeHm.data;


import com.Lbins.TreeHm.dao.RecordMsg;

import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class RecordData extends Data {
    private List<RecordMsg> data;

    public List<RecordMsg> getData() {
        return data;
    }

    public void setData(List<RecordMsg> data) {
        this.data = data;
    }
}
