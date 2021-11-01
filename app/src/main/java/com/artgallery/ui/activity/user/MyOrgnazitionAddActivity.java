package com.artgallery.ui.activity.user;

import com.artgallery.R;
import com.artgallery.base.BaseActivity;
import com.artgallery.base.ToolBarOptions;
import com.artgallery.databinding.ActivityMyOrgnazitionBinding;

/*
 * 我的机构
 * */
public class MyOrgnazitionAddActivity extends BaseActivity<ActivityMyOrgnazitionBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_orgnazition;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.my_orgnaziton;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
    }
}