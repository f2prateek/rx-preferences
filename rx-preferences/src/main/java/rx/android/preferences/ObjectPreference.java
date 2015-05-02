package rx.android.preferences;

import android.content.SharedPreferences;
import java.io.IOException;

public class ObjectPreference<T> extends Preference<T> {
  private final Converter<T> converter;
  private T value;

  public ObjectPreference(SharedPreferences sharedPreferences, Converter<T> converter, String key) {
    this(sharedPreferences, converter, key, null);
  }

  public ObjectPreference(SharedPreferences sharedPreferences, Converter<T> converter, String key,
      T defaultValue) {
    super(sharedPreferences, key, defaultValue);
    this.converter = converter;
  }

  @Override public T get() {
    if (value == null) {
      String data = sharedPreferences.getString(key, null);
      if (data == null) {
        value = defaultValue;
      } else {
        try {
          value = converter.fromString(data);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return value;
  }

  @Override public void set(T value) {
    try {
      String serialized = converter.toString(value);
      sharedPreferences.edit().putString(key, serialized).commit();
      this.value = value;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Convert a string to and from a concrete type.
   *
   * @param <T> Object type.
   */
  public interface Converter<T> {
    /** Converts a string to an object. */
    T fromString(String string) throws IOException;

    /** Converts {@code object} to a string. */
    String toString(T object) throws IOException;
  }
}