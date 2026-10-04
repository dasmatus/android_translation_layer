package com.google.android.play.core.integrity;

import android.content.Intent;

public abstract class IntegrityTokenResponse {
	public abstract String token();

	/* Real responses can hand back an Intent to show Google's remediation
	 * dialog; there is nothing to remediate here, so this is always 0/null. */
	public abstract int showDialog(android.app.Activity activity, int requestCode);
}
