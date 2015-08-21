package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;

final class FloatAdapter implements Preference.Adapter<Float> {
  static final FloatAdapter INSTANCE = new FloatAdapter();

  @Override public Float get(String key, SharedPreferences preferences) {
    return preferences.getFloat(key, 0f);
  }

  @Override public void set(String key, Float value, SharedPreferences.Editor editor) {
    editor.putFloat(key, value);
  }
}
