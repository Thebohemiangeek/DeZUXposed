# ZUXFree Xposed for Lenovo Legion Y700 Gen 4

The Xposed half of [ZUXFree](https://github.com/Thebohemiangeek/zuxfree). It changes ZUI's behaviour from the inside, which a root module alone can't do:

- **Turns off China-only ZUI features** through ZUI's own feature switches: Baidu location, Xiaotian voice assistant, Lenovo promo push (`LenovoWhatsNew`, `TabletPushOut`), China cloud sync, web filtering and more ([full list](app/src/main/java/io/github/miner7222/dezux/HookConstants.kt)).
- **Turns on global (ROW) features**, such as the ROW setup wizard and default apps.
- **Settings:** shows Google services, Wi-Fi hotspot, mainline module versions and "restore preinstalled apps".
- **Launcher:** Circle to Search works with the Google app.
- **Game Assistant:** removes the WeChat and QQ shortcuts.
- **Gallery, wallpapers, live captions:** fixes features that break outside the PRC region.

ZUI's own package installer and permission screen are only switched off when Google's replacements are installed (for example by the ZUXFree add-on), so you're never left without them.

## Install

1. Root with KernelSU, APatch or Magisk, and install an LSPosed build that supports **libxposed API 101+** (e.g. JingMatrix LSPosed / Vector).
2. Install [ZUXFree](https://github.com/Thebohemiangeek/zuxfree/releases) (recommended). This module also works on its own or alongside DeZUX.
3. Install `ZUXFree-Xposed-vX.Y.Z.apk` from [Releases](../../releases), enable it in LSPosed (its scope is preset), and reboot.

## Supported

Legion Y700 Gen 4 (TB322FC), ZUXOS 1.5 / Android 16. ZUXOS 2.5 / Android 17 isn't verified yet; hooks that don't match are skipped and logged rather than crashing.

## Firmware reference

[`docs/firmware/`](docs/firmware) has Lenovo's own feature table for each firmware: every ZUI feature with its description and default state. Use it to find the switch for anything you want to turn on or off.

## Building

`./gradlew testDebugUnitTest assembleDebug`. CI builds a debug APK on every push, and tagging `vX.Y.Z` publishes a signed release.

## Credits

This is a fork of [DeZUXposed](https://github.com/miner7222/DeZUXposed) by jjhitel / miner7222 (MIT), who did the original reverse-engineering. Thank you.

## License

[MIT](LICENSE). The original copyright notice is kept.
