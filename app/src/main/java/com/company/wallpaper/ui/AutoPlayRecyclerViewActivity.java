package com.company.wallpaper.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.company.wallpaper.AppConfig;
import com.company.wallpaper.R;
import com.company.wallpaper.adapter.VideoRecyclerViewAdapter;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.dueeeke.videoplayer.player.VideoView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class AutoPlayRecyclerViewActivity extends BaseListActivity {
    private HomeActivityViewModel viewModel;
    private int pageNum = 1;
    private VideoRecyclerViewAdapter videoRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;

    public static void toRecyclerViewVideoActivity(Context context, int typeId, String typeName) {
        Intent intent = new Intent(context, AutoPlayRecyclerViewActivity.class);
        intent.putExtra("typeId", typeId);
        intent.putExtra("typeName", typeName);
        context.startActivity(intent);
    }

    public void getData() {
        getLifecycle().addObserver(new LifecycleObserver() {
        });
        viewModel.suncategory(getIntent().getIntExtra("typeId", -1), getIntent().getStringExtra("typeName"), pageNum++, AppConfig.DefaultPageSize, new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                if (paperTypeBeans.size() < AppConfig.DefaultPageSize) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                refreshLayout.finishRefresh(true);
                videoRecyclerViewAdapter.addData(paperTypeBeans);
                if (videoRecyclerViewAdapter.getItemCount() < AppConfig.DefaultPageSize && paperTypeBeans.size() > 0) {
                    recyclerView.post(() -> {
                        //自动播放第一个
                        View view = recyclerView.getChildAt(0);
                        VideoView videoView = view.findViewById(R.id.video_player);
                        videoView.start();
                    });
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                refreshLayout.finishLoadMore(false);
                refreshLayout.finishRefresh(false);
                ToastUtils.showToast("加载失败，请稍后再试");
            }
        });
    }

    public void refresh() {
        if (videoRecyclerViewAdapter != null)
            videoRecyclerViewAdapter.clear();
        pageNum = 1;
        refreshLayout.setNoMoreData(false);
        getData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_auto_play_recycler_view;
    }

    @Override
    protected int getTitleResId() {
        return R.string.app_name;
    }

    @Override
    protected void initView() {
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        recyclerView = findViewById(R.id.rv);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshLayout = findViewById(R.id.refreshLayout);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(videoRecyclerViewAdapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                VideoView videoView = view.findViewById(R.id.video_player);
                if (videoView != null && !videoView.isFullScreen()) {
//                    Log.d("@@@@@@", "onChildViewDetachedFromWindow: called");
//                    int tag = (int) videoView.getTag();
//                    Log.d("@@@@@@", "onChildViewDetachedFromWindow: position: " + tag);
                    videoView.release();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem, visibleCount;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case SCROLL_STATE_IDLE: //滚动停止
                        autoPlayVideo(recyclerView);
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;//记录可视区域item个数
            }

            private void autoPlayVideo(RecyclerView view) {
                //循环遍历可视区域videoview,如果完全可见就开始播放
                for (int i = 0; i < visibleCount; i++) {
                    if (view == null || view.getChildAt(i) == null) continue;
                    VideoView videoView = view.getChildAt(i).findViewById(R.id.video_player);
                    if (videoView != null) {
                        Rect rect = new Rect();
                        videoView.getLocalVisibleRect(rect);
                        int videoHeight = videoView.getHeight();
                        if (rect.top == 0 && rect.bottom == videoHeight) {
                            videoView.start();
                            return;
                        }
                    }
                }
            }
        });
        refreshLayout.setOnRefreshListener(refreshLayout -> refresh());
        refreshLayout.setOnLoadMoreListener(refreshLayout -> getData());
        getData();

    }
}