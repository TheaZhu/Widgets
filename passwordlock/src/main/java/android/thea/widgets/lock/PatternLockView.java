package android.thea.widgets.lock;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thea on 2016/1/13 0013.
 */
public class PatternLockView extends LinearLayout{
    private CellLayout mCellLayout;
    private Paint mPathPaint;

    private Cell[] cells = new Cell[9];
    private ArrayList<Integer> mPattern = new ArrayList<>();

    private ColorStateList mPatternColor;
    private ColorStateList mNormalItemColor;
    private ColorStateList mWrongHintColor;

    private int mItemSize;
    private int mItemSpace;

    private OnPatternListener mPatternListener;

    public PatternLockView(Context context) {
        this(context, null);
    }

    public PatternLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PatternLockView,
                defStyleAttr, R.style.Widget_PatternLockView);

        mPatternColor = typedArray.getColorStateList(
                R.styleable.PatternLockView_plv_patternColor);
        mNormalItemColor = typedArray.getColorStateList(
                R.styleable.PatternLockView_plv_normalItemColor);
        mWrongHintColor = typedArray.getColorStateList(
                R.styleable.PatternLockView_plv_wrongHintColor);

        mItemSize = typedArray.getDimensionPixelSize(R.styleable.PatternLockView_plv_itemSize, 0);
        mItemSpace = typedArray.getDimensionPixelSize(R.styleable.PatternLockView_plv_itemSpace, 0);

        typedArray.recycle();

        mCellLayout = new CellLayout(context);
        addView(mCellLayout, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeWidth(context.getResources().getDimensionPixelSize(
                R.dimen.big_circle_stroke_width));
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setColor(mPatternColor.getDefaultColor());
    }

    public void setOnPatternListener(OnPatternListener patternListener) {
        mPatternListener = patternListener;
    }

    public void setWrong() {
        mPathPaint.setColor(mWrongHintColor.getDefaultColor());
        ViewCompat.postInvalidateOnAnimation(mCellLayout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clearPattern();
            }
        }, 2000);
    }

    public void clearPattern() {
        mPattern.clear();
        mPathPaint.setColor(mPatternColor.getDefaultColor());
        mCellLayout.initCells();
        ViewCompat.postInvalidateOnAnimation(mCellLayout);
    }

    public void drawAllPath(Canvas canvas) {
        for (int i = 0; i < mPattern.size() - 1; i++)
            drawPath(canvas, mPattern.get(i), mPattern.get(i + 1));
    }

    public void drawPath(Canvas canvas, int start, int end) {
        if (start >= 0 && start < cells.length && end >= 0 && end < cells.length) {
            float endX = cells[end].getCenterX();
            float endY = cells[end].getCenterY();
            drawPath(canvas, start, endX, endY);
        }
    }

    public void drawPath(Canvas canvas, int start, float endX, float endY) {
        if (start >= 0 && start < cells.length) {
            float startX = cells[start].getCenterX();
            float startY = cells[start].getCenterY();
            canvas.drawLine(startX, startY, endX, endY, mPathPaint);
        }
    }

    public class CellLayout extends GridLayout {
        private float currentX;
        private float currentY;

        public CellLayout(Context context) {
            super(context);
            setWillNotDraw(false);
            setColumnCount(3);
            setRowCount(3);
            initCells();
        }

        public void initCells() {
            removeAllViews();
            for (int i = 0; i < cells.length; i++) {
                cells[i] = new Cell();
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.width = mItemSize;
                lp.height = mItemSize;
                if (i / 3 > 0)
                    lp.topMargin = mItemSpace;
                if (i % 3 != 0)
                    lp.leftMargin = mItemSpace;
                addView(cells[i].getCell(), lp);
            }
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            drawAllPath(canvas);
            if (!mPattern.isEmpty())
                drawPath(canvas, mPattern.get(mPattern.size() - 1), currentX, currentY);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            float x = event.getX();
            float y = event.getY();
            if (action == MotionEvent.ACTION_DOWN) {
                clearPattern();
                int selectCell = judgeSelectCell(x, y);
                if (selectCell != -1) {
                    Cell cell = cells[selectCell];
                    cell.setSelected(true);
                    currentX = cell.getCenterX();
                    currentY = cell.getCenterY();
                    ViewCompat.postInvalidateOnAnimation(this);
                    mPattern.add(selectCell);
                    if (mPatternListener != null)
                        mPatternListener.onPatternStart();
                }
            }
            else if (action == MotionEvent.ACTION_MOVE) {
                int selectCell = judgeSelectCell(x, y);
                currentX = x;
                currentY = y;
                if ((selectCell == -1 && !mPattern.isEmpty()) ||
                        (selectCell != -1 && mPattern.contains(selectCell)))
                    ViewCompat.postInvalidateOnAnimation(this);
                else if (selectCell != -1 && !mPattern.contains(selectCell)) {
                    Cell cell = cells[selectCell];
                    cell.setSelected(true);
                    currentX = cell.getCenterX();
                    currentY = cell.getCenterY();
                    ViewCompat.postInvalidateOnAnimation(this);
                    mPattern.add(selectCell);
                    if (mPattern.size() == 1 && mPatternListener != null)
                        mPatternListener.onPatternStart();
                }
            }
            else if (action == MotionEvent.ACTION_UP) {
                if (!mPattern.isEmpty()) {
                    currentX = cells[mPattern.get(mPattern.size() - 1)].getCenterX();
                    currentY = cells[mPattern.get(mPattern.size() - 1)].getCenterY();
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearPattern();
                    }
                }, 1500);
                if (mPatternListener != null)
                    mPatternListener.onPatternEnd(mPattern);
            }
            return true;
        }
    }

    public int judgeSelectCell(float x, float y) {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].touched(x, y))
                return i;
        }
        return -1;
    }

    class Cell {
        View cell;
        CircleView big;
        CircleView small;

        private boolean isSelected = false;

        public Cell() {
            cell = LayoutInflater.from(getContext()).inflate(R.layout.layout_cell, null);
            big = (CircleView) cell.findViewById(R.id.cv_big);
            small = (CircleView) cell.findViewById(R.id.cv_small);

            big.setColor(mNormalItemColor);
            small.setColor(getContext().getResources().getColor(android.R.color.transparent));
        }

        public View getCell() {
            return cell;
        }

        public boolean touched(float x, float y) {
            return x >= cell.getX() + small.getX() && x <= cell.getX() + small.getX() + small.getWidth()
                    && y >= cell.getY() + small.getY() &&
                    y <= cell.getY() + small.getY() + small.getHeight();
        }

        public float getCenterX() {
            return cell.getX() + small.getX() + small.getWidth() / 2;
        }

        public float getCenterY() {
            return cell.getY() + small.getY() + small.getHeight() / 2;
        }

        public void setSelected(boolean selected) {
            if (isSelected != selected) {
                if (selected)
                    small.setColor(mPatternColor);
                else
                    small.setColor(getContext().getResources().getColor(android.R.color.transparent));
                isSelected = selected;
            }
        }
    }

    public interface OnPatternListener {
        void onPatternStart();

        void onPatternEnd(List<Integer> pattern);
    }
}
