/*
 * Copyright (c) 2014 by EagleXad
 * Team: EagleXad
 * Create: 2014-08-29
 */
package lib.core.row;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author Aloneter
 * @ClassName: ExRowListView
 * @Description: Row 模式 抽象类
 * @date 2015-12-11
 * @Version 6.0
 */
public abstract class ExRowListView extends ExRowBaseView {

    /**
     * Method_获取视图
     *
     * @param position    位置
     * @param convertView 视图
     * @param parent      父容器
     * @return 视图
     */
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
