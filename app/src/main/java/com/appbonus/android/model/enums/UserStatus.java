package com.appbonus.android.model.enums;

import com.google.gson.annotations.SerializedName;

public enum UserStatus {
    @SerializedName("active")
    ACTIVE,
    @SerializedName("leery")
    LEERY,
    @SerializedName("banned")
    BANNED
}
