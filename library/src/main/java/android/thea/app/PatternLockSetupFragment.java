package android.thea.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.thea.R;
import android.thea.widget.PatternLockView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class PatternLockSetupFragment extends Fragment {

    private PatternLockView mLockView;
    private OnLockSetupListener mLockSetupListener;

    private String mPattern = null;

    private PatternLockView.OnPatternListener mPatternListener = new PatternLockView.OnPatternListener() {
        @Override
        public void onPatternStart() {
        }

        @Override
        public void onPatternEnd(List<Integer> pattern) {
            if (mPattern == null) {
                mPattern = listToString(pattern);
                if (mLockSetupListener != null)
                    mLockSetupListener.onLockSet(pattern);
            }
            else {
                boolean isSame = listToString(pattern).equals(mPattern);
                if (isSame) {
                    new PatternModel(PatternLockSetupFragment.this.getContext()).setLockOpened(true);
                    new PatternModel(PatternLockSetupFragment.this.getContext()).setPattern(mPattern);
                }
                else {
                    mLockView.setWrong();
                    mPattern = null;
                }
                if (mLockSetupListener != null)
                    mLockSetupListener.onLockSetAgain(pattern, isSame);
            }
        }
    };

    public PatternLockSetupFragment() {
    }

    public void setOnLockSetupListener(OnLockSetupListener lockSetupListener) {
        mLockSetupListener = lockSetupListener;
    }

    public void reset() {
        mPattern = null;
        mLockView.clearPattern();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pattern_lock, container, false);
        mLockView = (PatternLockView) view.findViewById(R.id.plv_lock);
        mLockView.setDwellTime(1000);
        mLockView.setOnPatternListener(mPatternListener);
        return view;
    }

    private String listToString(List<Integer> list) {
        String str = "";
        for (int i : list)
            str += "-" + i;
        return str.length() == 0 ? str : str.substring(1);
    }

    public interface OnLockSetupListener {
        void onLockSet(List<Integer> pattern);

        void onLockSetAgain(List<Integer> pattern, boolean isSame);
    }
}
