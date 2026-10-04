# gstub — Google-facing compatibility shims

`gstub.jar` is a small jar of stand-in classes for Google libraries that Android
apps commonly link against but that are not part of AOSP. ATL loads it so that
an app referencing these classes resolves them and keeps running instead of
crashing with a `ClassNotFoundException`. This is the same approach microG takes:
provide the API surface locally, served by whatever the host can actually do.

## What is stubbed today

- `com.google.android.gms.providerinstaller.ProviderInstallerImpl` — no-op; ATL's
  TLS already comes from the host.
- `com.google.android.gms.tasks.*` — a minimal `Task<T>` and `Tasks`. The shim's
  producers only ever return already-complete tasks. As in Play, a listener
  added without an executor runs on the main thread; one added with an executor
  runs on it.
- `com.google.android.gms.common.*` — Play services availability. The locally
  served subset is reported as present (`ConnectionResult.SUCCESS`); an app then
  calls into it and each call is handled by its own shim. An API with no shim
  resolves as absent at the point of use, the normal GMS degradation path.
- `com.google.android.play.core.integrity.*` — the Play Integrity API, both the
  classic `IntegrityManager` and the newer `StandardIntegrityManager`.
- `com.google.android.gms.location.*` — the Fused Location client, implemented
  on the platform `android.location.LocationManager`, which ATL backs with the
  XDG location portal over D-Bus. One location stack: a Play API bound onto the
  built-in library API and, through it, onto a Freedesktop service.

## Play Integrity: what it can and cannot satisfy

Play Integrity exists so an app (and usually its server) can ask Google whether
the app binary, the device, and the user's licensing look genuine. The answer is
a token that **only Google's servers mint and decode**, signed with keys the
device never holds.

This shim does **not** have those keys and does **not** forge them. It returns a
well-formed but **unsigned, self-describing** token that states plainly it came
from the Android Translation Layer and that the device is unverified
(`verdictSource: "software-unverified"`, empty `deviceRecognitionVerdict`). It is
a single base64url segment, deliberately **not** the `header.payload.signature`
shape of a real signed token, so nothing can mistake it for a Google verdict.

Concretely:

- **Satisfied:** apps that only need the Integrity API to exist and return a
  non-empty token — a lenient client-side check, or a token the app fetches but
  never actually verifies server-side. These keep working.
- **Not satisfied, by design:** apps whose backend sends the token to Google's
  Play Integrity decode endpoint. Google will reject an unsigned non-Google
  token. That is the correct result for an unverified, non-Google device, and no
  local shim can change it without forging Google's signing keys.

We deliberately do **not** pursue hardware-backed "strong" integrity, leaked or
third-party keyboxes, or anything else that would forge a genuine-device
attestation. The goal here is interoperability for apps running on a de-Googled
compatibility layer, not defeating an app's server-side anti-abuse checks.
