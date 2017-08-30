/*
 * Copyright (c) 2014 by EagleXad
 * Team: EagleXad
 * Create: 2014-08-29
 */
package lib.core.row;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Aloneter
 * @ClassName: ExRowListViewAdapter
 * @Description: Row 模式 Adapter 抽象类
 * @date 2015-12-11
 * @Version 6.0
 */
public abstract class ExRowListViewAdapter extends BaseAdapter {

    protected Context mContext; // 上下文
    protected ExRowRepo mExRowRepo; // Row 管理对象

    /**
     * Construction_构造函数
     */
    public ExRowListViewAdapter(Context context) {

        mContext = context;
        mExRowRepo = ExRowRepo.newInstance();
    }

    @Override
    public int getCount() {

        return mExRowRepo.getCount();
    }

    @Override
    public int getViewTypeCount() {

        return getRowViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {

        ExRowListView row = (ExRowListView) mExRowRepo.getRow(position);

        return (row == null) ? -1 : row.getViewType();
    }

    @Override
    public Object getItem(int position) {

        return mExRowRepo.getRow(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExRowListView row = (ExRowListView) mExRowRepo.getRow(position);

        return (row == null) ? null : row.getView(position, convertView, parent);
    }

    /**
     * Method_添加 Row
     *
     * @param id    编号 -1 情况下追加最后位置
     * @param index 位置
     * @param row   Row
     */
    protected int addRow(String id, int index, ExRowListView row) {

        if (index == -1) {
            return mExRowRepo.addLast(id, row);
        }

        return mExRowRepo.add(id, index, row);
    }


    /**
     * Method_添加 Row 至最后一个位置
     *
     * @param id  编号
     * @param row Row
     * @return 添加后位置
     */
    protected int addLastRow(String id, ExRowListView row) {

        return mExRowRepo.addLast(id, row);
    }

    /**
     * Method_移除 Row
     *
     * @param id 编号
     * @return 删除后位置
     */
    protected int removeRow(String id) {

        return mExRowRepo.remove(id);
    }

    /**
     * Method_获取 Row 类型数目
     *
     * @return 数目
     */
    protected abstract int getRowViewTypeCount();

}
