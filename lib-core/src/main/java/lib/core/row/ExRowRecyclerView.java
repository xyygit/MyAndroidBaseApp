/*
 * Copyright (c) 2014 by EagleXad
 * Team: EagleXad
 * Create: 2014-08-29
 */
package lib.core.row;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author Aloneter
 * @ClassName: ExRowRecyclerView
 * @Description: Row 模式 抽象类
 * @date 2015-12-15
 * @Version 6.0
 */
public abstract class ExRowRecyclerView extends ExRowBaseView {

    /**
     * Method_获取 ViewHolder 对象
     *
     * @param parent 父级容器
     * @return ViewHolder 对象
     */
    public abstract RecyclerView.ViewHolder getViewHolder(ViewGroup parent);

    /**
     * Method_绑定数据
     *
     * @param holder   ViewHolder 对象
     * @param position 位置
     */
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

}
