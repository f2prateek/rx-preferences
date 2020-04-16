package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

final class LongAdapter implements RealPreference.Adapter<Long> {
  static final LongAdapter INSTANCE = new LongAdapter();

  @NonNull @Override public Long get(@NonNull String key, @NonNull SharedPreferences preferences,
      @NonNull Long defaultValue) {
    return preferences.getLong(key, defaultValue);
  }

  @Override public void set(@NonNull String key, @NonNull Long value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putLong(key, value);
  }
}
