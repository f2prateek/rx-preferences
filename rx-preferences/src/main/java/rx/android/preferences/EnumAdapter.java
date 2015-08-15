package rx.android.preferences;

import android.content.SharedPreferences;

final class EnumAdapter<T extends Enum<T>> implements Preference.Adapter<T> {
  private final Class<T> enumClass;

  EnumAdapter(Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  @Override public T get(String key, T defaultValue, SharedPreferences preferences) {
    String value = preferences.getString(key, null);
    return value == null ? defaultValue : Enum.valueOf(enumClass, value);
  }

  @Override public void set(String key, T value, SharedPreferences.Editor editor) {
    editor.putString(key, value.name());
  }
}
