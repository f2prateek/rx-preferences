package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

final class FloatAdapter implements Preference.Adapter<Float> {
  static final FloatAdapter INSTANCE = new FloatAdapter();

  @Override public Float get(@NonNull String key, @NonNull SharedPreferences preferences) {
    return preferences.getFloat(key, 0f);
  }

  @Override public void set(@NonNull String key, @NonNull Float value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putFloat(key, value);
  }
}
