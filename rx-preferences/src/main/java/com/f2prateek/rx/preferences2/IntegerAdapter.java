package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

final class IntegerAdapter implements RealPreference.Adapter<Integer> {
  static final IntegerAdapter INSTANCE = new IntegerAdapter();

  @Override public Integer get(@NonNull String key, @NonNull SharedPreferences preferences) {
    return preferences.getInt(key, 0);
  }

  @Override public void set(@NonNull String key, @NonNull Integer value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putInt(key, value);
  }
}
