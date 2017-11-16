package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;

final class EnumAdapter<T extends Enum<T>> implements RealPreference.Adapter<T> {
  private final Class<T> enumClass;

  EnumAdapter(Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  @Override
  public T get(String key, SharedPreferences preferences) {
    String value = preferences.getString(key, null);
    assert value != null; // Not called unless key is present.
    return Enum.valueOf(enumClass, value);
  }

  @Override
  public void set(String key, T value, SharedPreferences.Editor editor) {
    editor.putString(key, value.name());
  }
}
