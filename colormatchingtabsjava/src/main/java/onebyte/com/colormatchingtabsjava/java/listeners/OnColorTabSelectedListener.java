package onebyte.com.colormatchingtabsjava.java.listeners;


import onebyte.com.colormatchingtabsjava.java.model.ColorTab;

public interface OnColorTabSelectedListener {
    void onSelectedTab(ColorTab tab);

    /**
     * Called when a tab exits the selected state.
     *
     */
    void onUnselectedTab(ColorTab tab);
}
