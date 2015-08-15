package rx.android.preferences;

import android.content.SharedPreferences;

final class FloatAdapter implements Preference.Adapter<Float> {
  static final FloatAdapter INSTANCE = new FloatAdapter();

  @Override public Float get(String key, Float defaultValue, SharedPreferences preferences) {
    return preferences.getFloat(key, defaultValue);
  }

  @Override public void set(String key, Float value, SharedPreferences.Editor editor) {
    editor.putFloat(key, value);
  }
}
