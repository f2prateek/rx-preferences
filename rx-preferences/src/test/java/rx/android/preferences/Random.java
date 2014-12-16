package rx.android.preferences;

public final class Random {
  final static java.util.Random RANDOM = new java.util.Random();

  private Random() {
    throw new AssertionError("No Instances.");
  }

  static int nextInt() {
    return RANDOM.nextInt();
  }

  static int nextInt(int n) {
    return RANDOM.nextInt(n);
  }

  static boolean nextBoolean() {
    return RANDOM.nextBoolean();
  }

  static String nextString() {
    byte[] bytes = new byte[nextInt(10)];
    RANDOM.nextBytes(bytes);
    return new String(bytes);
  }
}
