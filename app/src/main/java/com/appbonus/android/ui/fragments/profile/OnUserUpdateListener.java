package com.appbonus.android.ui.fragments.profile;

import com.appbonus.android.model.User;

public interface OnUserUpdateListener {
    void onUpdate(User user, boolean reloadUI);
}
