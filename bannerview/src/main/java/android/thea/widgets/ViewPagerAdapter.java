package android.thea.widgets;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thea on 2015/12/29 0029.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mItems = new ArrayList<>();

    public ViewPagerAdapter() {
    }

    public ViewPagerAdapter(List<View> items) {
        mItems = items;
    }

    public View getItem(int index) {
        return mItems.get(index);
    }

    public View instantiateViewItem(View view, int position) {
        return view;
    }

    public View instantiateViewItem(ViewGroup container, View view, int position) {
        return view;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mItems.get(position);
        instantiateViewItem(v, position);
        if (container.indexOfChild(v) == -1)
            container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = mItems.get(position);
        if (container.indexOfChild(v) != -1)
            container.removeView(v);
    }
}
