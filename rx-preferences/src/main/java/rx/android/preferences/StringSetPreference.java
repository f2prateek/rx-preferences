package rx.android.preferences;

import android.content.SharedPreferences;
import java.util.Set;

public class StringSetPreference extends Preference<Set<String>> {
  public StringSetPreference(SharedPreferences preferences, String key) {
    this(preferences, key, null);
  }

  public StringSetPreference(SharedPreferences sharedPreferences, String key,
      Set<String> defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Set<String> get() {
    return sharedPreferences.getStringSet(key, defaultValue);
  }

  @Override public void set(Set<String> value) {
    sharedPreferences.edit().putStringSet(key, value).commit();
  }
}