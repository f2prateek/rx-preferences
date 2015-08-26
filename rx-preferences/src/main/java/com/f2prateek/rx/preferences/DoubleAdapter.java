package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

final class DoubleAdapter implements Preference.Adapter<Double> {
  static final DoubleAdapter INSTANCE = new DoubleAdapter();

  @Override public Double get(@NonNull String key, @NonNull SharedPreferences preferences) {
    return Double.longBitsToDouble(preferences.getLong(key, 0L));
  }

  @Override public void set(@NonNull String key, @NonNull Double value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putLong(key, Double.doubleToLongBits(value));
  }
}
