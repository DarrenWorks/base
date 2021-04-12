package com.base.baselibrary.background;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.noober.background.BackgroundFactory;

/**
 * Create by Darren
 * On 2021/1/14 10:00
 **/
public class BlRecyclerView extends RecyclerView {
    public BlRecyclerView(Context context) {
        super(context);
    }

    public BlRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BlRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
