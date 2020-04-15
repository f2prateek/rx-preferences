package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

final class FloatAdapter implements RealPreference.Adapter<Float> {
  static final FloatAdapter INSTANCE = new FloatAdapter();

  @NonNull @Override public Float get(@NonNull String key, @NonNull SharedPreferences preferences,
      @NonNull Float defaultValue) {
    return preferences.getFloat(key, defaultValue);
  }

  @Override public void set(@NonNull String key, @NonNull Float value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putFloat(key, value);
  }
}
