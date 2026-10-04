package com.google.android.play.core.integrity;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

/*
 * Hands out the shim's IntegrityManager. The returned manager always succeeds
 * with a software-unverified token (see SoftwareVerdict for exactly what that
 * does and does not satisfy).
 */
public final class IntegrityManagerFactory {
	private static final String TAG = "ATLIntegrity";

	private IntegrityManagerFactory() {}

	public static StandardIntegrityManager createStandard(final Context context) {
		final String packageName = context != null ? context.getPackageName() : "";
		return new StandardIntegrityManager() {
			@Override
			public Task<StandardIntegrityTokenProvider> prepareIntegrityToken(PrepareIntegrityTokenRequest request) {
				Log.i(TAG, "prepareIntegrityToken: software-unverified provider "
				    + "(tokens are not Google-signed; server-side decode will reject them)");
				StandardIntegrityTokenProvider provider = new StandardIntegrityTokenProvider() {
					@Override
					public Task<StandardIntegrityToken> request(StandardIntegrityTokenRequest req) {
						byte[] reqHash = req != null && req.requestHash() != null
						    ? req.requestHash().getBytes() : null;
						final String token = SoftwareVerdict.build(packageName, reqHash);
						return Tasks.forResult(new StandardIntegrityToken() {
							@Override
							public String token() {
								return token;
							}

							@Override
							public Task<Integer> showDialog(android.app.Activity activity, int requestCode) {
								return Tasks.forResult(0);
							}
						});
					}
				};
				return Tasks.forResult(provider);
			}
		};
	}

	public static IntegrityManager create(final Context context) {
		final String packageName = context != null ? context.getPackageName() : "";
		return new IntegrityManager() {
			@Override
			public Task<IntegrityTokenResponse> requestIntegrityToken(IntegrityTokenRequest request) {
				Log.i(TAG, "requestIntegrityToken: returning software-unverified token "
				    + "(not a Google-signed verdict; server-side decode will reject it)");
				byte[] nonce = null;
				if (request != null && request.nonce() != null) {
					try {
						nonce = Base64.decode(request.nonce(), Base64.DEFAULT);
					} catch (IllegalArgumentException e) {
						nonce = request.nonce().getBytes();
					}
				}
				final String token = SoftwareVerdict.build(packageName, nonce);
				return Tasks.forResult(new IntegrityTokenResponse() {
					@Override
					public String token() {
						return token;
					}

					@Override
					public Task<Integer> showDialog(android.app.Activity activity, int requestCode) {
						return Tasks.forResult(0);
					}
				});
			}
		};
	}
}
