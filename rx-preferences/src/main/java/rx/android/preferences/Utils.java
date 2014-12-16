package rx.android.preferences;

import android.content.SharedPreferences;
import android.text.TextUtils;

final class Utils {
  private Utils() {
    throw new AssertionError("no instances allowed");
  }

  static SharedPreferences assertNotNull(SharedPreferences sharedPreferences) {
    if (sharedPreferences == null) {
      throw new IllegalArgumentException("sharedPreferences must not be null");
    }
    return sharedPreferences;
  }

  static String assertNotNullOrEmpty(String key) {
    if (isNullOrEmpty(key)) {
      throw new IllegalArgumentException("key must not be null");
    }
    return key;
  }

  private static boolean isNullOrEmpty(String text) {
    return text == null || TextUtils.getTrimmedLength(text) == 0;
  }
}
