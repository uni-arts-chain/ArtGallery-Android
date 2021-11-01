package com.artgallery.ui.activity;

import android.text.Html;

import com.artgallery.R;
import com.artgallery.base.BaseActivity;
import com.artgallery.base.ToolBarOptions;
import com.artgallery.databinding.ActivityNoticeInfoBinding;
import com.artgallery.entity.AnnouncementVo;

public class NoticeInfoActivity extends BaseActivity<ActivityNoticeInfoBinding> {
    AnnouncementVo announcementVo;
    public static final String NOTIC = "notic";

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_info;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        announcementVo = (AnnouncementVo) getIntent().getSerializableExtra(NOTIC);

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        String msg = announcementVo == null ? "公告" : announcementVo.getTitle();
//        mToolBarOptions.titleId = R.string.message;
        mToolBarOptions.titleString = msg;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        if (announcementVo != null) {
            mDataBinding.content.setText(Html.fromHtml(announcementVo.getContent()));
        }
    }
}