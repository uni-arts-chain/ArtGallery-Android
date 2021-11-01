package com.artgallery.ui.fragment;

import com.artgallery.R;
import com.artgallery.adapter.BlindBoxAdapter;
import com.artgallery.base.BaseFragment;

public class ShoppingCartFragment extends BaseFragment {

    BlindBoxAdapter boxAdapter;

    public static BaseFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_shopping_cart;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}