# Roadmap

Goal: one install (ZUXFree module + this app + the optional Google-apps add-on) that turns a Chinese Legion Y700 Gen 4 into a working global tablet, and stays working across Lenovo updates.

## v1.1.0 (this release)

- [x] Core-screen safety: `ZuiPackageInstaller`, `ZuiPermissionController` and `ZuiContacts` are only switched off when a Google replacement is installed. Before this, using the module without DeZUX's bundled APKs could leave the tablet with no installer or permission screen.
- [x] Switch off the Lenovo promo/push features `LenovoWhatsNew` and `TabletPushOut`, matching ZUXFree.
- [x] Own app ID (`io.github.thebohemiangeek.zuxfree.xposed`) and name, so it never collides with upstream installs.
- [x] Unit tests run in CI on every push.
- [x] Lenovo's feature table for 1.5.10.259 checked in under `docs/firmware/`.

## v1.2: Android 17 (ZUXOS 2.5)

ZUXOS 2.5.10.003 (Android 17) went stable on 2026-10-30.
- Pull the TB322FC 2.5 firmware through the zuxfree-addon inventory pipeline and diff its feature table and class names against 1.5.10.259.
- Update the hook targets that moved; keep both variants where the class name differs.
- Raise `targetSdk`/`compileSdk` to 37 once the SDK is available in CI.

## v1.3: Settings screen

- A small in-app screen (libxposed remote preferences) to turn each hook group on or off: feature switches, Settings, launcher, Game Assistant, Gallery, wallpapers.
- Show which hooks installed and which were skipped on the current firmware, so bug reports come with the answer attached.

## v1.4: Work with the ZUXFree add-on

- Once the add-on ships Google's package installer and permission controller, list their folder names in `REPLACEABLE_FEATURES` (taken from the TB323FU ROW inventory) and test the full swap on a device.
- Add the remaining ROW features the add-on makes possible (Google Contacts, Google DocumentsUI overlays).

## Ongoing

- Merge upstream DeZUXposed fixes (`git fetch upstream`).
- Keep dependencies current (Dependabot PRs).
