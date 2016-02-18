package com.Lbins.TreeHm.data;

import com.Lbins.TreeHm.module.RecordVO;

import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class RecordData extends Data {
    private List<RecordVO> data;

    public List<RecordVO> getData() {
        return data;
    }

    public void setData(List<RecordVO> data) {
        this.data = data;
    }
}
