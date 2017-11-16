package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.f2prateek.rx.preferences2.Preconditions.checkNotNull;

final class RealPreference<T> implements Preference<T> {
  private final SharedPreferences preferences;
  private final String key;
  private final T defaultValue;
  private final Adapter<T> adapter;
  private final Observable<T> values;
  RealPreference(SharedPreferences preferences, final String key, T defaultValue,
                 Adapter<T> adapter, Observable<String> keyChanges) {
    this.preferences = preferences;
    this.key = key;
    this.defaultValue = defaultValue;
    this.adapter = adapter;
    this.values = keyChanges //
        .filter(new Predicate<String>() {
          @Override
          public boolean test(String changedKey) throws Exception {
            return key.equals(changedKey);
          }
        }) //
        .startWith("<init>") // Dummy value to trigger initial load.
        .map(new Function<String, T>() {
          @Override
          public T apply(String s) throws Exception {
            return get();
          }
        });
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public T defaultValue() {
    return defaultValue;
  }

  @Override
  public synchronized T get() {
    if (!preferences.contains(key)) {
      return defaultValue;
    }
    return adapter.get(key, preferences);
  }

  @Override
  public void set(T value) {
    checkNotNull(value, "value == null");
    SharedPreferences.Editor editor = preferences.edit();
    adapter.set(key, value, editor);
    editor.apply();
  }

  @Override
  public boolean isSet() {
    return preferences.contains(key);
  }

  @Override
  public synchronized void delete() {
    preferences.edit().remove(key).apply();
  }

  @Override
  @CheckResult
  public Observable<T> asObservable() {
    return values;
  }

  @Override
  @CheckResult
  public Consumer<? super T> asConsumer() {
    return new Consumer<T>() {
      @Override
      public void accept(T value) throws Exception {
        set(value);
      }
    };
  }

  /**
   * Stores and retrieves instances of {@code T} in {@link SharedPreferences}.
   */
  interface Adapter<T> {
    /**
     * Retrieve the value for {@code key} from {@code preferences}.
     */
    T get(String key, SharedPreferences preferences);

    /**
     * Store non-null {@code value} for {@code key} in {@code editor}.
     * <p>
     * Note: Implementations <b>must not</b> call {@code commit()} or {@code apply()} on
     * {@code editor}.
     */
    void set(String key, T value, SharedPreferences.Editor editor);
  }
}

