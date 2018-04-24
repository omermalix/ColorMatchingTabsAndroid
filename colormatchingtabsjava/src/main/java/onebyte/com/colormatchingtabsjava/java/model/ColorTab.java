package onebyte.com.colormatchingtabsjava.java.model;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import onebyte.com.colormatchingtabsjava.java.colortabs.ColorTabView;


public class ColorTab {
    public static int INVALID_POSITION = -1;
    public ColorTabView tabView = null;

    public Drawable icon = null;

    public void setIcon(Drawable icon) {
        this.icon = icon;
        tabView.updateView();
    }
    public CharSequence text = "";
    public int selectedColor = Color.GREEN;
    public int position = INVALID_POSITION;
    public boolean isSelected = false;
    public  void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        tabView.updateView();
    }

}