package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;

final class LongAdapter implements Preference.Adapter<Long> {
  static final LongAdapter INSTANCE = new LongAdapter();

  @Override public Long get(String key, SharedPreferences preferences) {
    return preferences.getLong(key, 0L);
  }

  @Override public void set(String key, Long value, SharedPreferences.Editor editor) {
    editor.putLong(key, value);
  }
}
