package com.artgallery.ui.activity;


import android.text.Html;

import com.artgallery.R;
import com.artgallery.base.BaseActivity;
import com.artgallery.base.ToolBarOptions;
import com.artgallery.databinding.ActivityUserAgreementBinding;
import com.artgallery.entity.BaseResponseVo;
import com.artgallery.entity.UserAggrementVo;
import com.artgallery.net.MinerCallback;
import com.artgallery.net.RequestManager;

import retrofit2.Call;
import retrofit2.Response;

public class UserAgreementActivity extends BaseActivity<ActivityUserAgreementBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_agreement;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.user_agreement;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContent();
    }

    private void getContent() {
        RequestManager.instance().getAgreement(new MinerCallback<BaseResponseVo<UserAggrementVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserAggrementVo>> call, Response<BaseResponseVo<UserAggrementVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null)
                        mDataBinding.content.setText(Html.fromHtml(response.body().getBody().getUser_agreement()));
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserAggrementVo>> call, Response<BaseResponseVo<UserAggrementVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}