package rx.android.preferences;

import android.content.SharedPreferences;

public class LongPreference extends Preference<Long> {
  public LongPreference(SharedPreferences preferences, String key) {
    this(preferences, key, 0L);
  }

  public LongPreference(SharedPreferences sharedPreferences, String key, long defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Long get() {
    return sharedPreferences.getLong(key, defaultValue);
  }

  @Override public void set(Long value) {
    sharedPreferences.edit().putLong(key, value).commit();
  }
}