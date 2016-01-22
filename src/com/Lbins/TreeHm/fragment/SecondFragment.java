package com.Lbins.TreeHm.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.base.BaseFragment;

/**
 * Created by Administrator on 2016/1/22.
 */
public class SecondFragment extends BaseFragment {
    private View view;
    private Resources res;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        initView();

        return view;
    }

    void initView( ){
        //
    }

}
