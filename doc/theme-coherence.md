# Theme coherence with the desktop

ATL renders Android apps as GTK4 windows. "Theme coherence" means those windows
pick up the host desktop's look — its light/dark mode and its palette/accent —
so an Android app does not stand out against native apps. This is written for
coherence with derisk (the LosOS Desktop shell), but every mechanism here is a
Freedesktop standard, so it works under any desktop that implements them.

There are two layers, and ATL consumes both through standard interfaces rather
than anything derisk-specific:

## 1. Light/dark — already handled, no ATL code

GTK4 reads the `color-scheme` key of the `org.freedesktop.appearance` namespace
from the `org.freedesktop.portal.Settings` D-Bus interface and flips
`gtk-application-prefer-dark-theme` to match, with no action from the
application. LosOS Desktop enables a settings portal, so an ATL app already
follows the desktop between light and dark. Nothing to do; noted so it is not
re-implemented.

## 2. Palette / accent — the small interface ATL consumes

For the richer coherence (accent colour, surface colours matching derisk's
theme), ATL takes a CSS layer from the desktop. There are two ways in, in
priority order; the first the desktop provides wins:

### a. File-backed (implemented)

If the environment variable `DERISK_THEME_CSS` names a readable CSS file, ATL
loads it as a `GtkCssProvider` at `GTK_STYLE_PROVIDER_PRIORITY_USER` — above
ATL's own default stylesheet, below an app's inline styling
(`src/main-executable/main.c`). The desktop writes its current theme to that
file as GTK CSS (for example `@define-color` accent definitions and
`window { background-color: … }`) and points ATL at it. This is deliberately
the lowest-coupling option: a plain file, no new D-Bus surface, re-read on each
app launch.

### b. D-Bus appearance portal (planned)

The standard place for a desktop to publish an accent colour is the
`accent-color` key of the same `org.freedesktop.appearance` namespace on
`org.freedesktop.portal.Settings`, with a `SettingChanged` signal on change.
When the derisk theming engine publishes there, ATL should read `accent-color`
at startup, subscribe to `SettingChanged`, and translate the value into the
same CSS layer as (a) so a live theme change restyles running apps. ATL already
generates D-Bus proxies with `gnome.gdbus_codegen` for its portals, so this
follows the existing pattern.

## Open item

The exact palette keys derisk's theming engine will expose (accent only, or a
fuller set of role colours, and under which interface) are being settled with
the theming-engine thread. Option (a) is live now and needs only a file; option
(b) is the standards-track path and will be wired once that interface is fixed.
Until then ATL stays coherent on light/dark and on whatever CSS the desktop
chooses to hand it through `DERISK_THEME_CSS`.
