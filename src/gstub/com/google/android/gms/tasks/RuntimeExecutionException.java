package com.google.android.gms.tasks;

/* Thrown by Task.getResult() when the task carries an exception, matching the
 * Play services contract so callers that catch it keep working. */
public class RuntimeExecutionException extends RuntimeException {
	public RuntimeExecutionException(Throwable cause) {
		super(cause);
	}
}
