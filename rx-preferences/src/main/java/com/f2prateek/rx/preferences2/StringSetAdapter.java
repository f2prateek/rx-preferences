package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;

final class StringSetAdapter implements RealPreference.Adapter<Set<String>> {
  static final StringSetAdapter INSTANCE = new StringSetAdapter();

  @Override
  public Set<String> get(String key, SharedPreferences preferences) {
    Set<String> value = preferences.getStringSet(key, null);
    assert value != null; // Not called unless key is present.
    return Collections.unmodifiableSet(value);
  }

  @Override
  public void set(String key, Set<String> value, SharedPreferences.Editor editor) {
    editor.putStringSet(key, value);
  }
}
