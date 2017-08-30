/*
 * Copyright (c) 2014 by EagleXad
 * Team: EagleXad
 * Create: 2014-08-29
 */

package lib.core.row;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Aloneter
 * @ClassName: ExRowRepo
 * @Description: Row 模式 管理方案
 */
public final class ExRowRepo {

    private final List<ExRowBaseView> mExRows = new ArrayList<>(); // Row 管理集合
    private final ConcurrentHashMap<String, List<ExRowBaseView>> mRowsMaps = new ConcurrentHashMap<>(); // Row 键值集合
    private final ConcurrentHashMap<Integer, ExRowRecyclerView> mRowsViewHolderMaps = new ConcurrentHashMap<>(); // RowViewHolder 键值集合

    /**
     * Method_创建新实例
     *
     * @return 实例对象
     */
    public static ExRowRepo newInstance() {

        return new ExRowRepo();
    }

    /**
     * Construction_构造函数
     */
    private ExRowRepo() {

    }

    /**
     * Method_获取 Row 集合长度
     *
     * @return 长度
     */
    public int getCount() {

        return mExRows.size();
    }

    /**
     * Method_判断 Row 集合是否为空
     *
     * @return 结果
     */
    public boolean isEmpty() {

        return mExRows.isEmpty();
    }

    /**
     * Method_通过位置获取 Row
     *
     * @param index 位置
     * @return Row
     */
    public ExRowBaseView getRow(int index) {

        return 0 <= index && index < getCount() ? mExRows.get(index) : null;
    }

    /**
     * Method_获取最后一个 Row
     *
     * @return Row
     */
    public ExRowBaseView getLastRow() {

        int index = getCount() - 1;

        if (index >= 0) {
            return getRow(index);
        }

        return null;
    }

    /**
     * Method_获取 Row 列表
     *
     * @param id 编号
     * @return Row 集合
     */
    public List<ExRowBaseView> getRowList(String id) {

        if (mRowsMaps.containsKey(id)) {
            return mRowsMaps.get(id);
        }

        return null;
    }

    /**
     * Method_获取 ViewHolder 适用于 RecyclerView
     *
     * @param viewType 类型
     * @return ViewHolder
     */
    public ExRowRecyclerView getViewHolder(int viewType) {

        return mRowsViewHolderMaps.get(viewType);
    }

    /**
     * Method_添加 Row 通过位置
     *
     * @param location 位置
     * @param row      Row
     */
    public int add(int location, ExRowBaseView row) {

        return add(null, location, row);
    }

    /**
     * Method_添加 Row 通过位置,唯一标识
     *
     * @param id       唯一标识
     * @param location 位置
     * @param row      Row
     */
    public int add(String id, int location, ExRowBaseView row) {

        putRowMap(id, row);
        putRowViewHolderMap(row);

        if (row != null) {
            mExRows.add(location, row);
        }

        return location;
    }

    /**
     * Method_添加集合 Row 通过位置
     *
     * @param location 位置
     * @param rows     Row 集合
     */
    public void addAll(int location, List<ExRowBaseView> rows) {

        addAll(null, location, rows);
    }

    /**
     * Method_添加集合 Row 通过位置,唯一标识
     *
     * @param id       唯一标识
     * @param location 位置
     * @param rows     Row 集合
     */
    public void addAll(String id, int location, List<ExRowBaseView> rows) {

        if (rows == null) {
            return;
        }

        int size = rows.size();

        for (int i = size - 1; i >= 0; i--) {
            add(id, location, rows.get(i));
        }
    }

    /**
     * Method_添加第一位置 Row
     *
     * @param row Row
     */
    public void addFirst(ExRowBaseView row) {

        addFirst(null, row);
    }

    /**
     * Method_添加第一位置 Row 通过唯一标识
     *
     * @param id  唯一标识
     * @param row Row
     */
    public void addFirst(String id, ExRowBaseView row) {

        add(id, 0, row);
    }

    /**
     * Method_添加第一位置 Row 集合
     *
     * @param rows Row 集合
     */
    public void addAllFirst(List<ExRowBaseView> rows) {

        addAllFirst(null, rows);
    }

    /**
     * Method_添加第一位置 Row 集合 通过唯一标识
     *
     * @param id   唯一标识
     * @param rows Row 集合
     */
    public void addAllFirst(String id, List<ExRowBaseView> rows) {

        if (rows == null) {
            return;
        }

        int size = rows.size();

        for (int i = size - 1; i >= 0; i--) {
            addFirst(id, rows.get(i));
        }
    }

    /**
     * Method_添加最后位置 Row
     *
     * @param row Row
     * @return 插入后位置
     */
    public int addLast(ExRowBaseView row) {

        return addLast(null, row);
    }

    /**
     * Method_添加最后位置 Row 通过唯一标识
     *
     * @param id  唯一标识
     * @param row Row
     * @return 插入后位置
     */
    public int addLast(String id, ExRowBaseView row) {

        putRowMap(id, row);
        putRowViewHolderMap(row);

        int position = -1;

        if (row != null) {
            mExRows.add(row);
            position = mExRows.size() - 1;
        }

        return position;
    }

    /**
     * Method_添加最后位置 Row 集合
     *
     * @param rows Row 集合
     */
    public void addAllLast(List<ExRowBaseView> rows) {

        addAllLast(null, rows);
    }

    /**
     * Method_添加最后位置 Row 集合 通过唯一标识
     *
     * @param id   唯一标识
     * @param rows Row 集合
     */
    public void addAllLast(String id, List<ExRowBaseView> rows) {

        if (rows == null) {
            return;
        }

        int size = rows.size();

        for (int i = 0; i < size; i++) {
            addLast(id, rows.get(i));
        }
    }

    /**
     * Method_移除 Row 通过位置
     *
     * @param index 位置
     * @return 删除后位置
     */
    public int removeRowAt(int index) {

        ExRowBaseView exRow = mExRows.get(index);

        remove(exRow);

        return index;
    }

    /**
     * Method_移除最后位置 Row
     *
     * @return 删除后位置
     */
    public int removeLastRow() {

        int index = getCount() - 1;

        if (index >= 0) {
            removeRowAt(index);
        }

        return index;
    }

    /**
     * Method_移除 Row 通过 Row
     *
     * @param row Row
     * @return 删除后位置
     */
    public int remove(ExRowBaseView row) {

        if (row == null) {
            return -1;
        }

        int index = mExRows.indexOf(row);

        removeRowMap(row);
        removeRowViewHolderMap(row);
        mExRows.remove(row);

        return index;
    }

    /**
     * Method_移除 Row 通过唯一标识
     *
     * @param id 唯一标识
     * @return 删除后位置
     */
    public int remove(String id) {

        if (id == null || !mRowsMaps.containsKey(id)) {
            return -1;
        }

        List<ExRowBaseView> list = mRowsMaps.get(id);
        int index = mExRows.size();

        for (ExRowBaseView exRow : list) {
            int delIndex = mExRows.indexOf(exRow);

            if (index > delIndex) {
                index = delIndex;
            }
            mExRows.remove(exRow);
        }

        mRowsMaps.remove(id);

        return index;
    }

    /**
     * Method_清空 Row 集合
     */
    public void clear() {

        mExRows.clear();
        mRowsMaps.clear();
        mRowsViewHolderMaps.clear();
    }

    /**
     * Method_添加 Row Map 通过唯一标识,Row
     *
     * @param id  唯一标识
     * @param row Row
     */
    private void putRowMap(String id, ExRowBaseView row) {

        if (id == null || row == null) {
            return;
        }

        List<ExRowBaseView> list = new ArrayList<>();

        if (mRowsMaps.containsKey(id)) {
            list = mRowsMaps.get(id);
        }

        list.add(row);

        mRowsMaps.put(id, list);
    }

    /**
     * Method_移除 Row Map 通过 Row
     *
     * @param row Row
     */
    private void removeRowMap(ExRowBaseView row) {

        if (row == null) {
            return;
        }
        for (List<ExRowBaseView> list : mRowsMaps.values()) {
            list.remove(row);
        }
    }

    /**
     * Method_添加 Row ViewHolder 通过 Row
     *
     * @param row Row
     */
    private void putRowViewHolderMap(ExRowBaseView row) {

        if (row == null || !(row instanceof ExRowRecyclerView)) {
            return;
        }

        ExRowRecyclerView recyclerViewRow = (ExRowRecyclerView) row;

        if (!mRowsViewHolderMaps.containsKey(recyclerViewRow.getViewType())) {
            mRowsViewHolderMaps.put(recyclerViewRow.getViewType(), recyclerViewRow);
        }
    }

    /**
     * Method_移除 Row ViewHolder 通过 Row
     *
     * @param row Row
     */
    private void removeRowViewHolderMap(ExRowBaseView row) {

        if (row == null || !(row instanceof ExRowRecyclerView)) {
            return;
        }

        ExRowRecyclerView recyclerViewRow = (ExRowRecyclerView) row;

        if (mRowsViewHolderMaps.containsKey(recyclerViewRow.getViewType())) {
            mRowsViewHolderMaps.remove(recyclerViewRow.getViewType());
        }
    }

}
