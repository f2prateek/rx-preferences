package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;

public abstract class Preference<T> {
  final SharedPreferences sharedPreferences;
  final String key;
  final T defaultValue;

  Preference(SharedPreferences sharedPreferences, String key, T defaultValue) {
    this.sharedPreferences = Utils.assertNotNull(sharedPreferences);
    this.key = Utils.assertNotNullOrEmpty(key);
    this.defaultValue = defaultValue;
  }

  public abstract T get();

  public abstract void set(T value);

  public boolean isSet() {
    return sharedPreferences.contains(key);
  }

  public void delete() {
    sharedPreferences.edit().remove(key).commit();
  }

  public abstract Observable<T> asObservable();
}
