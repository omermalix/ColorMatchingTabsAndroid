package onebyte.com.colortabs.listeners;


import onebyte.com.colortabs.model.ColorTab;

public interface OnColorTabSelectedListener {
    void onSelectedTab(ColorTab tab);

    /**
     * Called when a tab exits the selected state.
     *
     */
    void onUnselectedTab(ColorTab tab);
}
