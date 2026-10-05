package com.google.android.gms.tasks;

/* The subset of com.google.android.gms.tasks.Tasks the shim's producers use to
 * hand back already-complete tasks. */
public final class Tasks {
	private Tasks() {}

	public static <TResult> Task<TResult> forResult(TResult result) {
		return ImmediateTask.forResult(result);
	}

	public static <TResult> Task<TResult> forException(Exception e) {
		return ImmediateTask.forException(e);
	}

	public static <TResult> TResult await(Task<TResult> task) throws Exception {
		if (task.getException() != null) {
			throw task.getException();
		}
		return task.getResult();
	}
}
