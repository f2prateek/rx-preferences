package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * A preference of type {@link T}. Instances can be created from {@link RxSharedPreferences}.
 */
public interface Preference<T> {
  /**
   * The key for which this preference will store and retrieve values.
   */
  String key();

  /**
   * The value used if none is stored.
   */
  T defaultValue();

  /**
   * Retrieve the current value for this preference. Returns {@link #defaultValue()} if no value is
   * set.
   */
  T get();

  /**
   * Change this preference's stored value to {@code value}.
   */
  void set(T value);

  /**
   * Returns true if this preference has a stored value.
   */
  boolean isSet();

  /**
   * Delete the stored value for this preference, if any.
   */
  void delete();

  /**
   * Observe changes to this preference. The current value or {@link #defaultValue()} will be
   * emitted on first subscribe.
   */
  @CheckResult
  Observable<T> asObservable();

  /**
   * An action which stores a new value for this preference.
   */
  @CheckResult
  Consumer<? super T> asConsumer();

  /**
   * Converts instances of {@code T} to be stored and retrieved as Strings in {@link
   * SharedPreferences}.
   */
  interface Converter<T> {
    /**
     * Deserialize to an instance of {@code T}. The input is retrieved from {@link
     * SharedPreferences#getString(String, String)}.
     */
    T deserialize(String serialized);

    /**
     * Serialize the {@code value} to a String. The result will be used with {@link
     * SharedPreferences.Editor#putString(String, String)}.
     */
    String serialize(T value);
  }
}
