package com.company.wallpaper.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.ui.ImageShowActivity;
import com.company.wallpaper.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-19.
 */

public class GridTwoAdapter extends CommonViewAdapter<PaperTypeBean> {
    private final int Ad_Id = -1;
    boolean isList = false;
    private List<PaperTypeBean> checkList = new ArrayList<>();

    public void setList(boolean list) {
        isList = list;
    }

    public void clearSelected() {
        checkList.clear();
        if (listener != null) {
            listener.onListChange(checkList.size());
        }
        notifyDataSetChanged();
    }

    public List<PaperTypeBean> getCheckList() {
        return checkList;
    }

    public String getPaperId() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i).getPaperId() != 0) {
                buffer.append(checkList.get(i).getPaperId());
                if (i != checkList.size() - 1) {
                    buffer.append(',');
                }
            }
        }
        return buffer.toString();
    }
    public String getVideoId() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i).getVideoId() != 0) {
                buffer.append(checkList.get(i).getVideoId());
                if (i != checkList.size() - 1) {
                    buffer.append(',');
                }
            }
        }
        return buffer.toString();
    }
    int typeId;
    private String typeName = "";
    private List<View> ads = new ArrayList<>();
    private boolean isSelected = false;

    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyDataSetChanged();
    }

    public void addAds(View view) {
        ads.add(view);
    }

    public List<View> getAds() {
        return ads;
    }

    public void setAds(List<View> ads) {
        this.ads = ads;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public GridTwoAdapter(Context context, List<PaperTypeBean> datas) {
        super(context, datas, R.layout.item_grid);
        setCanShowEmpty(false);
    }


    @Override
    public void convert(ViewHolder holder, PaperTypeBean item) {
        if (item.isAd()) {
            if (ads.size() == 0) {
                return;
            }
            if (holder.getView(R.id.frame_ad) != null && ((FrameLayout) holder.getView(R.id.frame_ad)).getChildCount() == 0) {
                holder.getView(R.id.frame_ad).setVisibility(View.VISIBLE);
                ((FrameLayout) holder.getView(R.id.frame_ad)).addView(ads.remove(0));
            }
            return;
        }
        CheckBox box = holder.getView(R.id.cb);
        box.setOnCheckedChangeListener(null);
        box.setChecked(false);
        if (isSelected) {
            holder.getView(R.id.cb).setVisibility(View.VISIBLE);
            for (PaperTypeBean paperTypeBean : checkList) {
                if (paperTypeBean.getVideoId() == item.getVideoId() && paperTypeBean.getPaperId() == item.getPaperId()) {
                    ((CheckBox) holder.getView(R.id.cb)).setChecked(true);
                    break;
                }
            }

        } else {
            holder.getView(R.id.cb).setVisibility(View.GONE);
        }
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv_main), item.getPaperUrl());
        ((CheckBox) holder.getView(R.id.cb)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                PaperTypeBean bean = new PaperTypeBean();
                bean.setPaperId(item.getPaperId());
                bean.setVideoId(item.getVideoId());
                checkList.add(bean);
            } else {
                for (PaperTypeBean bean : checkList) {
                    if (bean.getVideoId() == item.getVideoId() && bean.getPaperId() == bean.getPaperId()) {
                        checkList.remove(bean);
                        break;
                    }
                }
            }
            if (listener != null) {
                listener.onListChange(checkList.size());
            }
        });
        holder.getView(R.id.cl_item).setOnClickListener(v -> {
            if (!isSelected) {
                if (typeName.equals("我的收藏")) {
                    ImageShowActivity.toImageViewActivity(mContext, item.getPaperId(), -1, 0);
                } else if (typeName.equals("我的足迹")) {
                    ImageShowActivity.toImageViewActivity(mContext, item.getPaperId(), -4, 0);
                } else if (typeName.equals("我的分享")) {
                    ImageShowActivity.toImageViewActivity(mContext, item.getPaperId(), -3, 0);
                } else if (typeName.equals("我的下载")) {
                    ImageShowActivity.toImageViewActivity(mContext, item.getPaperId(), -2, 0);
                } else {
                    ImageShowActivity.toImageViewActivity(mContext, item.getPaperId(), "随机", typeName, 0, typeId, isList);
                }
            } else {
                ((CheckBox) holder.getView(R.id.cb)).setChecked(!((CheckBox) holder.getView(R.id.cb)).isChecked());
            }
        });
    }

    @Override
    public int getLayoutId(int type) {
        if (type == Ad_Id) {
            return R.layout.item_ad;
        } else {
            return super.getLayoutId(type);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == NO_EMPTY) {
            if (mDatas.get(position).isAd()) {
                return Ad_Id;
            }
            return NO_EMPTY;
        } else {
            return EMPTY;
        }
    }

    public void selectAll() {
        for (PaperTypeBean mData : mDatas) {
            boolean isContain = false;
            for (PaperTypeBean bean : checkList) {
                if (bean.getPaperId() == mData.getPaperId() && bean.getVideoId() == mData.getVideoId()) {
                    isContain = true;
                    break ;
                }
            }
            if (!isContain) {
                PaperTypeBean bean = new PaperTypeBean();
                bean.setPaperId(mData.getPaperId());
                bean.setVideoId(mData.getVideoId());
                checkList.add(bean);
            }
        }
        if (listener != null) {
            listener.onListChange(checkList.size());
        }
        notifyDataSetChanged();
    }

    dealAdapterListener listener;

    public void setListener(dealAdapterListener listener) {
        this.listener = listener;
    }

    public interface dealAdapterListener {
        void onListChange(int size);

    }
}
