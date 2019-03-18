package com.f2prateek.rx.preferences2;

import androidx.annotation.NonNull;

final class PointPreferenceConverter implements Preference.Converter<Point> {
  @NonNull @Override public Point deserialize(@NonNull String serialized) {
    String[] parts = serialized.split(",", -1);
    if (parts.length != 2) {
      throw new IllegalStateException("Malformed point value: '" + serialized + "'");
    }
    return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
  }

  @NonNull @Override public String serialize(@NonNull Point value) {
    return value.x + "," + value.y;
  }
}
