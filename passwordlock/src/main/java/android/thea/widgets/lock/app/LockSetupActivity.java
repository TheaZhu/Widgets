package android.thea.widgets.lock.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.thea.widgets.lock.R;
import android.widget.TextView;

import java.util.List;

public class LockSetupActivity extends AppCompatActivity {
    private TextView tv;
    private PatternLockSetupFragment mLockSetupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_up);

        tv = (TextView) findViewById(R.id.tv);
        tv.setText("请绘制解锁图案");
        mLockSetupFragment = (PatternLockSetupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_pattern_lock);
        mLockSetupFragment.setOnLockSetupListener(new PatternLockSetupFragment.OnLockSetupListener() {
            @Override
            public void onLockSet(List<Integer> pattern) {
                if (pattern.size() < 3) {
                    mLockSetupFragment.reset();
                    tv.setText("最少连接三个，请重新设置");
                }
                else
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
