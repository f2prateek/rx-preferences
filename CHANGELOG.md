Change Log
==========

Version 2.0.0-RC1 *(25-12-2016)*
--------------------------------

rx-preferences has been updated to support RxJava 2.0. The `Preference` type is now an interface, but the core itself is mostly unchanged.

Because the release includes breaking API changes, we're changing the project's package name from `com.f2prateek.rx.preferences` to `com.f2prateek.rx.preferences2`. The maven group has also changed to `com.f2prateek.rx.preferences2`. This should make it possible for large applications and libraries to migrate incrementally. 

Version 1.0.2 *(15-06-2016)*
----------------------------

 * Remove custom backpressure support in favor of RxJava 1.1's built-in [buffer latest](https://github.com/f2prateek/rx-preferences/pull/39).


Version 1.0.1 *(28-10-2015)*
----------------------------

 * Add support for [backpressure](https://github.com/f2prateek/rx-preferences/pull/27).
 * Use [reasonable defaults](https://github.com/f2prateek/rx-preferences/pull/29) for creating Preferences that store primitives.


Version 1.0.0 *(23-08-2015)*
----------------------------

Initial release.
