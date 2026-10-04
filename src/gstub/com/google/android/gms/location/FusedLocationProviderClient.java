package com.google.android.gms.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

/*
 * Play's Fused Location client, implemented on top of the platform's own
 * android.location.LocationManager. In ATL that manager is backed by the XDG
 * location portal over D-Bus (see android/location/LocationManager.java, whose
 * best provider is "xdgportal"), so this is the concrete binding of a Google
 * Play API onto a built-in library API and, through it, onto a Freedesktop
 * service -- there is no second location stack.
 *
 * The fused provider's "priority" has no analogue here (there is one provider),
 * so only the request interval is carried through.
 */
public final class FusedLocationProviderClient {
	private static final String TAG = "ATLFusedLocation";

	private final LocationManager lm;
	// A Play LocationCallback is removed by identity, so keep the platform
	// listener each one was adapted to.
	private final Map<LocationCallback, LocationListener> adapters = new IdentityHashMap<>();

	FusedLocationProviderClient(Context context) {
		this.lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	private String provider() {
		return lm.getBestProvider(new Criteria(), true);
	}

	public Task<Location> getLastLocation() {
		// May legitimately be null before the first fix; that matches the real
		// contract, where apps must null-check getLastLocation().
		return Tasks.forResult(lm.getLastKnownLocation(provider()));
	}

	public Task<Void> requestLocationUpdates(LocationRequest request,
	                                         final LocationCallback callback,
	                                         Looper looper) {
		LocationListener adapter = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				callback.onLocationResult(LocationResult.create(Arrays.asList(location)));
			}
		};
		synchronized (adapters) {
			adapters.put(callback, adapter);
		}
		long interval = request != null ? request.getInterval() : 0;
		lm.requestLocationUpdates(provider(), interval, 0f, adapter);
		callback.onLocationAvailability(new LocationAvailability(true));
		return Tasks.forResult(null);
	}

	public Task<Void> removeLocationUpdates(LocationCallback callback) {
		LocationListener adapter;
		synchronized (adapters) {
			adapter = adapters.remove(callback);
		}
		if (adapter != null) {
			lm.removeUpdates(adapter);
		} else {
			Log.w(TAG, "removeLocationUpdates: no registered callback");
		}
		return Tasks.forResult(null);
	}

	public Task<Void> flushLocations() {
		return Tasks.forResult(null);
	}
}
