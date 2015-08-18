package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;

final class StringAdapter implements Preference.Adapter<String> {
  static final StringAdapter INSTANCE = new StringAdapter();

  @Override public String get(String key, String defaultValue, SharedPreferences preferences) {
    return preferences.getString(key, defaultValue);
  }

  @Override public void set(String key, String value, SharedPreferences.Editor editor) {
    editor.putString(key, value);
  }
}
