package com.google.android.gms.location;

import android.app.Activity;
import android.content.Context;

/* The static entry point apps use to obtain the fused client. Play exposes
 * both a Context and an Activity overload; bytecode records whichever the app
 * compiled against, so both have to exist. */
public final class LocationServices {
	private LocationServices() {}

	public static FusedLocationProviderClient getFusedLocationProviderClient(Context context) {
		return new FusedLocationProviderClient(context);
	}

	public static FusedLocationProviderClient getFusedLocationProviderClient(Activity activity) {
		return new FusedLocationProviderClient(activity);
	}
}
