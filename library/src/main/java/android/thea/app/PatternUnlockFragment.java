package android.thea.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.thea.R;
import android.thea.widget.PatternLockView;
import android.thea.widget.PatternLockView.OnPatternListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatternUnlockFragment extends Fragment {

    private PatternLockView mLockView;
    private OnUnlockListener mUnlockListener;

    private OnPatternListener mPatternListener = new OnPatternListener() {
        @Override
        public void onPatternStart() {
        }

        @Override
        public void onPatternEnd(List<Integer> pattern) {
            String p = new PatternModel(PatternUnlockFragment.this.getContext()).getPattern();
            if (!listToString(pattern).equals(p))
                mLockView.setWrong();
            if (mUnlockListener != null)
                mUnlockListener.onUnlock(listToString(pattern).equals(p));
        }
    };

    public PatternUnlockFragment() {
    }

    public void setOnUnlockListener(OnUnlockListener unlockListener) {
        mUnlockListener = unlockListener;
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
        mLockView.setOnPatternListener(mPatternListener);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String listToString(List<Integer> list) {
        String str = "";
        for (int i : list)
            str += "-" + i;
        return str.substring(1);
    }

    public interface OnUnlockListener {
        void onUnlock(boolean isCorrect);
    }
}
