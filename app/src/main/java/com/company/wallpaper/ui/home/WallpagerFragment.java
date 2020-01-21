package com.company.wallpaper.ui.home;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.company.wallpaper.R;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.bean.TabEntity;
import com.company.wallpaper.databinding.FragmentWallpagerBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WallpagerFragment extends BaseFragment<FragmentWallpagerBinding> {
    private ArrayList<WallpagerChildFragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private HomeActivityViewModel viewModel;

    public WallpagerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        getData();
    }

    private void getData() {
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        viewModel.videotype(new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                for (int i = 0; i < paperTypeBeans.size(); i++) {
                    mTabEntities.add(new TabEntity(paperTypeBeans.get(i).getTypeName()));
                    mFragments.add(new WallpagerChildFragment().setTypeId(paperTypeBeans.get(i).getTypeId()));
                    initData();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }

    private void initData() {
        bindingView.tablayout.setTabData(mTabEntities);
        bindingView.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        bindingView.viewPager.setOffscreenPageLimit(5);
        bindingView.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                bindingView.tablayout.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        bindingView.tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                bindingView.viewPager.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    @Override
    public int setContent() {
        return R.layout.fragment_wallpager;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
