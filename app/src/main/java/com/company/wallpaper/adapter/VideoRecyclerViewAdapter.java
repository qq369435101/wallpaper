package com.company.wallpaper.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.bean.VideoBean;
import com.company.wallpaper.utils.GlideCacheUtil;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.VideoView;

import java.util.ArrayList;
import java.util.List;

public class VideoRecyclerViewAdapter
        extends RecyclerView.Adapter<VideoRecyclerViewAdapter.VideoHolder> {

    private List<PaperTypeBean> videos = new ArrayList<>();

//    private ProgressManagerImpl mProgressManager;

//    private PlayerFactory mPlayerFactory = IjkPlayerFactory.create();

    public VideoRecyclerViewAdapter(List<PaperTypeBean> videos) {
        this.videos.addAll(videos);
    }

    @Override
    @NonNull
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_auto_play, parent, false);
        return new VideoHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

        PaperTypeBean videoBean = videos.get(position);

        ImageView thumb = holder.mController.getThumb();

        GlideCacheUtil.LoadImage(thumb.getContext(), thumb, videoBean.getPaperUrl());
        holder.mController.setEnableOrientation(true);
        holder.mController.setTitle(videoBean.getVideoName());

        holder.mVideoView.setUrl(videoBean.getVideoUrl());
        holder.mVideoView.setVideoController(holder.mController);

        holder.mTitle.setText(videoBean.getTypeName());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void addData(List<PaperTypeBean> videoList) {
        int size = videos.size();
        videos.addAll(videoList);
        //使用此方法添加数据，使用notifyDataSetChanged会导致正在播放的视频中断
        notifyItemRangeChanged(size, videos.size());
    }

    public void clear() {
        videos.clear();
        notifyDataSetChanged();
    }
    public class VideoHolder extends RecyclerView.ViewHolder {

        private VideoView mVideoView;
        private StandardVideoController mController;
        private TextView mTitle;

        VideoHolder(View itemView) {
            super(itemView);
            mVideoView = itemView.findViewById(R.id.video_player);
            int widthPixels = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
            mVideoView.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, widthPixels * 9 / 16 + 1));
            mController = new StandardVideoController(itemView.getContext());
            mTitle = itemView.findViewById(R.id.tv_title);
            mController.findViewById(R.id.fullscreen).setVisibility(View.GONE);
        }
    }
}