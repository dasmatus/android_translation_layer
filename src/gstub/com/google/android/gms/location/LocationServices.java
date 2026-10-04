package com.google.android.gms.location;

import android.content.Context;

/* The static entry point apps use to obtain the fused client. */
public final class LocationServices {
	private LocationServices() {}

	public static FusedLocationProviderClient getFusedLocationProviderClient(Context context) {
		return new FusedLocationProviderClient(context);
	}
}
