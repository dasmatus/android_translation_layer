package com.google.android.gms.location;

/* Whether location is currently obtainable. The platform resolves this through
 * the XDG portal, which may prompt; the shim optimistically reports available
 * and lets an actual request surface any real failure. */
public final class LocationAvailability {
	private final boolean available;

	public LocationAvailability(boolean available) {
		this.available = available;
	}

	public boolean isLocationAvailable() {
		return available;
	}
}
