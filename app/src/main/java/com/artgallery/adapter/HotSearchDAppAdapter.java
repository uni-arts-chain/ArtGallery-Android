package com.artgallery.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.artgallery.R;
import com.artgallery.entity.DAppSearchBean;

import java.util.List;

public class HotSearchDAppAdapter extends BaseQuickAdapter<DAppSearchBean, BaseViewHolder> {

    private final Context mContext;

    public HotSearchDAppAdapter(Context context,List<DAppSearchBean> data) {
        super(R.layout.item_recent_dapp_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppSearchBean item) {
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.tv_app_name, item.getTitle());
        }
        if(item.getLogo() != null){
            if(!TextUtils.isEmpty(item.getLogo().getUrl())){
                Glide.with(mContext).load(item.getLogo().getUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView) helper.getView(R.id.img_app_icon));
            }
        }
    }
}
