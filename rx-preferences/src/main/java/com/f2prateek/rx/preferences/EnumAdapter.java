package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;

final class EnumAdapter<T extends Enum<T>> implements Preference.Adapter<T> {
  private final Class<T> enumClass;

  EnumAdapter(Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  @Override public T get(String key, SharedPreferences preferences) {
    return Enum.valueOf(enumClass, preferences.getString(key, null));
  }

  @Override public void set(String key, T value, SharedPreferences.Editor editor) {
    editor.putString(key, value.name());
  }
}
