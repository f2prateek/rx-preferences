package rx.android.preferences;

import android.content.SharedPreferences;

final class PointPreferenceAdapter implements Preference.Adapter<Point> {
  @Override public Point get(String key, Point defaultValue, SharedPreferences preferences) {
    String value = preferences.getString(key, null);
    if (value == null) {
      return defaultValue;
    }
    String[] parts = value.split(",");
    if (parts.length != 2) {
      throw new IllegalStateException("Malformed point value: '" + value + "'");
    }
    return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
  }

  @Override public void set(String key, Point value, SharedPreferences.Editor editor) {
    String storedValue = null;
    if (value != null) {
      storedValue = value.x + "," + value.y;
    }
    editor.putString(key, storedValue);
  }
}
