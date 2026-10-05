package com.google.android.gms.common;

import android.content.Context;

/*
 * Reports the stubbed Play services as present. The subset of GMS this layer
 * provides (Integrity, ProviderInstaller, ...) is served locally, so an app
 * asking "are Play services available?" is told yes and goes on to call them;
 * each call is then handled by its own shim. APIs with no shim resolve as
 * absent at the point of use, which is the normal GMS degradation path.
 */
public class GoogleApiAvailabilityLight {
	private static final GoogleApiAvailabilityLight INSTANCE = new GoogleApiAvailabilityLight();

	public static GoogleApiAvailabilityLight getInstance() {
		return INSTANCE;
	}

	public int isGooglePlayServicesAvailable(Context context) {
		return ConnectionResult.SUCCESS;
	}

	public int getApkVersion(Context context) {
		// A plausible recent Play services version code; some apps compare it.
		return 240000000;
	}

	public String getErrorString(int errorCode) {
		return errorCode == ConnectionResult.SUCCESS ? "SUCCESS" : "ERROR(" + errorCode + ")";
	}
}
