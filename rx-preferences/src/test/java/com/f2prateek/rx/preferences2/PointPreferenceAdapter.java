package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

final class PointPreferenceAdapter implements Preference.Adapter<Point> {
  @Override public Point get(@NonNull String key, @NonNull SharedPreferences preferences) {
    String value = preferences.getString(key, null);
    assert value != null; // Not called unless key is present.
    String[] parts = value.split(",");
    if (parts.length != 2) {
      throw new IllegalStateException("Malformed point value: '" + value + "'");
    }
    return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
  }

  @Override public void set(@NonNull String key, @NonNull Point value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putString(key, value.x + "," + value.y);
  }
}
