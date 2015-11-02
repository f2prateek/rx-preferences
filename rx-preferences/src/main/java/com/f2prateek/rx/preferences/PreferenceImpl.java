package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/** A preference of type {@link T}. Instances can be created from {@link RxSharedPreferences}. */
public final class PreferenceImpl<T> implements Preference<T> {

  private final SharedPreferences preferences;
  private final String key;
  private final T defaultValue;
  private final Adapter<T> adapter;
  private final Observable<T> values;

  PreferenceImpl(SharedPreferences preferences, final String key, T defaultValue, Adapter<T> adapter,
                 Observable<String> keyChanges) {
    this.preferences = preferences;
    this.key = key;
    this.defaultValue = defaultValue;
    this.adapter = adapter;
    this.values = keyChanges
        .filter(new Func1<String, Boolean>() {
          @Override public Boolean call(String changedKey) {
            return key.equals(changedKey);
          }
        })
        .startWith("<init>") // Dummy value to trigger initial load.
        .lift(BackpressureBufferLastOperator.<String>instance())
        .map(new Func1<String, T>() {
          @Override public T call(String ignored) {
            return get();
          }
        });
  }

  /** The key for which this preference will store and retrieve values. */
  @Override
  @NonNull
  public String key() {
    return key;
  }

  /** The value used if none is stored. May be {@code null}. */
  @Override
  @Nullable
  public T defaultValue() {
    return defaultValue;
  }

  /**
   * Retrieve the current value for this preference. Returns {@link #defaultValue()} if no value is
   * set.
   */
  @Override
  @Nullable
  public T get() {
    if (!preferences.contains(key)) {
      return defaultValue;
    }
    return adapter.get(key, preferences);
  }

  /**
   * Change this preference's stored value to {@code value}. A value of {@code null} will delete the
   * preference.
   */
  @Override
  public void set(@Nullable T value) {
    SharedPreferences.Editor editor = preferences.edit();
    if (value == null) {
      editor.remove(key);
    } else {
      adapter.set(key, value, editor);
    }
    editor.apply();
  }

  /** Returns true if this preference has a stored value. */
  @Override
  public boolean isSet() {
    return preferences.contains(key);
  }

  /** Delete the stored value for this preference, if any. */
  @Override
  public void delete() {
    set(null);
  }

  /**
   * Observe changes to this preference. The current value or {@link #defaultValue()} will be
   * emitted on first subscribe.
   */
  @Override
  @CheckResult @NonNull
  public Observable<T> asObservable() {
    return values;
  }

  /**
   * An action which stores a new value for this preference. Passing {@code null} will delete the
   * preference.
   */
  @Override
  @CheckResult @NonNull
  public Action1<? super T> asAction() {
    return new Action1<T>() {
      @Override public void call(T value) {
        set(value);
      }
    };
  }
}
