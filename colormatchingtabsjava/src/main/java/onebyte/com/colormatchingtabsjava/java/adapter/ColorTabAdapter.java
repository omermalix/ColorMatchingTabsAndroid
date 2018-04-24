package onebyte.com.colormatchingtabsjava.java.adapter;

import android.graphics.drawable.Drawable;

import onebyte.com.colormatchingtabsjava.java.colortabs.ColorMatchTabLayout;
import onebyte.com.colormatchingtabsjava.java.model.ColorTab;


public class ColorTabAdapter {

    public ColorTab createColorTab(ColorMatchTabLayout tabLayout, String text, int color, Drawable icon)  {
        ColorTab colorTab = tabLayout.newTab();
        colorTab.text = text;
        colorTab.selectedColor = color;
        colorTab.setIcon(icon);
        return colorTab;
    }
}
