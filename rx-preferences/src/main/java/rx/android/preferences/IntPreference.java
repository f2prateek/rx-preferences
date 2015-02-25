package rx.android.preferences;

import android.content.SharedPreferences;

public class IntPreference extends Preference<Integer> {
  public IntPreference(SharedPreferences preferences, String key) {
    this(preferences, key, 0);
  }

  public IntPreference(SharedPreferences sharedPreferences, String key, int defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Integer get() {
    return sharedPreferences.getInt(key, defaultValue);
  }

  @Override public void set(Integer value) {
    sharedPreferences.edit().putInt(key, value).commit();
  }
}