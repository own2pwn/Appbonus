package com.appbonus.android.model.enums;

import com.google.gson.annotations.SerializedName;

public enum Sex {
    UNKNOWN,
    @SerializedName("male")
    MALE,
    @SerializedName("female")
    FEMALE
}
