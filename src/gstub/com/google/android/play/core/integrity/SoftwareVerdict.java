package com.google.android.play.core.integrity;

import android.util.Base64;
import java.nio.charset.StandardCharsets;

/*
 * Builds the opaque "token" string that the Integrity shim hands back.
 *
 * Honest by construction. A real Play Integrity token is a nested JWE/JWS that
 * only Google's servers can mint and decode, because the verdict is signed with
 * keys the device never holds. This shim does not have those keys and does not
 * forge them, so it cannot produce a token that Google Play's decode endpoint
 * will accept. What it produces instead is a self-describing, UNSIGNED payload
 * that states plainly it came from the Android Translation Layer and that the
 * device is unverified.
 *
 * The effect: apps that only need the Integrity API to exist and return a
 * non-empty token (a lenient client-side check, or a token they never actually
 * verify server-side) keep working. Apps whose backend sends the token to
 * Google for decoding will see it rejected -- that is the correct outcome for
 * an unverified, non-Google device, and it is not something a local shim can
 * change without forging Google's signing keys, which this project refuses to
 * do.
 */
final class SoftwareVerdict {
	private SoftwareVerdict() {}

	static String build(String packageName, byte[] requestHashOrNonce) {
		long now = System.currentTimeMillis();
		StringBuilder json = new StringBuilder();
		json.append('{');
		json.append("\"issuer\":\"android_translation_layer\",");
		// Named so anything that does parse it cannot mistake it for a Google verdict.
		json.append("\"verdictSource\":\"software-unverified\",");
		json.append("\"appIntegrity\":{\"appRecognitionVerdict\":\"UNEVALUATED\",");
		json.append("\"packageName\":\"").append(escape(packageName)).append("\"},");
		json.append("\"deviceIntegrity\":{\"deviceRecognitionVerdict\":[]},");
		json.append("\"accountDetails\":{\"appLicensingVerdict\":\"UNEVALUATED\"},");
		json.append("\"requestDetails\":{\"timestampMillis\":").append(now);
		if (requestHashOrNonce != null) {
			json.append(",\"request\":\"")
			    .append(Base64.encodeToString(requestHashOrNonce, Base64.NO_WRAP | Base64.URL_SAFE))
			    .append('"');
		}
		json.append("}}");
		// Single base64url segment, deliberately NOT the dotted header.payload.signature
		// shape of a JWS, so it is never mistaken for a signed Google token.
		return Base64.encodeToString(json.toString().getBytes(StandardCharsets.UTF_8),
		                             Base64.NO_WRAP | Base64.URL_SAFE);
	}

	private static String escape(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
