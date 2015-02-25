package rx.android.preferences;

import android.content.SharedPreferences;

public class FloatPreference extends Preference<Float> {
  public FloatPreference(SharedPreferences preferences, String key) {
    this(preferences, key, 0f);
  }

  public FloatPreference(SharedPreferences sharedPreferences, String key, float defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Float get() {
    return sharedPreferences.getFloat(key, defaultValue);
  }

  @Override public void set(Float value) {
    sharedPreferences.edit().putFloat(key, value).commit();
  }
}