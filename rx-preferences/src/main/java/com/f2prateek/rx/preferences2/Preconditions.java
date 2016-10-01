package com.f2prateek.rx.preferences2;

final class Preconditions {
  static void checkNotNull(Object o, String message) {
    if (o == null) {
      throw new NullPointerException(message);
    }
  }

  private Preconditions() {
    throw new AssertionError("No instances");
  }
}
