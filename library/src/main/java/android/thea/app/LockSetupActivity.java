package android.thea.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.thea.R;
import android.widget.TextView;

import java.util.List;

import static android.thea.app.PatternLockSetupFragment.*;

public class LockSetupActivity extends AppCompatActivity {
    private TextView tv;
    private PatternLockSetupFragment mLockSetupFragment;

    private OnLockSetupListener mLockSetupListener = new OnLockSetupListener() {
        @Override
        public void onLockSet(List<Integer> pattern) {
            if (pattern.size() < 3) {
                mLockSetupFragment.reset();
                tv.setText(R.string.error_short_pattern);
            }
            else
                tv.setText(R.string.please_draw_pattern_again);
        }

        @Override
        public void onLockSetAgain(List<Integer> pattern, boolean isSame) {
            if (isSame) {
                tv.setText(R.string.success_pattern);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
            else
                tv.setText(R.string.error_not_same);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setup);

        tv = (TextView) findViewById(R.id.tv);
        mLockSetupFragment = (PatternLockSetupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_pattern_lock);
        mLockSetupFragment.setOnLockSetupListener(mLockSetupListener);
    }

}
