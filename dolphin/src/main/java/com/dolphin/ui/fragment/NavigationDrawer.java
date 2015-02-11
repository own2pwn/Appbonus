package com.dolphin.ui.fragment;

import android.support.v4.widget.DrawerLayout;

public interface NavigationDrawer {
    public static interface NavigationDrawerCallbacks {
        void onNavigationItemSelected(int position);
    }

    boolean isDrawerOpen();
    void toggle();
    void setUp(int fragmentId, DrawerLayout drawerLayout);
    void repaint();
}
