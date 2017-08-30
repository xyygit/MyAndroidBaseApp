/*
 * Copyright (c) 2014 by EagleXad
 * Team: EagleXad
 * Create: 2014-08-29
 */
package lib.core.row;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author Aloneter
 * @ClassName: ExRowRecyclerViewAdapter
 * @Description: Row 模式 Adapter 抽象类
 * @date 2015-12-15
 * @Version 6.0
 */
public abstract class ExRowRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext; // 上下文
    protected ExRowRepo mExRowRepo; // Row 管理对象
    protected boolean mScrolling;

    /**
     * Construction_构造函数
     */
    public ExRowRecyclerViewAdapter(Context context) {

        mContext = context;
        mExRowRepo = ExRowRepo.newInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return mExRowRepo.getViewHolder(viewType).getViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (!holder.isRecyclable() || mScrolling) {
            return;
        }

        ExRowRecyclerView row = (ExRowRecyclerView) mExRowRepo.getRow(position);

        if (row != null) {
            row.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {

        return mExRowRepo.getCount();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public int getItemViewType(int position) {

        ExRowRecyclerView row = (ExRowRecyclerView) mExRowRepo.getRow(position);

        return (row == null) ? -1 : row.getViewType();
    }

    public void setScrolling(boolean scrolling) {

        mScrolling = scrolling;
    }

    public boolean isScrolling() {

        return mScrolling;
    }

}
