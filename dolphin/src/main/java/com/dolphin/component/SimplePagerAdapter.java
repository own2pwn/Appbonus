package com.dolphin.component;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

public class SimplePagerAdapter<T> extends PagerAdapter {
    protected LayoutInflater inflater;
    protected List<View> views;
    protected List<T> data;

    public SimplePagerAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.views = new LinkedList<View>();
    }

    protected void processViews() {
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View v = views.get(position);
        collection.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public void finishUpdate(ViewGroup container) {
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setData(List<T> data) {
        this.data = data;
        processViews();
    }
}
