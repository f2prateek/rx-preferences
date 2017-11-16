package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;

final class LongAdapter implements RealPreference.Adapter<Long> {
  static final LongAdapter INSTANCE = new LongAdapter();

  @Override
  public Long get(String key, SharedPreferences preferences) {
    return preferences.getLong(key, 0L);
  }

  @Override
  public void set(String key, Long value, SharedPreferences.Editor editor) {
    editor.putLong(key, value);
  }
}
