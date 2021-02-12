package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static com.f2prateek.rx.preferences2.Preconditions.checkNotNull;

/** A factory for reactive {@link Preference} objects. */
public final class RxSharedPreferences {
  private static final Float DEFAULT_FLOAT = 0f;
  private static final Integer DEFAULT_INTEGER = 0;
  private static final Boolean DEFAULT_BOOLEAN = false;
  private static final Long DEFAULT_LONG = 0L;
  private static final String DEFAULT_STRING = "";

  static final String NULL_KEY_EMISSION = "null_key_emission";

  /** Create an instance of {@link RxSharedPreferences} for {@code preferences}. */
  @CheckResult @NonNull
  public static RxSharedPreferences create(@NonNull SharedPreferences preferences) {
    checkNotNull(preferences, "preferences == null");
    return new RxSharedPreferences(preferences);
  }

  private final SharedPreferences preferences;
  private final Observable<String> keyChanges;

  private RxSharedPreferences(final SharedPreferences preferences) {
    this.preferences = preferences;
    this.keyChanges = Observable.create(new ObservableOnSubscribe<String>() {
      @Override public void subscribe(final ObservableEmitter<String> emitter) {
        final OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener() {
          @Override
          public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
            if (key == null) {
              emitter.onNext(NULL_KEY_EMISSION);
            } else {
              emitter.onNext(key);
            }
          }
        };

        emitter.setCancellable(new Cancellable() {
          @Override public void cancel() {
            preferences.unregisterOnSharedPreferenceChangeListener(listener);
          }
        });

        preferences.registerOnSharedPreferenceChangeListener(listener);
      }
    }).share();
  }

  /** Create a boolean preference for {@code key}. Default is {@code false}. */
  @CheckResult @NonNull
  public Preference<Boolean> getBoolean(@NonNull String key) {
    return getBoolean(key, DEFAULT_BOOLEAN);
  }

  /** Create a boolean preference for {@code key} with a default of {@code defaultValue}. */
  @CheckResult @NonNull
  public Preference<Boolean> getBoolean(@NonNull String key, @NonNull Boolean defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, BooleanAdapter.INSTANCE, keyChanges);
  }

  /** Create an enum preference for {@code key} with a default of {@code defaultValue}. */
  @CheckResult @NonNull
  public <T extends Enum<T>> Preference<T> getEnum(@NonNull String key, @NonNull T defaultValue,
      @NonNull Class<T> enumClass) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    checkNotNull(enumClass, "enumClass == null");
    return new RealPreference<>(preferences, key, defaultValue, new EnumAdapter<>(enumClass), keyChanges);
  }

  /** Create a float preference for {@code key}. Default is {@code 0}. */
  @CheckResult @NonNull
  public Preference<Float> getFloat(@NonNull String key) {
    return getFloat(key, DEFAULT_FLOAT);
  }

  /** Create a float preference for {@code key} with a default of {@code defaultValue}. */
  @CheckResult @NonNull
  public Preference<Float> getFloat(@NonNull String key, @NonNull Float defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, FloatAdapter.INSTANCE, keyChanges);
  }

  /** Create an integer preference for {@code key}. Default is {@code 0}. */
  @CheckResult @NonNull
  public Preference<Integer> getInteger(@NonNull String key) {
    return getInteger(key, DEFAULT_INTEGER);
  }

  /** Create an integer preference for {@code key} with a default of {@code defaultValue}. */
  @CheckResult @NonNull
  public Preference<Integer> getInteger(@NonNull String key, @NonNull Integer defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, IntegerAdapter.INSTANCE, keyChanges);
  }

  /** Create a long preference for {@code key}. Default is {@code 0}. */
  @CheckResult @NonNull
  public Preference<Long> getLong(@NonNull String key) {
    return getLong(key, DEFAULT_LONG);
  }

  /** Create a long preference for {@code key} with a default of {@code defaultValue}. */
  @CheckResult @NonNull
  public Preference<Long> getLong(@NonNull String key, @NonNull Long defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, LongAdapter.INSTANCE, keyChanges);
  }

  /**
   * Create a preference for type {@code T} for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult @NonNull public <T> Preference<T> getObject(@NonNull String key,
      @NonNull T defaultValue, @NonNull Preference.Converter<T> converter) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    checkNotNull(converter, "converter == null");
    return new RealPreference<>(preferences, key, defaultValue,
        new ConverterAdapter<>(converter), keyChanges);
  }

  /** Create a string preference for {@code key}. Default is {@code ""}. */
  @CheckResult @NonNull
  public Preference<String> getString(@NonNull String key) {
    return getString(key, DEFAULT_STRING);
  }

  /** Create a string preference for {@code key} with a default of {@code defaultValue}. */
  @CheckResult @NonNull
  public Preference<String> getString(@NonNull String key, @NonNull String defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, StringAdapter.INSTANCE, keyChanges);
  }

  /**
   * Create a string set preference for {@code key}. Default is an empty set. Note that returned set
   * value will always be unmodifiable.
   */
  @RequiresApi(HONEYCOMB)
  @CheckResult @NonNull
  public Preference<Set<String>> getStringSet(@NonNull String key) {
    return getStringSet(key, Collections.<String>emptySet());
  }

  /** Create a string set preference for {@code key} with a default of {@code defaultValue}. */
  @RequiresApi(HONEYCOMB)
  @CheckResult @NonNull
  public Preference<Set<String>> getStringSet(@NonNull String key,
      @NonNull Set<String> defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, StringSetAdapter.INSTANCE, keyChanges);
  }
  
  public void clear() {
    preferences.edit().clear().apply();
  }
}
