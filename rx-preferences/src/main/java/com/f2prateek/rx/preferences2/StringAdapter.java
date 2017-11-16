package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;

final class StringAdapter implements RealPreference.Adapter<String> {
  static final StringAdapter INSTANCE = new StringAdapter();

  @Override
  public String get(String key, SharedPreferences preferences) {
    String value = preferences.getString(key, null);
    assert value != null; // Not called unless key is present.
    return value;
  }

  @Override
  public void set(String key, String value, SharedPreferences.Editor editor) {
    editor.putString(key, value);
  }
}
