package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/** A preference of type {@link T}. Instances can be created from {@link RxSharedPreferences}. */
public final class Preference<T> {
  /** Stores and retrieves instances of {@code T} in {@link SharedPreferences}. */
  public interface Adapter<T> {
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

  private final SharedPreferences preferences;
  private final String key;
  private final T defaultValue;
  private final Adapter<T> adapter;
  private final Observable<T> values;

  Preference(SharedPreferences preferences, final String key, T defaultValue, Adapter<T> adapter,
      Observable<String> keyChanges) {
    this.preferences = preferences;
    this.key = key;
    this.defaultValue = defaultValue;
    this.adapter = adapter;
    this.values = keyChanges
        .filter(new Predicate<String>() {
          @Override public boolean test(String changedKey) throws Exception {
            return key.equals(changedKey);
          }
        })
        .startWith("<init>") // Dummy value to trigger initial load.
        .map(new Function<String, T>() {
          @Override public T apply(String s) throws Exception {
            return get();
          }
        });
  }

  /** The key for which this preference will store and retrieve values. */
  @NonNull
  public String key() {
    return key;
  }

  /** The value used if none is stored. May be {@code null}. */
  @Nullable
  public T defaultValue() {
    return defaultValue;
  }

  /**
   * Retrieve the current value for this preference. Returns {@link #defaultValue()} if no value is
   * set.
   */
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
  public boolean isSet() {
    return preferences.contains(key);
  }

  /** Delete the stored value for this preference, if any. */
  public void delete() {
    set(null);
  }

  /**
   * Observe changes to this preference. The current value or {@link #defaultValue()} will be
   * emitted on first subscribe.
   */
  @CheckResult @NonNull
  public Observable<T> asObservable() {
    return values;
  }

  /**
   * An action which stores a new value for this preference. Passing {@code null} will delete the
   * preference.
   */
  @CheckResult @NonNull
  public Consumer<? super T> asConsumer() {
    return new Consumer<T>() {
      @Override public void accept(T value) throws Exception {
        set(value);
      }
    };
  }
}
