package com.company.wallpaper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;


/**
 * Created by yushengyang.
 * Date: 2018/8/23.
 */

public abstract class CommonViewAdapter<T> extends BaseRecyclerAdapter<T, ViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mDatas;
    private int mLayoutId;
    private boolean canShowEmpty = false;
    public final int EMPTY = 0;
    public final int NO_EMPTY = 1;
    public int emptyLayoutId = -1;
    //固定条目数量
    private int MaxCount = -1;
    private View.OnClickListener EmptyClickListener = null;
    private String emptyText;

    public CommonViewAdapter(Context context, List<T> datas, int layoutId) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDatas = datas;
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 先inflate数据
        View itemView = mInflater.inflate(getLayoutId(i), viewGroup, false);
        // 返回ViewHolder
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (mDatas != null && mDatas.size() != 0) {
            convert(viewHolder, mDatas.get(i));
        } else {
            if (viewHolder.getView(R.id.ll_empty_refresh) != null) {
                viewHolder.getView(R.id.ll_empty_refresh).setOnClickListener(EmptyClickListener);
                if (emptyLayoutId == -1) {
                    ((TextView) viewHolder.getView(R.id.tv_empty)).setText(emptyText == null || emptyText.isEmpty() ? "这里什么也没有~" : emptyText);
                }
            }
        }
    }

    public void onEmptyListener(View.OnClickListener listener) {
        EmptyClickListener = listener;
    }

    public void setEmptyLayout(int EmptyId) {
        emptyLayoutId = EmptyId;
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param item 当前的数据
     */
    public abstract void convert(ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        if (mDatas.size() == 0 && canShowEmpty) {
            return 1;
        }
        if (MaxCount != -1) {
            return MaxCount;
        }
        return this.mDatas.size();
    }

    public void addData(T t) {
        mDatas.add(t);
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
    }

    public void addDatas(List<T> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<T> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public int getLayoutId(int type) {
        if (type == 0) {
            if (emptyLayoutId == -1) {
                return R.layout.item_empty;
            } else {
                return emptyLayoutId;
            }
        }
        return mLayoutId;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.size() == 0 && canShowEmpty) {
            return EMPTY;
        }
        return NO_EMPTY;
//        return super.getItemViewType(position);
    }

    public void setCanShowEmpty(boolean canShowEmpty) {
        this.canShowEmpty = canShowEmpty;
    }

    public int getDataSize() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
    }
    public void set(T mData, int position) {
        mDatas.set(position, mData);
        notifyDataSetChanged();
    }
    public List<T> getmDatas() {
        return mDatas;
    }

    public void setMaxCount(int maxCount) {
        MaxCount = maxCount;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public String getEmptyText() {
        return emptyText;
    }
}
