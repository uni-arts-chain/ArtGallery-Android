package com.artgallery.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.artgallery.R;
import com.artgallery.entity.DappFunctionBean;

import java.util.List;

public class DAppFunctionAdapter extends BaseQuickAdapter<DappFunctionBean, BaseViewHolder> {


    public DAppFunctionAdapter(@Nullable @org.jetbrains.annotations.Nullable List<DappFunctionBean> data) {
        super(R.layout.item_dapp_function_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DappFunctionBean item) {
        helper.setText(R.id.tv_function_name, item.getDappName());
        helper.setBackgroundRes(R.id.img_function, item.getDappIcon());
    }
}
