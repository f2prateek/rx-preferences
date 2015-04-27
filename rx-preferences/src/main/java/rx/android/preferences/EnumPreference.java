package rx.android.preferences;

import android.content.SharedPreferences;

public class EnumPreference<T extends Enum<T>> extends Preference<T> {
  private final Class<T> enumClass;
  T value;

  EnumPreference(SharedPreferences sharedPreferences, Class<T> enumClass, String key) {
    this(sharedPreferences, enumClass, key, null);
  }

  EnumPreference(SharedPreferences sharedPreferences, Class<T> enumClass, String key,
      T defaultValue) {
    super(sharedPreferences, key, defaultValue);
    this.enumClass = enumClass;
  }

  @Override public T get() {
    if (value == null) {
      String serialized = sharedPreferences.getString(key, null);
      if (serialized == null) {
        value = defaultValue;
      } else {
        value = Enum.valueOf(enumClass, serialized);
      }
    }
    return value;
  }

  @Override public void set(T value) {
    sharedPreferences.edit().putString(key, value == null ? null : value.toString()).commit();
  }
}
