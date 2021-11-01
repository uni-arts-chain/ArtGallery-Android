package com.artgallery.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.artgallery.R;
import com.artgallery.entity.WalletLinkBean;

import java.util.List;

public class WalletLinksAdapter extends BaseQuickAdapter<WalletLinkBean, BaseViewHolder> {

    List<WalletLinkBean> list;

    public WalletLinksAdapter(List<WalletLinkBean> list) {
        super(R.layout.item_wallet_links_layout, list);
        this.list = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletLinkBean item) {
        helper.setImageResource(R.id.img_links_icon, item.getImgId());
        helper.setText(R.id.tv_links_short_name, item.getShortName());
        helper.setText(R.id.tv_links_name, item.getName());
    }
}
