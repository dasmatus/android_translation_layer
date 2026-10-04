package com.google.android.play.core.integrity;

import com.google.android.gms.tasks.Task;

/*
 * The newer "standard request" Integrity API. Its request/response/provider
 * types are nested inside this interface in the real library, so they are
 * nested here too -- an app compiled against Play Integrity references them by
 * the names StandardIntegrityManager$PrepareIntegrityTokenRequest and so on,
 * and those names have to match for its classes to resolve.
 *
 * Every token this path produces is the same software-unverified payload the
 * classic path produces; see SoftwareVerdict for the honest limits.
 */
public interface StandardIntegrityManager {
	Task<StandardIntegrityTokenProvider> prepareIntegrityToken(PrepareIntegrityTokenRequest request);

	abstract class PrepareIntegrityTokenRequest {
		public abstract Long getCloudProjectNumber();

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder {
			private Long cloudProjectNumber;

			public Builder setCloudProjectNumber(long cloudProjectNumber) {
				this.cloudProjectNumber = cloudProjectNumber;
				return this;
			}

			public PrepareIntegrityTokenRequest build() {
				final Long fCloud = cloudProjectNumber;
				return new PrepareIntegrityTokenRequest() {
					@Override
					public Long getCloudProjectNumber() {
						return fCloud;
					}
				};
			}
		}
	}

	abstract class StandardIntegrityTokenRequest {
		public abstract String requestHash();

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder {
			private String requestHash;

			public Builder setRequestHash(String requestHash) {
				this.requestHash = requestHash;
				return this;
			}

			public StandardIntegrityTokenRequest build() {
				final String fHash = requestHash;
				return new StandardIntegrityTokenRequest() {
					@Override
					public String requestHash() {
						return fHash;
					}
				};
			}
		}
	}

	abstract class StandardIntegrityToken {
		public abstract String token();

		public abstract int showDialog(android.app.Activity activity, int requestCode);
	}

	interface StandardIntegrityTokenProvider {
		Task<StandardIntegrityToken> request(StandardIntegrityTokenRequest request);
	}
}
