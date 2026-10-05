package com.google.android.play.core.integrity;

/* Classic (one-shot) Integrity API request. */
public abstract class IntegrityTokenRequest {
	public abstract String nonce();

	public abstract Long cloudProjectNumber();

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String nonce;
		private Long cloudProjectNumber;

		public Builder setNonce(String nonce) {
			this.nonce = nonce;
			return this;
		}

		public Builder setCloudProjectNumber(long cloudProjectNumber) {
			this.cloudProjectNumber = cloudProjectNumber;
			return this;
		}

		public IntegrityTokenRequest build() {
			final String fNonce = nonce;
			final Long fCloud = cloudProjectNumber;
			return new IntegrityTokenRequest() {
				@Override
				public String nonce() {
					return fNonce;
				}

				@Override
				public Long cloudProjectNumber() {
					return fCloud;
				}
			};
		}
	}
}
