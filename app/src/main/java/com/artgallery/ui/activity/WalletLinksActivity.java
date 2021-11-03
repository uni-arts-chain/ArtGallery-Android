package com.artgallery.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.artgallery.R;
import com.artgallery.adapter.WalletLinksAdapter;
import com.artgallery.base.BaseActivity;
import com.artgallery.base.ToolBarOptions;
import com.artgallery.databinding.ActivityWalletLinksLayoutBinding;
import com.artgallery.entity.WalletLinkBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class WalletLinksActivity extends BaseActivity<ActivityWalletLinksLayoutBinding> {

    private WalletLinkBean mUartLink = new WalletLinkBean("UART", "UniArt", R.mipmap.icon_uart);
    private WalletLinkBean mETHLink = new WalletLinkBean("ETH", "Ethereum", R.mipmap.icon_eth);
    private List<WalletLinkBean> mList = new ArrayList<>();
    private WalletLinksAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_links_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "选择链类型";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        EventBus.getDefault().register(this);

        mList.add(mUartLink);
        mList.add(mETHLink);

        mAdapter = new WalletLinksAdapter(mList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mDataBinding.rvWalletLink.setLayoutManager(layoutManager);
        mDataBinding.rvWalletLink.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == 0) {
//                startActivity(AcountActivity.class);
            } else if (position == 1) {
                startActivity(SelectedWalletsActivity.class);
            }
        });
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
//        if (eventBusMessageEvent != null) {
//            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_IMPORT_ETH_SUCCESS)) {
//                finish();
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
