package com.artgallery.ui.activity;


import com.artgallery.R;
import com.artgallery.base.BaseActivity;
import com.artgallery.databinding.ActivityRestPswBinding;

public class ResetPswActivity extends BaseActivity<ActivityRestPswBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_rest_psw;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mDataBinding.next.setOnClickListener(view -> startActivity(ResetPswStep2Activity.class));
    }
}