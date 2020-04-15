package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import static com.f2prateek.rx.preferences2.Preconditions.checkNotNull;

final class ConverterAdapter<T> implements RealPreference.Adapter<T> {
  private final Preference.Converter<T> converter;

  ConverterAdapter(Preference.Converter<T> converter) {
    this.converter = converter;
  }

  @NonNull @Override public T get(@NonNull String key, @NonNull SharedPreferences preferences,
      @NonNull T defaultValue) {
    String serialized = preferences.getString(key, null);
    if (serialized == null) return defaultValue;

    T value = converter.deserialize(serialized);
    checkNotNull(value, "Deserialized value must not be null from string: " + serialized);
    return value;
  }

  @Override
  public void set(@NonNull String key, @NonNull T value, @NonNull SharedPreferences.Editor editor) {
    String serialized = converter.serialize(value);
    checkNotNull(serialized, "Serialized string must not be null from value: " + value);
    editor.putString(key, serialized);
  }
}
