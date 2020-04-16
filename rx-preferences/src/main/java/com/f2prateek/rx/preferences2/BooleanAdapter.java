package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

final class BooleanAdapter implements RealPreference.Adapter<Boolean> {
  static final BooleanAdapter INSTANCE = new BooleanAdapter();

  @NonNull @Override public Boolean get(@NonNull String key, @NonNull SharedPreferences preferences,
      @NonNull Boolean defaultValue) {
    return preferences.getBoolean(key, defaultValue);
  }

  @Override public void set(@NonNull String key, @NonNull Boolean value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putBoolean(key, value);
  }
}
