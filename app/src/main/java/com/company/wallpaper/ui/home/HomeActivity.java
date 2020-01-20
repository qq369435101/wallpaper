package com.company.wallpaper.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.company.wallpaper.R;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.databinding.ActivityHomeBinding;
import com.company.wallpaper.databinding.DialogPrivacyBinding;
import com.company.wallpaper.ui.PaperListActivity;
import com.company.wallpaper.ui.WebViewActivity;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.view.CustomDialog;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ysy.commonlib.utils.Utils;

import java.util.ArrayList;

public class HomeActivity extends MVVMActivity<ActivityHomeBinding, HomeActivityViewModel> {
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setMTATag("MainActivity");
        if (ShareUtils.getIBoolean("DialogShow", true)) {
            CustomDialog<DialogPrivacyBinding> customDialog = new CustomDialog<>(this, R.layout.dialog_privacy, -1);
            customDialog.getBindView().tvPositive.setOnClickListener(v -> {
                ShareUtils.putBoolean("DialogShow", false);
                customDialog.dismiss();
            });
            customDialog.getBindView().tv1.setOnClickListener(v -> WebViewActivity.toWebView(HomeActivity.this, "隐私协议", "http://www.rioses.top/?act=dyPrivacy"));
            customDialog.getBindView().tv2.setOnClickListener(v -> WebViewActivity.toWebView(HomeActivity.this, "注册协议", "http://www.rioses.top/?act=zyPrivacy"));
            customDialog.show();
        }

    }

    public void setMTATag(String tag) {
    }

    private void init() {
        initWindow(this);
        initContentFragment();
        initTabView();
        initViewPager();
        showContentView();
        changeBackMode();

    }

    private void initViewPager() {
        fragments.add(new HomePageFragment());
        fragments.add(new SearchFragment());
        fragments.add(new WallpagerFragment());
        bindingView.vpHome.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        bindingView.vpHome.setOffscreenPageLimit(3);
        SmartTabLayout layout = bindingView.titleBar.getCenterCustomView().findViewById(R.id.tab_list);
        layout.setViewPager(bindingView.vpHome);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        private String[] tabs = {"商品", "详情", "评价"};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
        @Override
        public int getCount() {
            return fragments.size();
        }



        @Override
        public BaseFragment getItem(int position) {
            return fragments.get(position);
        }

    }

    @Override
    protected boolean isSwipeBackEnable() {
        return false;
    }
    private void initTab(){

    }





    private void initTabView() {
//        BottomNavigationView navigationView = bindingView.navigationHome;
//        navigationView.setItemHorizontalTranslationEnabled(false);
//        navigationView.setItemIconTintList(null);
//        navigationView.setOnNavigationItemSelectedListener(menuItem -> {
//            if (menuItem.getItemId() == R.id.tab4) {
//                Utils.setStatusTextColor(false, this);
//            } else {
//                Utils.setStatusTextColor(true, this);
//            }
//            switch (menuItem.getItemId()) {
//                case R.id.tab1: {
//                    showFragment(0);
//                    return true;
//                }
//                case R.id.tab2: {
//                    showFragment(1);
//                    return true;
//                }
//                case R.id.tab3: {
//                    showFragment(2);
//                    return true;
//                }
//                case R.id.tab4: {
//                    showFragment(3);
//                    return true;
//                }
//            }
//            return false;
//        });
    }

    private void showFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (position == i) {
                transaction.show(fragments.get(i));
            } else {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commitAllowingStateLoss();
    }


    private void initContentFragment() {
//        if (fragments.size() == 0) {
//            fragments.add(new HomePageFragment());
//            fragments.add(new SearchFragment());
//            fragments.add(new WallpagerFragment());
//            fragments.add(new UserInfoFragment());
//            transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.fl_home, fragments.get(0));
//            transaction.add(R.id.fl_home, fragments.get(1));
//            transaction.add(R.id.fl_home, fragments.get(2));
//            transaction.add(R.id.fl_home, fragments.get(3));
//            transaction.hide(fragments.get(1));
//            transaction.hide(fragments.get(2));
//            transaction.hide(fragments.get(3));
//            transaction.commitAllowingStateLoss();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragments.get(3) != null) {
            fragments.get(3).onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
