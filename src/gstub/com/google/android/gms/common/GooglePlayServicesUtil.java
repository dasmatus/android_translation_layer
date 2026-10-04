package com.google.android.gms.common;

import android.content.Context;

/* The old static-method API, superseded by GoogleApiAvailability but still
 * referenced by older apps. Mirrors the same always-available answer. */
public final class GooglePlayServicesUtil {
	private GooglePlayServicesUtil() {}

	public static int isGooglePlayServicesAvailable(Context context) {
		return ConnectionResult.SUCCESS;
	}

	public static boolean isUserRecoverableError(int errorCode) {
		return false;
	}

	public static String getErrorString(int errorCode) {
		return GoogleApiAvailability.getInstance().getErrorString(errorCode);
	}
}
