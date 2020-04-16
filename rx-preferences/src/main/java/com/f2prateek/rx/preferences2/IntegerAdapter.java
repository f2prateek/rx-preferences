package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

final class IntegerAdapter implements RealPreference.Adapter<Integer> {
  static final IntegerAdapter INSTANCE = new IntegerAdapter();

  @NonNull @Override public Integer get(@NonNull String key, @NonNull SharedPreferences preferences,
      @NonNull Integer defaultValue) {
    return preferences.getInt(key, defaultValue);
  }

  @Override public void set(@NonNull String key, @NonNull Integer value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putInt(key, value);
  }
}
