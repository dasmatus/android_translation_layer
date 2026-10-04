package com.google.android.gms.location;

import android.location.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* The batch of fixes delivered to a LocationCallback. The platform delivers one
 * fix at a time, so the shim's results hold a single location. */
public final class LocationResult {
	private final List<Location> locations;

	private LocationResult(List<Location> locations) {
		this.locations = locations;
	}

	public static LocationResult create(List<Location> locations) {
		return new LocationResult(new ArrayList<>(locations));
	}

	public Location getLastLocation() {
		return locations.isEmpty() ? null : locations.get(locations.size() - 1);
	}

	public List<Location> getLocations() {
		return Collections.unmodifiableList(locations);
	}
}
