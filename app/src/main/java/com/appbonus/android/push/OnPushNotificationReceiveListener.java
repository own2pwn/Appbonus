/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.appbonus.android.push;

import android.os.Bundle;

public interface OnPushNotificationReceiveListener {
    boolean onPushReceive(Bundle extras);
}
