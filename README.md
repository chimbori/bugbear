# BugBear

BugBear is a privacy-friendly, minimalist, modern, extensible, wire-format compatible alternative to [ACRA](https://github.com/ACRA/acra).

Simplicity and minimalism is the guiding principle behind every design decision in BugBear.
It does not have many bells or whistles. It is opinionated by design, and the most common options have been chosen and optimized for.
It uses basic Kotlin constructs to provide extensibility, there are no fancy DSLs or service loaders; this minimizes the code (both source and binary) included in the app.

## Privacy Friendly

BugBear is designed from the ground up to be privacy-focused.

- Logs collected by BugBear contain much less sensitive information than those collected by the ACRA client library. This is intentional. Data that is not critically required is not collected.
- Limiting data collection makes it harder for a malicious developer to fingerprint a specific device or user based on reported characteristics.
- Reduced risk of unintentional data exposure in case the crash logs server is compromised or accessed inappropriately.
- Saves storage costs by having less data to store on the server.

The following fields are not logged by BugBear:

- `SharedPreferences`:
  This can contain private data, so the library does not log anything by default.
  If you care about specific SharedPreferences, you can log them in the app explicitly, but the library will not implicitly log every value for you.
- `USER_IP`:
  Diagnosing app crashes does not usually require a user’s IP address.
- `DUMPSYS_MEMINFO`, `AVAILABLE_MEM_SIZE`, `TOTAL_MEM_SIZE`:
  Could be used for fingerprinting, and has questionable value in diagnosing crashes, so this is not logged.
- `BUILD_CONFIG`:
  Specific build-related fields are logged, but the whole object is not logged.
- `ENVIRONMENT`:
  Way too many fields, with very few of them actually useful.

## Minimalist

- **Plugins** (known as `Populator`s) are simply Kotlin objects. App developers implement an interface and add the new POJO (POKO because it’s Kotlin?) to a List at startup.
  No services, plugin loaders, or other wrappers are necessary.
- **No Configuration DSL**, etc. Just set properties directly on each plugin. More readable, easier to follow, no syntactic sugar.
- **No Reflection**: This library will never unintentionally collect more info than what’s hard-coded in it at compile-time. Because we don’t use reflection, BugBear won’t serialize static fields like Build.UNKNOWN as `"UNKNOWN": "unknown"`
- **Single Destination**: The only supported destination is an HTTP(S) endpoint. Very few apps these days show dialogs, notifications, toasts, or other UI when they crash. Even Android OS no longer shows a dialog when an app crashes.
- **Minimal Dependencies**: Only basic Android and Kotlin dependencies are used.

## Modern

Android development has evolved significantly since the early days when the original ACRA library was authored.
BugBear brings the following changes over ACRA:

- **Uses WorkManager** instead of `JobService`.
  Saves battery & works consistently across Android versions to upload reports periodically.
- **KotlinX Serialization** instead on `JsonObject`.
  The schema is defined as a Kotlin data class at compile-time, and not as keys/values (no surprises at runtime).
- **Kotlin-first** and Kotlin-only.

## Extensible

If you really need a bunch of extra information added to the outgoing report, you can add it yourself.

- Adding new Populators is easy. Implement an interface, and add it during initialization.
- Removing pre-configured Populators is also straightforward.

## Wire Format Compatible with ACRA

The data format used by BugBear is exactly the same as ACRA (minus a few fields that BugBear chooses not to log), so you can use any ACRA-compatible backend to collect and analyze crashes.
We recommend [Acrarium](https://github.com/F43nd1r/Acrarium), maintained by the current maintainer of ACRA.

## New Features

### Hosted Config

Typically, the configuration for bug reporting libraries is baked into the app binary.
But this prevents developers from being able to update the Report Upload URL, or to decommission bug reporting for apps that are no longer supported.

BugBear offers a way to host the configuration on a remote server which is fetched at runtime & cached on the client.
This allows developers to switch the reporting URL or disable BugBear entirely, all remotely.

# License

    Copyright 2024 © Chimbori — Makers of Hermit, the Lite Apps Browser.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
