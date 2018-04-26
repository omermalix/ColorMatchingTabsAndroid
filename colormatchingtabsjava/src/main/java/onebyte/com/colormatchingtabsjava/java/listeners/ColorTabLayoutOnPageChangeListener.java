package onebyte.com.colortabs.listeners;

import android.support.v4.view.ViewPager;

import java.lang.ref.WeakReference;

import onebyte.com.colortabs.colortabs.ColorMatchTabLayout;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class ColorTabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private int previousScrollState = 0;
    private int scrollState  = 0;
    private ColorMatchTabLayout colorTabLayout;
    private WeakReference<ColorMatchTabLayout> tabLayoutReference;

    public ColorTabLayoutOnPageChangeListener(ColorMatchTabLayout colorTabLayout)
    {
        this.colorTabLayout = colorTabLayout;
        tabLayoutReference = new WeakReference(colorTabLayout);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ColorMatchTabLayout tabLayout = tabLayoutReference.get();
        boolean updateIndicator = !(scrollState == SCROLL_STATE_SETTLING && previousScrollState == SCROLL_STATE_IDLE);
        tabLayout.setScrollPosition(position, positionOffset, updateIndicator);
    }

    @Override
    public void onPageSelected(int position) {
        ColorMatchTabLayout tabLayout = tabLayoutReference.get();
        if (!tabLayout.tabStripLayout.isAnimate) {
            tabLayout.select(tabLayout.getTabAt(position));
            tabLayout.getSelectedTabView().clickedTabView = tabLayout.getSelectedTabView();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        previousScrollState = scrollState;
        scrollState = state;
    }
}
