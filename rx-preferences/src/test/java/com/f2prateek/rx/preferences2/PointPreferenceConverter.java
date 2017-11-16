package com.f2prateek.rx.preferences2;

final class PointPreferenceConverter implements Preference.Converter<Point> {
  @Override
  public Point deserialize(String serialized) {
    String[] parts = serialized.split(",");
    if (parts.length != 2) {
      throw new IllegalStateException("Malformed point value: '" + serialized + "'");
    }
    return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
  }

  @Override
  public String serialize(Point value) {
    return value.x + "," + value.y;
  }
}
