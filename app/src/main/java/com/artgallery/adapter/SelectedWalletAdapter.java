package com.artgallery.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.artgallery.R;
import com.artgallery.eth.domain.ETHWallet;

import java.util.List;

public class SelectedWalletAdapter extends BaseQuickAdapter<ETHWallet, BaseViewHolder> {

    private Context context;

    public SelectedWalletAdapter(Context context, List<ETHWallet> ethWallets) {
        super(R.layout.item_selected_wallet_layout, ethWallets);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ETHWallet item) {
        helper.setText(R.id.tv_wallet_name, item.getName());
        helper.setText(R.id.tv_wallet_address, item.getAddress());
        int index = helper.getAdapterPosition() + 1;
        if (index % 3 == 1) {
            helper.setBackgroundRes(R.id.layout_wallet, R.drawable.shape_item_wallet_light_blue);
        } else if (index % 3 == 2) {
            helper.setBackgroundRes(R.id.layout_wallet, R.drawable.shape_item_wallet_yellow);
        } else if (index % 3 == 0) {
            helper.setBackgroundRes(R.id.layout_wallet, R.drawable.shape_item_wallet_deep_blue);
        }
    }
}
