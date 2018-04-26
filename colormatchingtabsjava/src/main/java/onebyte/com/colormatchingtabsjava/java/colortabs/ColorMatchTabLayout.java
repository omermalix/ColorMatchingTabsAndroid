package onebyte.com.colortabs.colortabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import onebyte.com.colortabs.R;
import onebyte.com.colortabs.listeners.OnColorTabSelectedListener;
import onebyte.com.colortabs.model.ColorTab;


public class ColorMatchTabLayout extends HorizontalScrollView{

    private int INVALID_WIDTH = -1;

    public SlidingTabStripLayout tabStripLayout;
    public List<ColorTab> tabs= new ArrayList<>();
    public OnColorTabSelectedListener tabSelectedListener;
    public ColorTab internalSelectedTab;
    public long tabMaxWidth = 2147483647;
    public ColorTabView previousSelectedTab;
    private ColorTab selectedTab;
    private int selectedTabIndex;
    /**
     * Sets selected ColorTab width in portrait orientation. Default max tab width is 146dp
     */

    public int selectedTabWidth;

    /**
     * Sets selected ColorTab width in horizontal orientation. Default max tab width is 146dp
     */
    public int selectedTabHorizontalWidth;
    ColorTab colorTab;
    ColorTabView colorTabView;

    /**
     * Sets selected ColorTab by position
     */
    public void selectedTabIndex(int value){
        selectedTabIndex = value;
        select(getTabAt(value));
    }

    /**
     * Sets selected ColorTab
     */
    public void selectedTab(ColorTab value){
        selectedTab = value;
    }
    /**
     * Returns the position of the current selected tab.
     *
     * @return selected tab position, or {@code -1} if there isn't a selected tab.
     */
    public int selectedTabPosition() {
       return internalSelectedTab.position;
    }

    public ColorMatchTabLayout(Context context) {
        this(context,null);
        return;
    }
    public ColorMatchTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ColorMatchTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(attrs, defStyleAttr);
    }

    public void initLayout(AttributeSet attrs, int defStyleAttr) {
        setHorizontalScrollBarEnabled(false);
        tabStripLayout =new SlidingTabStripLayout(getContext());
        super.addView(tabStripLayout, 0,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ColorMatchTabLayout);
        initViewTreeObserver(typedArray);
        selectedTabHorizontalWidth =  getContext().getResources().getDimensionPixelOffset(R.dimen.tab_max_width);
        selectedTabWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.tab_max_width);
        colorTab = new ColorTab();
        colorTabView = new ColorTabView(getContext());
//        if(tabStripLayout.getChildCount() > 0)
//            tabStripLayout.removeAllViews();
    }


    @SuppressLint("ResourceAsColor")
    public void initViewTreeObserver(TypedArray typedArray) {
        if(typedArray.getDimensionPixelSize(R.styleable.ColorMatchTabLayout_selectedTabWidth, INVALID_WIDTH) != INVALID_WIDTH) {
            selectedTabWidth = typedArray.getDimensionPixelSize(R.styleable.ColorMatchTabLayout_selectedTabWidth, INVALID_WIDTH);
        }
        if(typedArray.getDimensionPixelSize(R.styleable.ColorMatchTabLayout_selectedTabHorizontalWidth, INVALID_WIDTH) != INVALID_WIDTH) {
            selectedTabHorizontalWidth = typedArray.getDimensionPixelSize(R.styleable.ColorMatchTabLayout_selectedTabHorizontalWidth, INVALID_WIDTH);
        }
        typedArray.recycle();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int originHeightMeasureSpec) {
        int heightMeasureSpec = originHeightMeasureSpec;
        // If we have a MeasureSpec which allows us to decide our height, try and use the default
        // height
        int idealHeight = (100 + getPaddingTop() + getPaddingBottom());
        switch(MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                 heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)), MeasureSpec.EXACTLY);
                  break;
            case MeasureSpec.UNSPECIFIED:
                 heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, MeasureSpec.EXACTLY);
                break;
        }

        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            // If we don't have an unspecified width spec, use the given size to calculate
            // the max tab width
            WindowManager systemService =( WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int selectTabWidth;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                selectTabWidth = selectedTabWidth;
            else
                selectTabWidth = selectedTabHorizontalWidth;
            int probable = (systemService.getDefaultDisplay().getWidth() - selectTabWidth) / (tabs.size() - 1);
            if (probable < getContext().getResources().getDimensionPixelOffset(R.dimen.default_width))
                tabMaxWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.default_width);
            else
                tabMaxWidth = probable;

        }

        // Now super measure itself using the (possibly) modified height spec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * Method add color tab to this layout. The tab will be added at the end of the list.
     * If this is the first tab to be added it will become the selected tab.
     * @param tab ColorTab to add
     */
    public void addTab(ColorTab tab) {
            tab.setSelected(tabs.isEmpty());
            if (tab.isSelected) {
                internalSelectedTab = tab;
            }
            addColorTabView(tab);

    }

    public void addColorTabView(ColorTab tab) {
        configureTab(tab, tabs.size());
        tabStripLayout.addView(tab.tabView, tab.position ,createLayoutParamsForTabs());

    }

    public ColorTab newTab(){
        ColorTab colorTab = new ColorTab();
        colorTab.tabView = createTabView(colorTab);
        return colorTab;
    }

    public ColorTabView createTabView(ColorTab tab) {
        ColorTabView colorTabView = new ColorTabView(getContext());
        colorTabView.setTab(tab);
        return colorTabView;
    }

    public void configureTab(ColorTab tab, int position) {
        tab.position = position;
        tabs.add(position, tab);

        int count = tabs.size();
        for (int i = position + 1; i < (count - 1); i++) {
            tabs.get(i).position = i;
        }
    }

    public LinearLayout.LayoutParams createLayoutParamsForTabs() {
        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        updateTabViewLayoutParams(lp);
        return lp;
    }

    public void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Returns the number of tabs currently registered in layout.
     *
     *  @return ColorTab count
     */
    public int count() {
        return tabs.size();
    }

    /**
     * Returns the tab at the specified index.
     */
    public ColorTab getTabAt(int index){
        if (index < 0 || index >= count()) {
            return null;
        } else {
            return tabs.get(index);
        }
    }

   public void setScrollPosition(int position, float positionOffset,boolean updateSelectedText) {
        int roundedPosition = Math.round(position + positionOffset);
        if (roundedPosition < 0 || roundedPosition >= tabStripLayout.getChildCount()) {
            return;
        }

        // Update the 'selected state' view as we scroll, if enabled
        if (updateSelectedText) {
            setSelectedTabView(roundedPosition);
        }
    }

    /**
//     * Add {@link OnColorTabSelectedListener}
//     * @param tabSelectedListener listener to add
     */
    public void addOnColorTabSelectedListener(OnColorTabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }

    private void setSelectedTabView(int position) {
        int tabCount = tabStripLayout.getChildCount();
        if (position < tabCount) {
            int i = 0;
            while(tabCount - 1 != i)
            {
                View child = tabStripLayout.getChildAt(i);
                if(i == position)
                child.setSelected(true);
                else
                child.setSelected(false);
                i++;
            }
        }
    }

    public void select(ColorTab colorTab) {
        if (colorTab == internalSelectedTab) {
            return;
        } else {
            previousSelectedTab = getSelectedTabView();
            internalSelectedTab.setSelected(false);
            tabSelectedListener.onUnselectedTab(internalSelectedTab);
            internalSelectedTab = colorTab;
            internalSelectedTab.setSelected(true);
            tabSelectedListener.onSelectedTab(colorTab);
        }
    }

   public ColorTabView getSelectedTabView(){
//        internalSelectedTab.position = 0;
        return (ColorTabView) tabStripLayout.getChildAt(internalSelectedTab.position);
    }

}
