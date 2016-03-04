package com.Lbins.TreeHm.data;


import com.Lbins.TreeHm.dao.RecordMsg;

import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class RecordDataSingle extends Data {
    private RecordMsg data;

    public RecordMsg getData() {
        return data;
    }

    public void setData(RecordMsg data) {
        this.data = data;
    }
}
