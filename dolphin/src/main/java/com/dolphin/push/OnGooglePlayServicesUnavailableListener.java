package com.dolphin.push;

public interface OnGooglePlayServicesUnavailableListener {
    /*
        GooglePlayServicesUtil.getErrorDialog(resultCode, context,
            GoogleCloudMessagingUtils.PLAY_SERVICES_RESOLUTION_REQUEST).show();
    */
    void onGooglePlayServicesUnavailable(int resultCode);
}
