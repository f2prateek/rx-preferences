package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

final class RealPreference<T> implements Preference<T> {
  private final SharedPreferences preferences;
  private final String key;
  private final T defaultValue;
  private final Adapter<T> adapter;
  private final Observable<T> values;
  private T cache; // Guarded by this.

  RealPreference(SharedPreferences preferences, final String key, T defaultValue,
      Adapter<T> adapter, Observable<String> keyChanges) {
    this.preferences = preferences;
    this.key = key;
    this.defaultValue = defaultValue;
    this.adapter = adapter;
    this.values = keyChanges //
        .filter(new Predicate<String>() {
          @Override public boolean test(String changedKey) throws Exception {
            return key.equals(changedKey);
          }
        }) //
        .startWith("<init>") // Dummy value to trigger initial load.
        .map(new Function<String, T>() {
          @Override public T apply(String s) throws Exception {
            return get();
          }
        });
  }

  @Override @NonNull public String key() {
    return key;
  }

  @Override @Nullable public T defaultValue() {
    return defaultValue;
  }

  @Override @Nullable public synchronized T get() {
    if (!preferences.contains(key)) {
      return defaultValue;
    }
    if (cache == null) {
      cache = adapter.get(key, preferences);
    }
    return cache;
  }

  @Override public synchronized void set(@Nullable T value) {
    SharedPreferences.Editor editor = preferences.edit();
    if (value == null) {
      editor.remove(key);
    } else {
      adapter.set(key, value, editor);
    }
    editor.apply();
    cache = value;
  }

  @Override public boolean isSet() {
    return preferences.contains(key);
  }

  @Override public void delete() {
    set(null);
  }

  @Override @CheckResult @NonNull public Observable<T> asObservable() {
    return values;
  }

  @Override @CheckResult @NonNull public Consumer<? super T> asConsumer() {
    return new Consumer<T>() {
      @Override public void accept(T value) throws Exception {
        set(value);
      }
    };
  }
}
