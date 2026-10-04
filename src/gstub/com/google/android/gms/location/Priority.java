package com.google.android.gms.location;

/* Play's newer priority constants. The shim maps all of them onto the single
 * provider the platform LocationManager offers (the XDG location portal), so
 * the value only affects how often updates are requested, not which hardware
 * is used. */
public final class Priority {
	public static final int PRIORITY_HIGH_ACCURACY = 100;
	public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
	public static final int PRIORITY_LOW_POWER = 104;
	public static final int PRIORITY_PASSIVE = 105;

	private Priority() {}
}
