package com.Lbins.TreeHm.data;


import com.Lbins.TreeHm.module.CityObj;
import com.Lbins.TreeHm.module.ProvinceObj;

import java.util.List;

/**
 * Created by Administrator on 2016/2/10.
 */
public class CityData extends Data {
    private List<CityObj> data;

    public List<CityObj> getData() {
        return data;
    }

    public void setData(List<CityObj> data) {
        this.data = data;
    }
}
