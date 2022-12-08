# Change Log

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

<a name="2.1.5"></a>
# 2.1.5 (2022-12-08)

### Bug Fixes / Enhancements
* Update to support Android Sdk Version 31+

<a name="2.1.2"></a>
# 2.1.3 (2022-03-17)

### Bug Fixes / Enhancements
* Fix package.json

<a name="2.1.2"></a>
# 2.1.2 (2020-09-23)

### Bug Fixes / Enhancements
* For Android, add permision android.permission.ACCESS_BACKGROUND_LOCATION for Android API 29 or above

<a name="2.1.1"></a>
# 2.1.1 (2020-09-17)

### Bug fixes

<a name="2.1.0"></a>
# 2.1.0 (2020-09-15)

### Enhancements
* Support Autolinking for React Native 0.60+
* AndroidX support

<a name="2.0.4"></a>
# 2.0.4 (2019-06-25)

### Bug Fixes / Enhancements
* For iOS, adds logics to handle CLCircularRegion class event(s) notified by CLLocationManager only
* For Android, adds logics for Android O


<a name="2.0.3"></a>
# 2.0.3 (2019-05-24)

### Bug Fixes / Enhancements
* Based on react-native-region-monitor@next, updated to use GeofencingClient as the main entry point for interacting with the geofencing APIs. (Reference: https://developers.google.com/android/reference/com/google/android/gms/location/GeofencingClient)

<a name="1.2.0"></a>
# 1.2.0 (2017-01-21)

### Bug Fixes
* **background:** queue events in background mode before listeners are attached ([20b35b8](https://github.com/martijndeh/react-native-region-monitor/commit/20b35b8))
* **regionChange:** post region changes only once ([2de5947](https://github.com/martijndeh/react-native-region-monitor/commit/2de5947))


### Features

* **regions:** implement regionMonitor#clearRegions ([034fa19](https://github.com/martijndeh/react-native-region-monitor/commit/034fa19))
