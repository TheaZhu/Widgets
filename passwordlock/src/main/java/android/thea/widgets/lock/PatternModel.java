package android.thea.widgets.lock;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Thea on 2016/1/15 0015.
 */
public class PatternModel {
    private SharedPreferences sp;

    public PatternModel(Context context) {
        sp = context.getSharedPreferences("pattern", Context.MODE_PRIVATE);
    }

    public boolean getLockOpened() {
        return sp.getBoolean("lock_open", false);
    }

    public void setLockOpened(boolean open) {
        sp.edit().putBoolean("lock_open", open).apply();
    }

    public String getPattern() {
        return sp.getString("pattern", "");
    }

    public void setPattern(String pattern) {
        sp.edit().putString("pattern", pattern).apply();
    }
}
