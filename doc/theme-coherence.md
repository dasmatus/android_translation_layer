# Theme coherence with the desktop

ATL renders Android apps as GTK4 windows. "Theme coherence" means those windows
pick up the host desktop's look — its light/dark mode and its palette/accent —
so an Android app does not stand out against native apps. This is written for
coherence with derisk (the LosOS Desktop shell), and the interface below is the
one the derisk theming engine publishes.

## The interface derisk publishes

The derisk theming engine (crate `mcsapi-theme` in dasmatus/mcsapi — zero
dependencies, no toolkit) owns the theme and writes it to two files under
`$XDG_RUNTIME_DIR/derisk/`, each rewritten atomically (write a temp file, then
rename) whenever the theme changes:

- **`theme.json`** — `id`, `name`, `scheme` (dark/light), `palette`
  (background, surface, foreground, border, accent, destructive), `tokens`
  (the shadcn roles: background, foreground, card, muted, muted_foreground,
  primary, primary_foreground, secondary, hover, destructive,
  destructive_foreground, border, ring, overlay, selection, radius), `fonts`
  (sans, weight, monospace, sizes in logical px), `icons` (theme, cursor,
  cursor_size), and a `portal` block mirroring the Freedesktop appearance keys
  (color_scheme 1=dark/2=light, accent_color `[r,g,b]` in 0..1, contrast).
  Colors are `"#rrggbb"` or `"#rrggbbaa"`.
- **`android/values/colors.xml`** — the same theme as a ready Android
  resource: `mcsapi_<role>` colors (`#aarrggbb`) with
  `colorPrimary`/`colorAccent`/`colorSurface`/`colorOnSurface`/`colorError`/
  `textColorPrimary`/`windowBackground`/`statusBarColor` aliased onto them.
  This is what `mcsapi_theme::export::android_colors(&theme)` generates.

A consumer watches `$XDG_RUNTIME_DIR/derisk/` with inotify for `IN_MOVED_TO`
(the rename half of each atomic write) to pick up live theme changes.

The long-term live signal is the standard `org.freedesktop.appearance` keys
(`color-scheme`, `accent-color`, `contrast`) on the `org.freedesktop.portal.Settings`
D-Bus interface — the same values as the `portal` block above — but the engine
does not implement that yet, so **read the files, do not depend on the portal
signal**. x2mcsapi remains the CSS/GTK/Qt restyler for foreign toolkit apps; it
is not this interface.

## What ATL consumes, and the split

- **Light/dark** already follows the `org.freedesktop.appearance` `color-scheme`
  key that GTK4 honours on its own, through the settings portal LosOS Desktop
  enables. No ATL code; noted so it is not re-implemented.
- **ATL's own GTK chrome** (title bar, dialogs) is restyled by x2mcsapi as any
  GTK4 app, so ATL need not drive it.
- **The Android content ATL draws itself** is the piece x2mcsapi cannot reach,
  because it is ATL's custom rendering of Android views rather than standard
  GTK widgets. This is what ATL consumes the interface for: map
  `theme.json`'s palette/tokens onto the colors ATL's view rendering uses (and
  `colors.xml` onto the framework resource values an app reads for
  `?attr/colorPrimary` and friends), re-reading on each `IN_MOVED_TO`.

## Status

- Implemented: a stopgap GTK CSS layer — if `DERISK_THEME_CSS` names a CSS
  file, `main.c` loads it over ATL's default stylesheet at USER priority. A
  desktop that generates GTK CSS from the theme can use it today.
- The task, to do in an environment that can build and run ATL end to end:
  read `$XDG_RUNTIME_DIR/derisk/theme.json` (and/or overlay the published
  `colors.xml` onto framework-res), apply its palette/tokens to ATL's view
  rendering, and watch the directory with inotify for live updates. The
  `mcsapi-theme` crate's `export::json` fixes the exact field names to parse.
