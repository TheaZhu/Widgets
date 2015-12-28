package android.thea.widgets;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Thea on 2015/12/22 0022.
 */
public class CyclePagerAdapter extends PagerAdapter
        implements ViewPager.OnPageChangeListener {
    private static final String TAG = CyclePagerAdapter.class.getSimpleName();
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private View mHeader, mTail;

    private Handler mHandler = new Handler();

    public CyclePagerAdapter(ViewPager viewPager, PagerAdapter adapter) {
        mViewPager = viewPager;
        mAdapter = adapter;

        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public int getCount() {
        return enabled() ? mAdapter.getCount() + 2 : mAdapter != null ? mAdapter.getCount() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i(TAG, position + "");
        View v = null;
        if (position == 1) {
            if (mHeader != null && container.indexOfChild(mHeader) == -1)
                v = mHeader;
            if (mTail == null || container.indexOfChild(mTail) != -1)
                mTail = (View) mAdapter.instantiateItem(container, mAdapter.getCount() - 1);
        }
        else if (position == getCount() - 2) {
            if(mTail != null && container.indexOfChild(mTail) == -1)
                v = mTail;
            if (mHeader == null || container.indexOfChild(mHeader) != -1)
                mHeader = (View) mAdapter.instantiateItem(container, 0);
        }

        Log.i(TAG, (v == null) + " " + (mHeader == null) + " " + (mTail == null) + "");

        if (v == null)
            v = (View) mAdapter.instantiateItem(container, convert(position));
        if (container.indexOfChild(v) == -1)
            container.addView(v);
        return v;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i(TAG, "destroyItem: " + position);
        mAdapter.destroyItem(container, convert(position), object);

        if (container.indexOfChild((View) object) != -1)
            container.removeView((View) object);
    }

    public int getItemPosition(Object object) {
        return mAdapter.getItemPosition(object);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (!edge(position) && mAdapter != null)
            mAdapter.setPrimaryItem(container, convert(position), object);
    }

    private int convert(int item) {
        if (!enabled())
            return item;
        else if (item == 0)
            return mAdapter.getCount() - 1;
        else if (item > mAdapter.getCount())
            return 0;
        else
            return item - 1;
    }

    private boolean enabled() {
        return mAdapter != null && mAdapter.getCount() > 1;
    }

    private boolean edge(int position){
        return enabled() && (position == 0 || position == getCount() - 1);
    }

    @Override
    public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        if(enabled())
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(position == 0)
                        mViewPager.setCurrentItem((getCount() - 2) % getCount(), false); // 切到倒数第二页
                    else if(position == getCount() - 1)
                        mViewPager.setCurrentItem(1, false);
                }
            }, 30); // 延迟切换，避免闪烁
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
