package com.artgallery.ui.activity;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.utils.TextUtils;
import com.artgallery.MainActivity;
import com.artgallery.R;
import com.artgallery.base.BaseActivity;
import com.artgallery.databinding.ActivityGuideBinding;
import com.artgallery.utils.SharedPreUtils;
import com.artgallery.widget.GuideView;

import jnr.ffi.annotations.In;

public class GuideActivity extends BaseActivity<ActivityGuideBinding> {

    private static final int CREATE_NEW_ACCOUNT = 0;

    private final int[] mPageImages = {
            R.mipmap.guide1,
            R.mipmap.guide2,
            R.mipmap.img_guide3,
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mDataBinding.guideView.setData(mPageImages, null, R.layout.guide_enter_view_layout, new GuideView.GuideCallBack() {
            @Override
            public void slidingPosition(int position) {

            }

            @Override
            public void slidingEnd() {

            }

            @Override
            public void onEndClickListener() {

            }

            @Override
            public void onEnterClickListener() {
                Intent intent = new Intent(GuideActivity.this, PinCodeKtActivity.class);
                intent.putExtra("SET_PASSWORD", true);
                startActivityForResult(intent, CREATE_NEW_ACCOUNT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_NEW_ACCOUNT) {
            if (!TextUtils.isEmpty(SharedPreUtils.getString(this, SharedPreUtils.ACCOUNT_KEY_PIN))) {
                startActivity(MainActivity.class);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
