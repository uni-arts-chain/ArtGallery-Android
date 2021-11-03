package com.artgallery;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.artgallery.base.BaseActivity;
import com.artgallery.base.YunApplication;
import com.artgallery.constant.AppConstant;
import com.artgallery.constant.ExtraConstant;
import com.artgallery.databinding.ActivityMainBinding;
import com.artgallery.entity.BaseResponseVo;
import com.artgallery.entity.EventBusMessageEvent;
import com.artgallery.entity.NetworkInfos;
import com.artgallery.entity.ReceiverPushBean;
import com.artgallery.entity.UserVo;
import com.artgallery.eth.domain.ETHWallet;
import com.artgallery.eth.interact.CreateWalletInteract;
import com.artgallery.eth.util.ETHWalletUtils;
import com.artgallery.net.MinerCallback;
import com.artgallery.net.RequestManager;
import com.artgallery.ui.fragment.CreatorFragment;
import com.artgallery.ui.fragment.FindFragment;
import com.artgallery.ui.fragment.HomeFragment;
import com.artgallery.ui.fragment.MineFragment;
import com.artgallery.ui.fragment.NFTMallFragment;
import com.artgallery.utils.DownLoadManager;
import com.artgallery.utils.NotificationUtil;
import com.artgallery.utils.SharedPreUtils;
import com.artgallery.utils.UserManager;
import com.artgallery.widget.PermissionDialog;
import com.artgallery.widget.UpdateDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;

import org.bouncycastle.util.encoders.Hex;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private HomeFragment homeFragment;
    private NFTMallFragment nftMallFragment;
    private MineFragment mineFragment;
    private CreatorFragment creatorFragment;
    private FindFragment findFragment;
    private boolean mIsback;
    private Fragment mCurrentFragment;
    private int mCurrentItemId;
    private List<String> mLackedPermission = new ArrayList<>();
    private BottomNavigationView mBottomNavigationView;
    public UpdateDialog mDialog;
    public UpdateDialog mForceDialog;
    public static final String JUMP_PAGE = "jump_page";
    private static final String DEFAULT_WALLET_NAME = "walletName";

    private String isFirstLoad;
    private final CreateWalletInteract createWalletInteract = new CreateWalletInteract();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (!PermissionUtils.isGranted(PermissionConstants.getPermissions(PermissionConstants.STORAGE))) {
            mLackedPermission.add(PermissionConstants.STORAGE);
        }
        if (null != mLackedPermission && !mLackedPermission.isEmpty()) {
            PermissionUtils.permission(mLackedPermission.toArray(new String[0])).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    permissionsGranted.containsAll(Arrays.asList(PermissionConstants.getPermissions(PermissionConstants.STORAGE)));
                    DownLoadManager.with().init(MainActivity.this);
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    LogUtils.eTag("mosr", "onPermissionDenied");
                }
            }).request();
        }
        DownLoadManager.with().init(MainActivity.this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        isFirstLoad = CacheDiskStaticUtils.getString(ExtraConstant.KEY_GUIDE_FLAG);
        if (isFirstLoad == null || !isFirstLoad.equals("1")) {
            String password = SharedPreUtils.getString(this, SharedPreUtils.ACCOUNT_KEY_PIN);
            createWalletInteract.create(DEFAULT_WALLET_NAME, password, password, "", "ETH").subscribe(this::jumpToWalletBackUp, this::showError);
        }
        CacheDiskStaticUtils.put(ExtraConstant.KEY_GUIDE_FLAG, "1");
        saveData();
//        loginByAddress();
        queryNetworks();
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        nftMallFragment = (NFTMallFragment) getSupportFragmentManager().findFragmentByTag("nftMallFragment");
        creatorFragment = (CreatorFragment) getSupportFragmentManager().findFragmentByTag("creatorFragment");
        findFragment = (FindFragment) getSupportFragmentManager().findFragmentByTag("findFragment");
        mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("mineFragment");
        if (null == mCurrentFragment) {
            switch (mCurrentItemId) {
                case R.id.navigation_home:
                    mCurrentFragment = homeFragment;
                    break;
                case R.id.navigation_art_sort:
                    mCurrentFragment = nftMallFragment;
                    break;
                case R.id.navigation_creator:
                    mCurrentFragment = creatorFragment;
                    break;
                case R.id.navigation_find:
                    mCurrentFragment = findFragment;
                    break;
                case R.id.navigation_mine:
                    mCurrentFragment = mineFragment;
                    break;
            }
        }


        mBottomNavigationView = findViewById(R.id.mBottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if (null == getSupportFragmentManager()) {
                return false;
            }

            if (mCurrentItemId == menuItem.getItemId()) {
                return false;
            }
            mCurrentItemId = menuItem.getItemId();
            FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (null != mCurrentFragment) {
                mFragmentTransaction.hide(mCurrentFragment);
            }
            switch (mCurrentItemId) {
                case R.id.navigation_home:
                    if (null == homeFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("homeFragment")) {
                            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
                        } else {
                            homeFragment = (HomeFragment) HomeFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, homeFragment, "homeFragment");
                        }
                    }
                    mCurrentFragment = homeFragment;
                    break;
                case R.id.navigation_art_sort:
                    if (null == nftMallFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("nftMallFragment")) {
                            nftMallFragment = (NFTMallFragment) getSupportFragmentManager().findFragmentByTag("nftMallFragment");
                        } else {
                            nftMallFragment = (NFTMallFragment) NFTMallFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, nftMallFragment, "nftMallFragment");
                        }
                    }
                    mCurrentFragment = nftMallFragment;
                    break;

                case R.id.navigation_find:
                    if (null == findFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("findFragment")) {
                            findFragment = (FindFragment) getSupportFragmentManager().findFragmentByTag("findFragment");
                        } else {
                            findFragment = (FindFragment) FindFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, findFragment, "findFragment");
                        }
                    }
                    mCurrentFragment = findFragment;
                    break;
                case R.id.navigation_creator:
                    if (null == creatorFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("creatorFragment")) {
                            creatorFragment = (CreatorFragment) getSupportFragmentManager().findFragmentByTag("creatorFragment");
                        } else {
                            creatorFragment = (CreatorFragment) CreatorFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, creatorFragment, "creatorFragment");
                        }
                    }
                    mCurrentFragment = creatorFragment;
                    break;
                case R.id.navigation_mine:
                    if (null == mineFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("mineFragment")) {
                            mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("mineFragment");
                        } else {
                            mineFragment = (MineFragment) MineFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, mineFragment, "mineFragment");
                        }
                    }
                    mCurrentFragment = mineFragment;
                    break;
            }
//                setAndroidNativeLightStatusBar(mCurrentItemId != R.id.navigation_mine, false, true);
            if (null != mCurrentFragment) {
                mFragmentTransaction.show(mCurrentFragment);
            }
            mFragmentTransaction.commitAllowingStateLoss();
            return true;
        });

        mBottomNavigationView.setSelectedItemId(0 != mCurrentItemId ? mCurrentItemId : R.id.navigation_home);
        mBottomNavigationView.setItemIconTintList(null);
    }

    public void showError(Throwable errorInfo) {
        dismissLoading();
        ToastUtils.showShort(errorInfo.toString());
    }

    public void jumpToWalletBackUp(ETHWallet wallet) {
        dismissLoading();
        ToastUtils.showShort("创建钱包成功");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void queryNetworks() {
        RequestManager.instance().queryNetworks(new MinerCallback<BaseResponseVo<List<NetworkInfos>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<NetworkInfos>>> call, Response<BaseResponseVo<List<NetworkInfos>>> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        YunApplication.setNetWorkInfo(response.body().getBody());
                        if (TextUtils.isEmpty(SharedPreUtils.getString(MainActivity.this, SharedPreUtils.KEY_RPC_URL))) {
                            initMainNet(response.body().getBody());
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<NetworkInfos>>> call, Response<BaseResponseVo<List<NetworkInfos>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void initMainNet(List<NetworkInfos> networkInfos) {
        if (networkInfos != null && networkInfos.size() > 0) {
            for (int i = 0; i < networkInfos.size(); i++) {
                if (networkInfos.get(i).getTitle().equals("主网络")) {
                    List<NetworkInfos.ChainNetWork> mainChainWorks = networkInfos.get(i).getChain_networks();
                    if (mainChainWorks != null && mainChainWorks.size() > 0) {
                        if (!TextUtils.isEmpty(mainChainWorks.get(0).getName())) {
                            YunApplication.NETWORK_RPC_URL = mainChainWorks.get(0).getRpc_url() + YunApplication.NETWORK_API_KEY;
                            YunApplication.NETWORK_CHAIN_ID = mainChainWorks.get(0).getChain_id();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstant.HOME_CURRENT_ITEM_ID, mCurrentItemId);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessageEvent mEventBusMessageEvent) {
        if (null != mEventBusMessageEvent && !TextUtils.isEmpty(mEventBusMessageEvent.getmMessage())) {
            if (TextUtils.equals(ExtraConstant.EVENT_SKIP_HOME, mEventBusMessageEvent.getmMessage())) {
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
            } else if (TextUtils.equals(ExtraConstant.EVENT_PUSH, mEventBusMessageEvent.getmMessage())) {
                pushNotifiction(mEventBusMessageEvent.getmValue().toString());
            } else if (TextUtils.equals(ExtraConstant.EVENT_MORE_PICTUR_SELECT, mEventBusMessageEvent.getmMessage())) {
                mBottomNavigationView.setSelectedItemId(R.id.navigation_art_sort);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsback) {
                finish();
            } else {
                //监听全屏视频时返回键
                mIsback = true;
                ToastUtils.showShort(R.string.double_click_exit);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsback = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intentSkip(intent);
    }

    public void intentSkip(Intent mIntent) {
        if (null == mIntent) {
            return;
        }
        if (null == mIntent.getStringExtra(JUMP_PAGE)) {
            return;
        }
        String jumpPage = mIntent.getStringExtra(JUMP_PAGE);
        switch (jumpPage) {
            case "0":
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
                break;
            case "1":
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_art_sort);
                }
                break;
            case "2":
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_creator);
                }
                break;
        }
    }

    boolean hasJump = false;

    @Override
    protected void onResume() {
        super.onResume();
        UserManager.isLogin(false);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void needPermission() {

    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void permissionDenied() {
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void neverAsk() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE};
        PermissionDialog permissionDialog
                = new PermissionDialog(MainActivity.this, permission);
        permissionDialog.show();
    }

    private void saveData() {

    }

    ReceiverPushBean receiverPushBean;

    private void pushNotifiction(String json) {
        LogUtils.e("json = " + json);
        hasJump = false;
        Gson gson = new Gson();
        receiverPushBean = gson.fromJson(json, ReceiverPushBean.class);
        NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());
        notificationUtil.sendNotification(receiverPushBean);
    }

}