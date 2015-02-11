package com.dolphin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface StandardFragment {
    void placeProperFragment(String fragmentTag);

    void placeProperFragment(String fragmentTag, Bundle args);

    void placeProperFragment(String fragmentTag, Bundle args, boolean addToBackStackCustom, Fragment targetFragment);

    boolean closeCurrentFragment();
}
