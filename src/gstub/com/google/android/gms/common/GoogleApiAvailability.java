package com.google.android.gms.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;

/* The full GoogleApiAvailability surface, extending the light variant. All the
 * dialog/resolution entry points are no-ops because there is nothing to
 * resolve: availability is always reported as SUCCESS. */
public final class GoogleApiAvailability extends GoogleApiAvailabilityLight {
	private static final GoogleApiAvailability INSTANCE = new GoogleApiAvailability();

	public static GoogleApiAvailability getInstance() {
		return INSTANCE;
	}

	public boolean isUserResolvableError(int errorCode) {
		return false;
	}

	public Dialog getErrorDialog(Activity activity, int errorCode, int requestCode) {
		return null;
	}

	public Dialog getErrorDialog(Activity activity, int errorCode, int requestCode,
	                             DialogInterface.OnCancelListener cancelListener) {
		return null;
	}

	public Intent getErrorResolutionIntent(Context context, int errorCode, String tag) {
		return null;
	}

	public void showErrorNotification(Context context, int errorCode) {}
}
