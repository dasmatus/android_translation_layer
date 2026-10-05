package com.google.android.gms.common;

/* The connection-result codes apps compare against. Only SUCCESS is ever
 * returned by the shim's availability check; the rest exist so switch/if
 * statements over them compile and resolve. */
public final class ConnectionResult {
	public static final int SUCCESS = 0;
	public static final int SERVICE_MISSING = 1;
	public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
	public static final int SERVICE_DISABLED = 3;
	public static final int SIGN_IN_REQUIRED = 4;
	public static final int INVALID_ACCOUNT = 5;
	public static final int RESOLUTION_REQUIRED = 6;
	public static final int NETWORK_ERROR = 7;
	public static final int INTERNAL_ERROR = 8;
	public static final int SERVICE_INVALID = 9;
	public static final int API_UNAVAILABLE = 16;
	public static final int SERVICE_UPDATING = 18;

	private final int statusCode;

	public ConnectionResult(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getErrorCode() {
		return statusCode;
	}

	public boolean isSuccess() {
		return statusCode == SUCCESS;
	}
}
