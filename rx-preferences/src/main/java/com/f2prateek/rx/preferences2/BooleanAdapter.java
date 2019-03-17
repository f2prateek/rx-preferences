package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

final class BooleanAdapter implements RealPreference.Adapter<Boolean> {
  static final BooleanAdapter INSTANCE = new BooleanAdapter();

  @Override public Boolean get(@NonNull String key, @NonNull SharedPreferences preferences) {
    return preferences.getBoolean(key, false);
  }

  @Override public void set(@NonNull String key, @NonNull Boolean value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putBoolean(key, value);
  }
}
