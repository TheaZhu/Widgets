package com.thea.widgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.thea.widgets.BottomTabLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private BottomTabLayout btl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btl = (BottomTabLayout) findViewById(R.id.btl);
//        btl.addTab(btl.newTab().setIcon(R.mipmap.ic_recommend).setText("aaa"));
        btl.addTab(btl.newTab().setText("bbb"));
        btl.addTab(btl.newTab().setText("ccc"));
        btl.addTab(btl.newTab().setText("ddd"));
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
}
