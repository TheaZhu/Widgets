package com.thea.widgets;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private static final String TAG = MainActivity.class.getSimpleName();
//    private BannerView bv;
//    private ViewPager bv;
    private ArrayList<View> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bv = (BannerView) findViewById(R.id.bv);
//        for (int i = 0; i < 3; i++) {
//            mItems.add(getLayoutInflater().inflate(R.layout.pager_layout, null));
//        }
//        bv.setAdapter(mViewAdapter);
//        bv.setCurrentItem(1);

//        bv.setAdapter(new MyAdapter(mItems));

//        LinearLayout rl = (LinearLayout) findViewById(R.id.rl);
//        View v = findViewById(R.id.ll_btns);
//        CloneView cloneView = new CloneView(this, v);
//        cloneView.setBackgroundColor(Color.RED);
//        ViewCompat.postInvalidateOnAnimation(v);
//        ViewCompat.postInvalidateOnAnimation(cloneView);
//        rl.addView(cloneView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitUtil.back(this);
            return true;
        }*/
        return super.onKeyDown(keyCode, event);
    }

    /*private ViewPagerAdapter mViewAdapter = new ViewPagerAdapter(mItems) {
        @Override
        public View instantiateViewItem(View view, int position) {
//            Button button = (Button) view.findViewById(R.id.btn);
//            button.setText("按钮" + position);
            return view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "第" + position + "页";
        }
    };*/

    public class MyAdapter extends PagerAdapter {
        private List<View> items = new ArrayList<>();

        public MyAdapter(List<View> items) {
            this.items = items;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("MyAdapter", position + "");
            View view = items.get(position);
            Button button = (Button) view.findViewById(R.id.btn);
            button.setText("按钮" + position);
            if (container.indexOfChild(view) == -1)
                container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = items.get(position);
            if (container.indexOfChild(view) != -1)
                container.removeView(view);
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "第" + position + "页";
        }
    }
}
