package android.thea.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by Thea on 2015/12/29 0029.
 */
public class CloneView extends View {
    private View mView;

    public CloneView(Context context, View view) {
        super(context);
        mView = view;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mView.draw(canvas);
    }
}
