package com.f2prateek.rx.preferences

import android.content.SharedPreferences
import java.util.HashMap

object RxSharedPreferencesCache {
  val lock: Any = Object()
  val cache: MutableMap<SharedPreferences, RxSharedPreferences> = HashMap()

  fun get(sharedPreferences: SharedPreferences): RxSharedPreferences {
    var prefs = cache[sharedPreferences]
    if (prefs == null) {
      synchronized(lock) {
        prefs = cache.get(sharedPreferences)
        if (prefs == null) {
          prefs = RxSharedPreferences.create(sharedPreferences)
          cache[sharedPreferences] = prefs!!
        }
      }
    }
    return prefs!!
  }
}
