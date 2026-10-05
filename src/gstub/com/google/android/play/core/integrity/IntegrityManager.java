package com.google.android.play.core.integrity;

import com.google.android.gms.tasks.Task;

/* Classic Integrity API entry point: requestIntegrityToken(...). */
public interface IntegrityManager {
	Task<IntegrityTokenResponse> requestIntegrityToken(IntegrityTokenRequest request);
}
