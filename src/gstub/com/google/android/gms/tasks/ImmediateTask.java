package com.google.android.gms.tasks;

import java.util.concurrent.Executor;

/*
 * A Task that is already finished the moment it is handed out, carrying either
 * a result or an exception. Listeners run synchronously on the thread that
 * attaches them; the optional Executor overloads defer to it so callers that
 * pass their own executor keep their threading expectations. This is all the
 * concurrency the shim needs, because none of its producers do real work.
 */
final class ImmediateTask<TResult> extends Task<TResult> {
	private final TResult result;
	private final Exception exception;

	private ImmediateTask(TResult result, Exception exception) {
		this.result = result;
		this.exception = exception;
	}

	static <T> ImmediateTask<T> forResult(T result) {
		return new ImmediateTask<>(result, null);
	}

	static <T> ImmediateTask<T> forException(Exception e) {
		return new ImmediateTask<>(null, e);
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public boolean isSuccessful() {
		return exception == null;
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public TResult getResult() {
		if (exception != null) {
			throw new RuntimeExecutionException(exception);
		}
		return result;
	}

	@Override
	public Exception getException() {
		return exception;
	}

	@Override
	public Task<TResult> addOnSuccessListener(OnSuccessListener<? super TResult> listener) {
		if (exception == null) {
			listener.onSuccess(result);
		}
		return this;
	}

	@Override
	public Task<TResult> addOnSuccessListener(Executor executor, OnSuccessListener<? super TResult> listener) {
		if (exception == null) {
			executor.execute(() -> listener.onSuccess(result));
		}
		return this;
	}

	@Override
	public Task<TResult> addOnFailureListener(OnFailureListener listener) {
		if (exception != null) {
			listener.onFailure(exception);
		}
		return this;
	}

	@Override
	public Task<TResult> addOnFailureListener(Executor executor, OnFailureListener listener) {
		if (exception != null) {
			executor.execute(() -> listener.onFailure(exception));
		}
		return this;
	}

	@Override
	public Task<TResult> addOnCompleteListener(OnCompleteListener<TResult> listener) {
		listener.onComplete(this);
		return this;
	}

	@Override
	public Task<TResult> addOnCompleteListener(Executor executor, OnCompleteListener<TResult> listener) {
		executor.execute(() -> listener.onComplete(this));
		return this;
	}
}
