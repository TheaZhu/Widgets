package android.thea.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.thea.R;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Thea on 2016/1/14 0014.
 */
public class CircleView extends View {
    private static final int STYLE_FILL = 0;
    private static final int STYLE_STROKE = 1;

    private Paint mPaint;
    private int mStyle;
    private int mStrokeWidth;
    private ColorStateList mColor;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView,
                defStyleAttr, R.style.Widget_CircleView);
        mStyle = typedArray.getInt(R.styleable.CircleView_cv_style, 1);
        mColor = typedArray.getColorStateList(R.styleable.CircleView_cv_color);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleView_cv_stroke_width, 0);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setStyle(mStyle == STYLE_FILL ? Paint.Style.FILL : Paint.Style.STROKE);
        mPaint.setColor(mColor.getDefaultColor());
        if (mStyle == STYLE_STROKE)
            mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
    }

    public void setColor(ColorStateList colors) {
        if (colors != null) {
            mColor = colors;
            setColor(colors.getDefaultColor());
        }
    }

    public void setColor(@ColorInt int color) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.min(getWidth() / 2, getHeight() / 2);
        if (mStyle == STYLE_STROKE)
            radius -= mStrokeWidth / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
    }
}
