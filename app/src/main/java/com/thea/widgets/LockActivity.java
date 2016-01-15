package com.thea.widgets;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.thea.widgets.lock.app.PatternLockSetupFragment;
import android.widget.TextView;

import java.util.List;

public class LockActivity extends AppCompatActivity {
    private TextView tv;
    private PatternLockSetupFragment mLockSetupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        tv = (TextView) findViewById(R.id.tv);
        mLockSetupFragment = (PatternLockSetupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_pattern_lock);
        mLockSetupFragment.setOnLockSetupListener(new PatternLockSetupFragment.OnLockSetupListener() {
            @Override
            public void onLockSet(List<Integer> pattern) {
                tv.setText("请再次绘制解锁图案");
            }

            @Override
            public void onLockSetAgain(List<Integer> pattern, boolean isSame) {
                if (isSame) {
                    tv.setText("设置成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }
                else {
                    tv.setText("与上次输入不一致，请重新设置");
                }
            }
        });
    }

}
