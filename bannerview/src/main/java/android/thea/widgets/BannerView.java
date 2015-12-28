package android.thea.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thea on 2015/12/22 0022.
 */
public class BannerView extends FrameLayout {
    private static final String TAG = BannerView.class.getSimpleName();
    public static final int MARK_CENTER = Gravity.CENTER_HORIZONTAL;

    private ViewPager mViewPager;
    private PagerMarkStrip mMarkStrip;
    private final PageListener mPageListener = new PageListener();

    private int mLastKnownCurrentPage = -1;

    private int mMarkGravity;

    private Drawable mSelectIcon;
    private Drawable mNormalIcon;
    private int mIconPaddingStart;
    private int mIconPaddingTop;
    private int mIconPaddingEnd;
    private int mIconPaddingBottom;

    private boolean mShowTitle;
    private int mTitleAppearance;
    private ColorStateList mTitleTextColors;
    private float mTitleTextSize;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView,
                defStyleAttr, R.style.Widget_BannerView);

        mMarkGravity = typedArray.getInt(R.styleable.BannerView_markGravity, MARK_CENTER);
        mSelectIcon = typedArray.getDrawable(R.styleable.BannerView_selectIcon);
        mNormalIcon = typedArray.getDrawable(R.styleable.BannerView_normalIcon);
        mIconPaddingStart = mIconPaddingTop = mIconPaddingEnd = mIconPaddingBottom =
                typedArray.getDimensionPixelSize(R.styleable.BannerView_iconPadding, 0);
        mIconPaddingStart = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_iconPaddingStart, mIconPaddingStart);
        mIconPaddingTop = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_iconPaddingStart, mIconPaddingTop);
        mIconPaddingEnd = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_iconPaddingStart, mIconPaddingEnd);
        mIconPaddingBottom = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_iconPaddingStart, mIconPaddingBottom);

        mShowTitle = typedArray.getBoolean(R.styleable.BannerView_showPageTitle, false);
        mTitleTextColors = typedArray.getColorStateList(R.styleable.BannerView_pageTitleTextColor);
        mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.BannerView_pageTitleTextSize,
                getResources().getDimensionPixelOffset(R.dimen.title_text_size));

        typedArray.recycle();

        mViewPager = new ViewPager(context);
        mViewPager.addOnPageChangeListener(mPageListener);
        addView(mViewPager, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mMarkStrip = new PagerMarkStrip(context);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        addView(mMarkStrip, lp);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setAdapter(PagerAdapter adapter) {
        adapter.registerDataSetObserver(mPageListener);
        mMarkStrip.initTitleView();
        if (adapter instanceof CyclePagerAdapter)
            mMarkStrip.initIcons(adapter.getCount() - 2);
        else
            mMarkStrip.initIcons(adapter.getCount());
        mViewPager.setAdapter(adapter);
        updateMark(0);
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    public void updateMark(int currentItem) {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter instanceof CyclePagerAdapter)
            currentItem = ((CyclePagerAdapter) adapter).getItemPosition(currentItem);
        mMarkStrip.update(currentItem, mViewPager.getAdapter());
        mLastKnownCurrentPage = currentItem;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        // This view masquerades as an action bar tab.
        event.setClassName(BannerView.class.getName());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        // This view masquerades as an action bar tab.
        info.setClassName(BannerView.class.getName());
    }

    public class PagerMarkStrip extends LinearLayout {
        private TextView mTitleView;
        private LinearLayout mLayout;
        private List<ImageView> mIcons = new ArrayList<>();

        public PagerMarkStrip(Context context) {
            super(context);
            setWillNotDraw(false);
            setOrientation(VERTICAL);
        }

        public void initTitleView() {
            if (mTitleView == null) {
                mTitleView = (TextView) LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_mark_title, this, false);
//            mTitleView.setTextAppearance(mTitleAppearance);
                mTitleView.setTextSize(mTitleTextSize);
                if (mTitleTextColors != null)
                    mTitleView.setTextColor(mTitleTextColors);
                mTitleView.setGravity(Gravity.CENTER);
            }
            else {
                removeView(mTitleView);
            }
            LayoutParams lp = (LayoutParams) mTitleView.getLayoutParams();
            lp.gravity = mMarkGravity;
            addView(mTitleView, 0, lp);
        }

        public void initIcons(int count) {
            if (mLayout == null)
                mLayout = (LinearLayout) LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_mark_icons, this, false);
            else {
                removeView(mLayout);
                mLayout.removeAllViews();
            }

            for (int i = 0; i < count; i++) {
                final int position = i;
                ImageView iv = (ImageView) LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_mark_icon, mLayout, false);
                iv.setImageDrawable(mNormalIcon);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(position, true);
                    }
                });
                ViewCompat.setPaddingRelative(iv, mIconPaddingStart, mIconPaddingTop,
                        mIconPaddingEnd, mIconPaddingBottom);
                mIcons.add(iv);
                mLayout.addView(iv);
            }

            LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
            lp.gravity = mMarkGravity;
            addView(mLayout);
        }

        public void update(int position, PagerAdapter adapter) {
            if (position >= 0 && adapter != null && position != mLastKnownCurrentPage) {
                if (mShowTitle)
                    mTitleView.setText(adapter.getPageTitle(position));
                if (mLastKnownCurrentPage >= 0 && mLastKnownCurrentPage < mIcons.size())
                    mIcons.get(mLastKnownCurrentPage).setImageDrawable(mNormalIcon);
                mIcons.get(position).setImageDrawable(mSelectIcon);
            }
        }
    }

    private class PageListener extends DataSetObserver implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_SETTLING) {
                updateMark(mViewPager.getCurrentItem());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        @Override
        public void onChanged() {
            updateMark(mViewPager.getCurrentItem());
        }
    }
}
