package onebyte.com.colortabs.adapter;

import android.graphics.drawable.Drawable;

import onebyte.com.colortabs.colortabs.ColorMatchTabLayout;
import onebyte.com.colortabs.model.ColorTab;


public class ColorTabAdapter {

    public ColorTab createColorTab(ColorMatchTabLayout tabLayout, String text, int color, Drawable icon)  {
        ColorTab colorTab = tabLayout.newTab();
        colorTab.text = text;
        colorTab.selectedColor = color;
        colorTab.setIcon(icon);
        return colorTab;
    }
}
