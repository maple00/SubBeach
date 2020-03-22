package com.rainwood.subbeach.looper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.rainwood.subbeach.R;

/**
 * @Author: sxs
 * @Time: 2020/3/22 14:52
 * @Desc: java类作用描述
 */
public class SxsLooperPager extends LinearLayout {

    public SxsLooperPager(Context context) {
        this(context, null);
    }

    public SxsLooperPager(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public SxsLooperPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.looper_pager_layout, this, true);
    }
}
