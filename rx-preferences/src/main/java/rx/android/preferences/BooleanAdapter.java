package rx.android.preferences;

import android.content.SharedPreferences;

final class BooleanAdapter implements Preference.Adapter<Boolean> {
  static final BooleanAdapter INSTANCE = new BooleanAdapter();

  @Override public Boolean get(String key, Boolean defaultValue, SharedPreferences preferences) {
    return preferences.getBoolean(key, defaultValue);
  }

  @Override public void set(String key, Boolean value, SharedPreferences.Editor editor) {
    editor.putBoolean(key, value);
  }
}
