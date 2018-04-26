package onebyte.com.colortabs.colortabs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import onebyte.com.colortabs.R;
import onebyte.com.colortabs.model.ColorTab;

import static onebyte.com.colortabs.Constant.ANIMATION_TEXT_APPEARANCE_DURATION;


public class ColorTabView extends LinearLayout implements View.OnClickListener {

    public ColorTabView(Context context) {
        this(context, null);
    }
    public ColorTabView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    public ColorTabView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        initColorTabView();
        }

       public ColorTab tab = null;

    public void setTab(ColorTab value) {
        tab = value;
        updateView();
        }

    public TextView textView;
    public ImageView iconView;
    public ColorTabView clickedTabView = null;

    private void initColorTabView() {
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        setClickable(true);
        setBackgroundColor(Color.TRANSPARENT);
        initViews();
        this.setOnClickListener(ColorTabView.this);
        }

    private void initViews() {
        iconView = new ImageView(getContext());
        iconView.setBackgroundColor(Color.TRANSPARENT);
        addView(iconView);
        textView =new TextView(getContext());
        addView(textView);
        }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            // This view masquerades as an action bar tab.
            event.setClassName(ActionBar.Tab.class.getName());
        }
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            // This view masquerades as an action bar tab.
            info.setClassName(ActionBar.Tab.class.getName());
        }

        @Override
        public void onMeasure(int origWidthMeasureSpec, int origHeightMeasureSpec) {
        int specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec);
        long maxWidth = ((ColorMatchTabLayout)getParent().getParent()).tabMaxWidth;

        int widthMeasureSpec;
        int heightMeasureSpec = origHeightMeasureSpec - getResources().getDimensionPixelOffset(R.dimen.tab_padding);
        if (maxWidth > 0 && (specWidthMode == MeasureSpec.UNSPECIFIED || specWidthSize > maxWidth)) {
        // If we have a max width and a given spec which is either unspecified or
        // larger than the max width, update the width spec using the same mode
        int selectTabMaxWidth;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            selectTabMaxWidth = ((ColorMatchTabLayout)getParent().getParent()).selectedTabWidth;
        else
            selectTabMaxWidth = ((ColorMatchTabLayout)getParent().getParent()).selectedTabHorizontalWidth;
        long i;
        if(tab.isSelected)
            i = selectTabMaxWidth;
        else
            i = selectTabMaxWidth;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) i, MeasureSpec.EXACTLY);
        } else {
        // Else, use the original width spec
        widthMeasureSpec = origWidthMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        iconView.setPadding(getResources().getDimensionPixelOffset(R.dimen.normal_margin), 0, getResources().getDimensionPixelOffset(R.dimen.normal_margin), getResources().getDimensionPixelOffset(R.dimen.tab_padding));
        textView.setPadding(0, 0, getResources().getDimensionPixelOffset(R.dimen.normal_margin), getResources().getDimensionPixelOffset(R.dimen.tab_padding));
        if (clickedTabView != null) {
            ((SlidingTabStripLayout) getParent()).animateDrawTab(clickedTabView);
        }
        }

        @Override
        public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateView();
        }

        public void updateView() {
        ColorTab colorTab = tab;
        if (colorTab.text != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setAlpha(0f);
            textView.setMaxLines(1);
            textView.setText(colorTab.text);
            reColorTextView(colorTab.isSelected);
//            textView.setTextColor(getBackgroundColor());
            animatePlayButton();
            textView.requestLayout();
        }
//        } else {
//        textView.setVisibility(View.GONE);
//        }
        if (colorTab.icon != null) {
        iconView.setImageDrawable(colorTab.icon);
        reColorDrawable(colorTab.isSelected);
        iconView.requestLayout();
        }
        requestLayout();
        }

        public void animatePlayButton() {
        textView.animate().alpha(1f).setDuration(ANIMATION_TEXT_APPEARANCE_DURATION).start();
        }

        @Override
        public void onClick(View v) {
        if (!((SlidingTabStripLayout) getParent()).isAnimate) {
        ColorTabView clickedTabView = (ColorTabView) v;
        if (((ColorMatchTabLayout) getParent().getParent()).internalSelectedTab != clickedTabView.tab) {
            ((ColorMatchTabLayout) getParent().getParent()).select(clickedTabView.tab);
        this.clickedTabView = clickedTabView;
        }
        }
        }

        public void reColorDrawable(boolean isSelected) {
        if (isSelected) {
        iconView.setColorFilter(null);
        iconView.setColorFilter(getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
        } else {
        iconView.setColorFilter(tab.selectedColor, PorterDuff.Mode.SRC_ATOP);
        }
        }
    public void reColorTextView(boolean isSelected) {
        if (isSelected) {
            textView.setTextColor(getBackgroundColor());
        } else {
            textView.setTextColor(tab.selectedColor);
        }
    }

        public int getBackgroundColor(){
        int color = ContextCompat.getColor(getContext(), R.color.mainBackgroundColor);
        if (getParent() != null) {
        ColorDrawable background = (ColorDrawable)((ColorMatchTabLayout)getParent().getParent()).getBackground();
        if (background instanceof ColorDrawable) {
        color = background.getColor();
        }
        }
        return color;
        }

        }