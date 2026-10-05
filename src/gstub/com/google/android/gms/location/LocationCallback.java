package com.google.android.gms.location;

/* The callback an app registers for ongoing fixes. Both methods default to
 * no-ops so an app only overriding one still compiles and runs. */
public abstract class LocationCallback {
	public void onLocationResult(LocationResult result) {}

	public void onLocationAvailability(LocationAvailability availability) {}
}
