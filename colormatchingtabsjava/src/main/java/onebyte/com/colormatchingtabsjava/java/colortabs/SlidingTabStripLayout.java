package onebyte.com.colortabs.colortabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import onebyte.com.colortabs.R;
import onebyte.com.colortabs.Constant;


public class SlidingTabStripLayout extends LinearLayout {

    private float CONTROL_X1 = 0.175f;
    private float CONTROL_Y1 = 0.885f;
    private float CONTROL_X2 = 0.360f;
    private float CONTROL_Y2 = 1.200f;
    private int FIRST_TAB_POSITION = 0;
    private int INVALID_TABS_AMOUNT = 5;

    private Paint backgroundPaint;
    private Canvas backgroundCanvas;
    public boolean isAnimate = false;
    private float animateLeftX = 0f;
    private float animateY = 0f;
    private boolean isMenuToggle = false;

    public SlidingTabStripLayout(Context context) {
        this(context, null);
    }

    public SlidingTabStripLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabStripLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);
        setWillNotDraw(false);
        initCanvas();
    }
//    }

    /**
     * The method creates the Paint() object to create a rectangle and passes it Paint.ANTI_ALIAS_FLAG
     * that smooths the edges of the rectangle
     */

    private void initCanvas() {
        backgroundPaint = new Paint();
        backgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * The method draw rectangle for selected tab and animate it if it is necessary
     */

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isMenuToggle) {
            int i;
            for (i = 0; i <= (getChildCount() - 1); i++) {
                ColorTabView child = (ColorTabView) getChildAt(i);
                backgroundCanvas = canvas;
                if (child.tab.isSelected) {
                    backgroundPaint.setColor(child.tab.selectedColor);
                }
                if (child.tab.isSelected && isAnimate) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        animateRectangle(child, canvas);
                    }
                } else if (child.tab.isSelected && !isAnimate && !isMenuToggle) {
                    drawBackgroundTab(child, canvas);
                }
            }
        }
    }

    /**
     * Draw a rectangle if it is not animated
     */

    private void drawBackgroundTab(ColorTabView child, Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawRectangle(child, canvas, false);
        }
    }

    /**
     * Animate the selected tab rectangle. Called in onLayout() method of ColorTabView
     */

    public void animateDrawTab(final ColorTabView child) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(((ColorMatchTabLayout) getParent()).previousSelectedTab.getX(), child.getX());
        valueAnimator.setDuration(Constant.ANIMATION_DURATION);
        valueAnimator.setInterpolator(PathInterpolatorCompat.create(CONTROL_X1, CONTROL_Y1, CONTROL_X2, CONTROL_Y2));
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                child.clickedTabView = null;
                isAnimate = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animateLeftX = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();

    }

    /**
     * Draw a rectangle if it is animated
     */

    private void animateRectangle(ColorTabView child, Canvas canvas) {
        drawRectangle(child, canvas, true);
    }

    private void drawRectangle(ColorTabView child, Canvas canvas, boolean isAnimateRectangle) {
        float left = 0f;
        float right = 0f;
        if (!isAnimateRectangle) {
            if (child.tab.position == FIRST_TAB_POSITION)
                left = child.getX() - getContext().getResources().getDimensionPixelOffset(R.dimen.radius);
            else
                left = child.getX();
            if (child.tab.position == ((ColorMatchTabLayout) getParent()).count() - 1)
                right = child.getX() + child.getWidth() + getContext().getResources().getDimensionPixelOffset(R.dimen.radius);
            else
                right = child.getX() + child.getWidth();
        } else {
            float leftX = animateLeftX;
            if (child.tab.position == FIRST_TAB_POSITION)
                left = leftX - (getContext().getResources().getDimensionPixelOffset(R.dimen.radius));
            else
                left = leftX;
            if (child.tab.position == ((ColorMatchTabLayout) getParent()).count() - (1))
                right = leftX + child.getWidth() + getContext().getResources().getDimensionPixelOffset(R.dimen.radius);
            else
                right = leftX + child.getWidth();
        }
        float z;
        if (isMenuToggle)
            z = animateY;
        else
            z = 0f;
        RectF rectangle = new RectF(left, z, right, child.getHeight());
        canvas.drawRoundRect(rectangle, getContext().getResources().getDimensionPixelOffset(R.dimen.radius), getContext().getResources().getDimensionPixelOffset(R.dimen.radius), backgroundPaint);
    }

}