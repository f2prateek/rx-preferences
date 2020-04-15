package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

final class EnumAdapter<T extends Enum<T>> implements RealPreference.Adapter<T> {
  private final Class<T> enumClass;

  EnumAdapter(Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  @NonNull @Override public T get(@NonNull String key, @NonNull SharedPreferences preferences,
      @NonNull T defaultValue) {
    String value = preferences.getString(key, null);
    if (value == null) return defaultValue;
    return Enum.valueOf(enumClass, value);
  }

  @Override
  public void set(@NonNull String key, @NonNull T value, @NonNull SharedPreferences.Editor editor) {
    editor.putString(key, value.name());
  }
}
