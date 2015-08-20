package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;

final class IntegerAdapter implements Preference.Adapter<Integer> {
  static final IntegerAdapter INSTANCE = new IntegerAdapter();

  @Override public Integer get(String key, SharedPreferences preferences) {
    return preferences.getInt(key, 0);
  }

  @Override public void set(String key, Integer value, SharedPreferences.Editor editor) {
    editor.putInt(key, value);
  }
}
