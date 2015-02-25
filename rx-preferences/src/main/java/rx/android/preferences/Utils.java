package rx.android.preferences;

import android.text.TextUtils;

final class Utils {
  private Utils() {
    throw new AssertionError("no instances allowed");
  }

  static boolean isNullOrEmpty(String text) {
    return text == null || TextUtils.getTrimmedLength(text) == 0;
  }
}
