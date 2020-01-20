package com.company.wallpaper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.company.wallpaper.R;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.databinding.ActivityGuide2Binding;
import com.company.wallpaper.ui.home.HomeActivity;
import com.company.wallpaper.utils.Utils;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;


public class GuideActivity extends MVVMActivity<ActivityGuide2Binding, HomeActivityViewModel> {
    int[] imgs = new int[]{R.drawable.img_guide_1, R.drawable.img_guide_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide2);
        Utils.setStatusBar(this);
        Utils.setStatusTextColor(true, this);
        showContentView();
        bindingView.vpGuide.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View inflate = getLayoutInflater().inflate(R.layout.layout_guide, null, false);
                container.addView(inflate);
                ((ImageView) inflate.findViewById(R.id.iv)).setImageResource(imgs[position]);

                if (position == imgs.length - 1) {
                    inflate.findViewById(R.id.tv_to_home).setVisibility(View.VISIBLE);
                    inflate.findViewById(R.id.tv_to_home).setOnClickListener(v -> startActivity(new Intent(GuideActivity.this, HomeActivity.class)));
                }

                return inflate;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
//                super.destroyItem(container, position, object);
            }
        });
    }
    @Override
    protected boolean isChangeStateBar() {
        return false;
    }

}
