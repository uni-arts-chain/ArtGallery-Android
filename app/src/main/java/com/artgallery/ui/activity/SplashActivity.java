package com.artgallery.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.artgallery.MainActivity;
import com.artgallery.R;
import com.artgallery.base.BaseActivity;
import com.artgallery.constant.ExtraConstant;
import com.artgallery.databinding.ActivitySplashBinding;
import com.artgallery.utils.LoadOneTimeGifUtils;
import com.blankj.utilcode.util.CacheDiskStaticUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> implements LoadOneTimeGifUtils.GifListener {

    private String guide_flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.SplashTheme);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        guide_flag = CacheDiskStaticUtils.getString(ExtraConstant.KEY_GUIDE_FLAG);
        LoadOneTimeGifUtils.loadOneTimeGif(this, R.drawable.app_launch, mDataBinding.imgSplash, this);

    }

    private void skipTomMain() {
        overridePendingTransition(R.anim.boxing_fade_in, R.anim.boxing_fade_out);
        startActivity(TextUtils.equals("1", guide_flag) ? MainActivity.class : GuideActivity.class);
        SplashActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void gifPlayComplete() {
        skipTomMain();
    }
}

