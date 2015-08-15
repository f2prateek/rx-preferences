package rx.android.preferences;

import android.content.SharedPreferences;
import java.util.Set;

final class StringSetAdapter implements Preference.Adapter<Set<String>> {
  static final StringSetAdapter INSTANCE = new StringSetAdapter();

  @Override
  public Set<String> get(String key, Set<String> defaultValue, SharedPreferences preferences) {
    return preferences.getStringSet(key, defaultValue);
  }

  @Override public void set(String key, Set<String> value, SharedPreferences.Editor editor) {
    editor.putStringSet(key, value);
  }
}
