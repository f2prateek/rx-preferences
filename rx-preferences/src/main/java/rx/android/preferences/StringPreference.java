package rx.android.preferences;

import android.content.SharedPreferences;

public class StringPreference extends Preference<String> {
  public StringPreference(SharedPreferences preferences, String key) {
    this(preferences, key, null);
  }

  public StringPreference(SharedPreferences sharedPreferences, String key, String defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public String get() {
    return sharedPreferences.getString(key, defaultValue);
  }

  @Override public void set(String value) {
    sharedPreferences.edit().putString(key, value).commit();
  }
}