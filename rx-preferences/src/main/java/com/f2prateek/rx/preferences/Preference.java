package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.functions.Action1;

/** A preference of type {@link T}. Instances can be created from {@link RxSharedPreferences}. */
public interface Preference<T> {

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

  /** The key for which this preference will store and retrieve values. */
  @NonNull
  String key();

  /** The value used if none is stored. May be {@code null}. */
  @Nullable
  T defaultValue();

  /**
   * Retrieve the current value for this preference. Returns {@link #defaultValue()} if no value is
   * set.
   */
  @Nullable
  T get();

  /**
   * Change this preference's stored value to {@code value}. A value of {@code null} will delete the
   * preference.
   */
  void set(@Nullable T value);

  /** Returns true if this preference has a stored value. */
  boolean isSet();

  /** Delete the stored value for this preference, if any. */
  void delete();

  /**
   * Observe changes to this preference. The current value or {@link #defaultValue()} will be
   * emitted on first subscribe.
   */
  @CheckResult @NonNull
  Observable<T> asObservable();

  /**
   * An action which stores a new value for this preference. Passing {@code null} will delete the
   * preference.
   */
  @CheckResult @NonNull
  Action1<? super T> asAction();
}
