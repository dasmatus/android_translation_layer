package com.google.android.play.core.integrity;

/* Surfaced the way the real library surfaces failures. The shim does not throw
 * it in normal operation, but apps catch it, so the type must resolve. */
public class IntegrityServiceException extends Exception {
	private final int errorCode;

	public IntegrityServiceException(int errorCode, Throwable cause) {
		super("Integrity error " + errorCode, cause);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
