package com.dolphin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface StandardFragment {
    Fragment placeProperFragment(String fragmentTag);

    Fragment placeProperFragment(String fragmentTag, Bundle args);

    Fragment placeProperFragment(String fragmentTag, Bundle args, boolean addToBackStackCustom, Fragment targetFragment);

    boolean closeCurrentFragment();
}
