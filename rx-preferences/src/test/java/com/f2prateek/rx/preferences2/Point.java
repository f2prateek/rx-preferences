package com.f2prateek.rx.preferences2;

final class Point {
  public final int x;
  public final int y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Point)) return false;
    Point point = (Point) o;
    return x == point.x && y == point.y;
  }

  @Override public int hashCode() {
    return 31 * x + y;
  }

  @Override public String toString() {
    return "Point{x=" + x + ", y=" + y + '}';
  }
}
