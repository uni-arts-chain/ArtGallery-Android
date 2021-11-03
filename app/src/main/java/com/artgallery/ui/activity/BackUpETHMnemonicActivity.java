package com.artgallery.ui.activity;

import android.os.Bundle;

import com.artgallery.R;
import com.artgallery.adapter.BackUpMnemonicAdapter;
import com.artgallery.base.BaseActivity;
import com.artgallery.base.ToolBarOptions;
import com.artgallery.databinding.ActivityBackupEthMnemonicLayoutBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;

public class BackUpETHMnemonicActivity extends BaseActivity<ActivityBackupEthMnemonicLayoutBinding> {

    private String mnemonic;
    private ArrayList<String> mnemonics;
    private BackUpMnemonicAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_backup_eth_mnemonic_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "备份助记词";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        EventBus.getDefault().register(this);

        if (getIntent() != null) {
            mnemonic = getIntent().getStringExtra("wallet_mnemonic");
        }
        mnemonics = new ArrayList<>(Arrays.asList(mnemonic.split(" ")));
        mAdapter = new BackUpMnemonicAdapter(mnemonics);
        mDataBinding.rvMnemonic.setAdapter(mAdapter);
        mDataBinding.btnNext.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("list", mnemonics);
//            startActivity(ConfirmMnemonicActivity.class, bundle);
        });
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
//        if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_BACKUP_SUCCESS)) {
//            finish();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
