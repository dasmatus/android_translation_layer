package com.google.android.gms.location;

/* Play's LocationRequest, both the legacy setter form and the newer Builder.
 * Only the interval is actually used by the shim (to drive the platform
 * LocationManager); priority is carried for API completeness. */
public final class LocationRequest {
	public static final int PRIORITY_HIGH_ACCURACY = Priority.PRIORITY_HIGH_ACCURACY;
	public static final int PRIORITY_BALANCED_POWER_ACCURACY = Priority.PRIORITY_BALANCED_POWER_ACCURACY;
	public static final int PRIORITY_LOW_POWER = Priority.PRIORITY_LOW_POWER;
	public static final int PRIORITY_NO_POWER = Priority.PRIORITY_PASSIVE;

	private long intervalMillis = 3600000;
	private int priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY;

	public static LocationRequest create() {
		return new LocationRequest();
	}

	public LocationRequest setInterval(long millis) {
		this.intervalMillis = millis;
		return this;
	}

	public LocationRequest setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public long getInterval() {
		return intervalMillis;
	}

	public int getPriority() {
		return priority;
	}

	public static Builder builder(int priority, long intervalMillis) {
		return new Builder(priority, intervalMillis);
	}

	public static final class Builder {
		private long intervalMillis;
		private int priority;

		public Builder(int priority, long intervalMillis) {
			this.priority = priority;
			this.intervalMillis = intervalMillis;
		}

		public Builder setIntervalMillis(long millis) {
			this.intervalMillis = millis;
			return this;
		}

		public Builder setPriority(int priority) {
			this.priority = priority;
			return this;
		}

		public LocationRequest build() {
			LocationRequest r = new LocationRequest();
			r.intervalMillis = intervalMillis;
			r.priority = priority;
			return r;
		}
	}
}
