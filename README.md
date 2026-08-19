# Bloom — Women's Wellness / PCOS Companion App

A Kotlin + Jetpack Compose implementation of the Bloom onboarding flow and Home experience,
with Google AdMob monetization (App Open Ad + adaptive Banner Ad).

## Setup instructions

**Requirements:** Android Studio (Ladybug+), JDK 17+, Android SDK with `compileSdk 36` / `platform-tools`.

```bash
git clone <this-repo>
cd Bloom
./gradlew assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`. Open the project in
Android Studio and hit Run for the full experience (a real device/emulator is needed to
exercise the AdMob flows — Compose previews alone won't load ads).

This repo was built and verified with `./gradlew assembleDebug` in a headless environment
without an emulator available, so the build is confirmed to compile and package correctly,
but the on-device flows (ad rendering, navigation, DataStore persistence across restarts)
have **not** been visually smoke-tested on a running device by the author of this commit —
please run it once in Android Studio before treating it as final.

## Architecture

- **UI:** Jetpack Compose + Material3, single-Activity, one `NavHost` (`BloomNavHost`) with
  routes for Splash, the 4 onboarding steps, and Home.
- **Pattern:** MVVM. `OnboardingViewModel` holds in-progress answers across the 4 onboarding
  steps (shared instance, scoped to the NavHost's composition, so back-navigation doesn't
  lose answers). `HomeViewModel` holds the persisted profile (via `StateFlow`) plus today's
  logged values and which bottom sheet is open, and is shared across the four Home tabs
  (Dashboard/Insights/Learn/Settings) and every "Log X" sheet.
- **DI:** no framework — a tiny `BloomViewModelFactory` constructs ViewModels with a
  `BloomPreferencesRepository` dependency (obtained from `BloomApplication`). Simple enough
  not to need Hilt for a project this size.
- **Persistence:** `androidx.datastore.preferences` (`BloomPreferencesRepository`) stores
  onboarding completion, name, journey stage, per-category tracking toggles, per-category
  goal targets, and reminder settings — this is what distinguishes first launch from a
  returning launch. **Today's logged values** (meals/movement/water/relaxation/sleep/cycle/
  symptoms) are intentionally kept in-memory in `HomeViewModel` for this demo; a production
  build would add a Room table keyed by date so history and trends actually persist. This is
  called out again below under Limitations.
- **Design system:** `ui/theme/` (Color/Type/Dimens/Theme) centralizes the cream/maroon/gold
  palette and spacing scale extracted from the Figma file — no screen hardcodes a raw color
  or dp value. `ui/components/` holds every reusable primitive (buttons, cards, toggles,
  radio rows, pill selectors, steppers, bottom nav, bottom-sheet chrome, bar charts, the
  flower logo) so the 20+ screens/sheets compose from the same building blocks.

## Ad implementation approach

- **SDK init:** `MobileAds.initialize()` runs once in `BloomApplication.onCreate()`.
- **Ad unit IDs:** centralized in `ads/AdConfig.kt`, switched between Google's public test
  IDs and (placeholder) production IDs via `BuildConfig.DEBUG`, so nothing is hardcoded at
  the call sites — swap the `PROD_*` constants before a release build.
- **App Open Ad** (`ads/AppOpenAdManager.kt`): a single owner of the whole lifecycle —
  preload, 4-hour cache expiry, and *when* it's safe to show.
  - **First-time users are never interrupted.** `AppOpenAdManager.setAdsEnabled(true)` is
    only called by the Splash screen once it has confirmed `onboardingCompleted == true`
    from DataStore; first-time users go straight to onboarding and the manager never loads
    or shows an ad during that flow. (The brief's "Expected Flow" section shows an ad before
    onboarding on first launch, but the detailed App Open Ad requirements explicitly say the
    opposite — "No App Open Ad should interrupt the first-time onboarding experience." This
    implementation follows the more specific, unambiguous requirement.)
  - **Non-blocking:** the Splash screen requests a fresh ad and waits up to 2.5s
    (`withTimeoutOrNull`) before navigating to Home regardless of whether the ad finished
    loading — never blocks indefinitely.
  - **Two triggers, no double-show:** the splash flow explicitly drives the very first show
    for a returning user; every subsequent background→foreground transition is driven
    automatically by `ProcessLifecycleOwner.onStart`. The manager's `isColdStart` flag makes
    the very first `onStart` (which is the same cold-launch the splash flow already handled)
    a no-op, so the two triggers never race into a double show.
  - **Never stacks two ads:** `isShowingAd` guards every show attempt.
  - **Failure handling:** load failures, show failures, and dismissal all funnel into the
    same `loadAd()` call to warm the next ad, and always invoke the completion callback so
    the caller (Home) is never left waiting.
- **Banner Ad** (`ads/BannerAdView.kt`): a Compose `BloomBannerAd` wraps `AdView`, sized with
  `AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize` against the actual screen width
  (no hardcoded banner dimensions), lifecycle-paused/resumed with the host `LifecycleOwner`,
  and destroyed on dispose. It collapses to zero height on load failure instead of leaving a
  broken placeholder. Placed in the Home `Scaffold`'s `bottomBar` slot, directly above the
  bottom nav and only on the Dashboard tab — it never overlaps or scrolls with the wellness
  content underneath.
- **Analytics** (`ads/AdAnalytics.kt`): all 8 requested events
  (`app_open_ad_requested/loaded/failed/shown/dismissed`, `banner_ad_loaded/failed/impression`)
  are logged via a single overridable `logger` function (defaults to Logcat) — swap it for a
  Firebase Analytics call in one place without touching any ad code.

## Test Ad IDs used

Google's official public test IDs (Android), used in debug builds via `AdConfig`:

| Ad type | Test ad unit ID |
|---|---|
| App Open | `ca-app-pub-3940256099942544/9257395921` |
| Adaptive Banner | `ca-app-pub-3940256099942544/9214589741` |
| App ID (manifest) | `ca-app-pub-3940256099942544~3347511713` |

## Assumptions / limitations

- **Onboarding vs. App Open Ad ordering:** resolved the brief's internal contradiction in
  favor of "no ad interrupts first-time onboarding" — see Ad implementation approach above.
- **Today's logged values are in-memory only** (reset on process death) — see Persistence
  above. Onboarding completion and all Settings (tracking toggles, goal targets, reminder
  config, name, journey stage) *are* fully persisted via DataStore.
- **Figma access:** the Figma file couldn't be fetched programmatically (no browser/Figma
  tooling in this environment) — the UI was built from screenshots of the onboarding, home,
  insights, learn, settings, and log-sheet frames shared directly in this conversation, plus
  the written spec. Colors/spacing are close visual matches, not pixel-exact exports.
  Headings use the platform serif italic (`FontFamily.Serif` + italic) rather than a bundled
  custom font (e.g. Lora/Playfair Display) to avoid shipping font files for this demo — swap
  `BloomHeadingFont` in `ui/theme/Type.kt` for a real `GoogleFont`/bundled `Font` to match the
  mark exactly.
- **"Learn" and "Insights" article content** is static/non-interactive (tapping a Learn topic
  doesn't open a detail screen) — the brief scoped only Onboarding + Home in detail; these
  screens exist because they're required for the bottom nav (visible in the Home Dashboard
  frame) to make sense, and because the "Log X" sheets that Home links to needed somewhere to
  live, but their depth wasn't part of the core ask.
- **Live Notification Preview** (Settings → "Live notification preview") is a UI-only mock of
  what a live-updating lock-screen notification could look like — no real Android
  notification is posted; wiring `NotificationCompat`/promoted/progress-style notifications
  was out of scope for this demo.
- **Sign out** button in Settings is inert (no auth system exists to sign out of).
- **minSdk 24** with core library desugoring enabled (for `java.time`, used by cycle-date
  tracking).
- Not verified on a running emulator/device in this environment (see Setup instructions) —
  `./gradlew assembleDebug` succeeds and produces a valid APK, but please do one manual run
  through Android Studio before shipping.
