package com.artgallery.ui;

import com.artgallery.R;
import com.artgallery.base.BaseActivity;
import com.artgallery.base.ToolBarOptions;
import com.artgallery.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.about_us;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

    }
}