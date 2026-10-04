package com.google.android.gms.tasks;

import java.util.concurrent.Executor;

/*
 * Minimal re-implementation of Play services' Task<T>. Enough of the surface
 * that apps using the common "requestX().addOnSuccessListener(...)" pattern
 * keep working. The shim only ever produces tasks that are already complete
 * (see Tasks), so every listener fires synchronously at attach time.
 */
public abstract class Task<TResult> {
	public abstract boolean isComplete();

	public abstract boolean isSuccessful();

	public abstract boolean isCanceled();

	public abstract TResult getResult();

	public abstract Exception getException();

	public abstract Task<TResult> addOnSuccessListener(OnSuccessListener<? super TResult> listener);

	public abstract Task<TResult> addOnSuccessListener(Executor executor, OnSuccessListener<? super TResult> listener);

	public abstract Task<TResult> addOnFailureListener(OnFailureListener listener);

	public abstract Task<TResult> addOnFailureListener(Executor executor, OnFailureListener listener);

	public abstract Task<TResult> addOnCompleteListener(OnCompleteListener<TResult> listener);

	public abstract Task<TResult> addOnCompleteListener(Executor executor, OnCompleteListener<TResult> listener);
}
