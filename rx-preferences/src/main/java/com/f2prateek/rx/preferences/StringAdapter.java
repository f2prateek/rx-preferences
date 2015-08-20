package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;

final class StringAdapter implements Preference.Adapter<String> {
  static final StringAdapter INSTANCE = new StringAdapter();

  @Override public String get(String key, SharedPreferences preferences) {
    return preferences.getString(key, null);
  }

  @Override public void set(String key, String value, SharedPreferences.Editor editor) {
    editor.putString(key, value);
  }
}
