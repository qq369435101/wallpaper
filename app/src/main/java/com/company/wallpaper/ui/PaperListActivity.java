package com.company.wallpaper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.company.wallpaper.R;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.bean.TabEntity;
import com.company.wallpaper.databinding.ActivityPaperListBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ysy.commonlib.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class PaperListActivity extends MVVMActivity<ActivityPaperListBinding, HomeActivityViewModel> {
    private ArrayList<PaperListChildFragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_list);
        showContentView();
        bindingView.titleBar.getCenterTextView().setText(getIntent().getStringExtra("title"));
        getTab();
        changeBackMode();
    }

    private void initData() {
        bindingView.viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        bindingView.viewPager.setOffscreenPageLimit(3);
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

    /**
     * @param typeId
     * @param title
     * @param type    0,1
     * @param context
     */
    public static void toPagerListActivity(int typeId, String title, int type, Context context) {
        Intent intent = new Intent(context, PaperListActivity.class);
        intent.putExtra("typeId", typeId);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        context.startActivity(intent);

    }

    private void getTab() {
        if (getIntent().getIntExtra("type", -1) == 0) {
            mViewModel.typecategory(getIntent().getIntExtra("typeId", -1), new onResponseListener<List<PaperTypeBean>>() {
                @Override
                public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
//                    ToastUtils.showToast(paperTypeBeans.size()+"");
                    bindingView.tablayout.setTabSpaceEqual(paperTypeBeans.size() < 5);
                    String[] strings = new String[paperTypeBeans.size()];
                    for (int i = 0; i < paperTypeBeans.size(); i++) {
                        mTitles.add(paperTypeBeans.get(i).getTypeName());
                        mTabEntities.add(new TabEntity(paperTypeBeans.get(i).getTypeName()));
                        mFragments.add(new PaperListChildFragment().setData(paperTypeBeans.get(i).getTypeId(), paperTypeBeans.get(i).getTypeName()));
                        strings[i] = paperTypeBeans.get(i).getTypeName();
                    }
                    initData();
                    bindingView.tablayout.setViewPager(bindingView.viewPager, strings);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    DialogUtils.showFailedDialog(PaperListActivity.this, bindingView.viewPager);
                }
            });
        } else {
            mViewModel.homecategory(getIntent().getIntExtra("typeId", -1), new onResponseListener<List<PaperTypeBean>>() {
                @Override
                public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                    bindingView.tablayout.setTabSpaceEqual(paperTypeBeans.size() < 5);
                    String[] strings = new String[paperTypeBeans.size()];
                    for (int i = 0; i < paperTypeBeans.size(); i++) {
                        mTitles.add(paperTypeBeans.get(i).getTypeName());
                        mTabEntities.add(new TabEntity(paperTypeBeans.get(i).getTypeName()));
                        mFragments.add(new PaperListChildFragment().setData(paperTypeBeans.get(i).getTypeId(), paperTypeBeans.get(i).getTypeName()));
                        strings[i] = paperTypeBeans.get(i).getTypeName();
                    }
                    initData();
                    bindingView.tablayout.setViewPager(bindingView.viewPager, strings);

                }

                @Override
                public void onFailed(Throwable throwable) {
                    Log.e("okhttp", throwable.getMessage());

                }
            });
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
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

    @Override
    protected boolean isSwipeBackEnable() {
        return false;
    }
}
