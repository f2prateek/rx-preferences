package com.f2prateek.rx.preferences2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.f2prateek.rx.preferences2.Preconditions.checkNotNull;

final class RealPreference<T> implements Preference<T> {
  /** Stores and retrieves instances of {@code T} in {@link SharedPreferences}. */
  interface Adapter<T> {
    /** Retrieve the value for {@code key} from {@code preferences}. */
    T get(@NonNull String key, @NonNull SharedPreferences preferences);

    /**
     * Store non-null {@code value} for {@code key} in {@code editor}.
     * <p>
     * Note: Implementations <b>must not</b> call {@code commit()} or {@code apply()} on
     * {@code editor}.
     */
    void set(@NonNull String key, @NonNull T value, @NonNull SharedPreferences.Editor editor);
  }

  @NonNull private final SharedPreferences preferences;
  @NonNull private final String key;
  @NonNull private final T defaultValue;
  @NonNull private final Adapter<T> adapter;
  @NonNull private final Observable<T> values;

  @SuppressLint("CommitPrefEdits")
  RealPreference(@NonNull SharedPreferences preferences, @NonNull final String key,
                 @NonNull T defaultValue, @NonNull Adapter<T> adapter,
                 @NonNull Observable<String> keyChanges) {
    this.preferences = preferences;
    this.key = key;
    this.defaultValue = defaultValue;
    this.adapter = adapter;
    this.values = keyChanges //
        .distinctUntilChanged() //
        .startWith("<init>") // Dummy value to trigger initial load.
        .map(new Function<String, T>() {
          @Override public T apply(String s) {
            return get();
          }
        });
  }

  @Override @NonNull public String key() {
    return key;
  }

  @Override @NonNull public T defaultValue() {
    return defaultValue;
  }

  @Override @NonNull public synchronized T get() {
    if (!preferences.contains(key)) {
      return defaultValue;
    }
    return adapter.get(key, preferences);
  }

  @Override public void set(@NonNull T value) {
    checkNotNull(value, "value == null");
    SharedPreferences.Editor editor = preferences.edit();
    adapter.set(key, value, editor);
    editor.apply();
  }

  @Override public boolean isSet() {
    return preferences.contains(key);
  }

  @Override public synchronized void delete() {
    preferences.edit().remove(key).apply();
  }

  @Override @CheckResult @NonNull public Observable<T> asObservable() {
    return values;
  }

  @Override @CheckResult @NonNull public Consumer<? super T> asConsumer() {
    return new Consumer<T>() {
      @Override public void accept(T value) {
        set(value);
      }
    };
  }
}
