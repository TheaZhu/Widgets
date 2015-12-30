package android.thea.widgets.bannerview;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Thea on 2015/12/29 0029.
 */
public class CycleViewPager extends ViewPager {
    private static final String TAG = CyclePagerAdapter.class.getSimpleName();

    private Handler mHandler = new Handler();

    private CyclePagerAdapter mCycleAdapter;

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            if (mCycleAdapter != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 0)
                            setCurrentItem((mCycleAdapter.getCount() - 2) %
                                    mCycleAdapter.getCount(), false); // 切到倒数第二页
                        else if (position == mCycleAdapter.getCount() - 1)
                            setCurrentItem(1, false);
                    }
                }, 200);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public CycleViewPager(Context context) {
        super(context);
    }

    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter instanceof ViewPagerAdapter && adapter.getCount() > 1) {
            mCycleAdapter = new CyclePagerAdapter((ViewPagerAdapter) adapter);
            addOnPageChangeListener(mPageChangeListener);
            super.setAdapter(mCycleAdapter);
            setCurrentItem(1);
        }
        else
            super.setAdapter(adapter);
    }

    public int convertPosition(int position) {
        if (mCycleAdapter == null)
            return position;
        if (position == 0)
            return mCycleAdapter.getCount() - 3;
        else if (position == mCycleAdapter.getCount() - 1)
            return 0;
        else
            return position - 1;
    }

    /*@Override
    public PagerAdapter getAdapter() {
        return mViewAdapter == null ? super.getAdapter() : mViewAdapter;
    }*/

    private class CyclePagerAdapter extends PagerAdapter {
        private ViewPagerAdapter mAdapter;
        private View mHeader, mTail;

        public CyclePagerAdapter(ViewPagerAdapter adapter) {
            mAdapter = adapter;
            mHeader = new CloneView(CycleViewPager.this.getContext(),
                    adapter.getItem(adapter.getCount() - 1));
            mTail = new CloneView(CycleViewPager.this.getContext(), adapter.getItem(0));
        }

        @Override
        public int getCount() {
            return mAdapter.getCount() + 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v;
            if (position == 0)
                v = mAdapter.instantiateViewItem(mHeader, mAdapter.getCount() - 1);
            else if (position > mAdapter.getCount())
                v = mAdapter.instantiateViewItem(mTail, 0);
            else
                v = mAdapter.instantiateViewItem(mAdapter.getItem(position - 1), position - 1);
            if (container.indexOfChild(v) == -1)
                container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = getItem(position);
            if (container.indexOfChild(v) != -1)
                container.removeView(v);
        }

        public View getItem(int position) {
            if (position == 0)
                return mHeader;
            else if (position > mAdapter.getCount())
                return mTail;
            else
                return mAdapter.getItem(position - 1);
        }

        @Override
        public int getItemPosition(Object object) {
            if (object == mHeader)
                return getCount() - 1;
            else if (object == mTail)
                return 0;
            else
                return mAdapter.getItemPosition(object) + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mAdapter.getPageTitle(convertPosition(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
