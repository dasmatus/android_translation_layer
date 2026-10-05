package com.google.android.play.core.integrity;

import com.google.android.gms.tasks.Task;

public abstract class IntegrityTokenResponse {
	public abstract String token();

	/* Real responses can show Google's remediation dialog and complete with the
	 * dialog's result code; there is nothing to remediate here, so the shim
	 * completes at once with 0. The return type is Task<Integer>, as in the
	 * real library: the descriptor is what an app's bytecode links against. */
	public abstract Task<Integer> showDialog(android.app.Activity activity, int requestCode);
}
