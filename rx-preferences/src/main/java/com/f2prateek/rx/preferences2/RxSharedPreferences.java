package com.f2prateek.rx.preferences2;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.annotation.CheckResult;

import java.util.Collections;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

import static com.f2prateek.rx.preferences2.Preconditions.checkNotNull;

/**
 * A factory for reactive {@link Preference} objects.
 */
@SuppressWarnings("WeakerAccess")
public final class RxSharedPreferences {
  private static final Float DEFAULT_FLOAT = 0f;
  private static final Integer DEFAULT_INTEGER = 0;
  private static final Boolean DEFAULT_BOOLEAN = false;
  private static final Long DEFAULT_LONG = 0L;
  private static final String DEFAULT_STRING = "";
  private final SharedPreferences preferences;
  private final Observable<String> keyChanges;

  private RxSharedPreferences(final SharedPreferences preferences) {
    this.preferences = preferences;
    this.keyChanges = Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
        final OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener() {
          @Override
          public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
            emitter.onNext(key);
          }
        };

        emitter.setCancellable(new Cancellable() {
          @Override
          public void cancel() throws Exception {
            preferences.unregisterOnSharedPreferenceChangeListener(listener);
          }
        });

        preferences.registerOnSharedPreferenceChangeListener(listener);
      }
    }).share();
  }

  /**
   * Create an instance of {@link RxSharedPreferences} for {@code preferences}.
   */
  @CheckResult
  public static RxSharedPreferences create(SharedPreferences preferences) {
    checkNotNull(preferences, "preferences == null");
    return new RxSharedPreferences(preferences);
  }

  /**
   * Create a boolean preference for {@code key}. Default is {@code false}.
   */
  @CheckResult
  public Preference<Boolean> getBoolean(String key) {
    return getBoolean(key, DEFAULT_BOOLEAN);
  }

  /**
   * Create a boolean preference for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public Preference<Boolean> getBoolean(String key, Boolean defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, BooleanAdapter.INSTANCE, keyChanges);
  }

  /**
   * Create an enum preference for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public <T extends Enum<T>> Preference<T> getEnum(String key, T defaultValue, Class<T> enumClass) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    checkNotNull(enumClass, "enumClass == null");
    return new RealPreference<>(preferences, key, defaultValue, new EnumAdapter<>(enumClass), keyChanges);
  }

  /**
   * Create a float preference for {@code key}. Default is {@code 0}.
   */
  @CheckResult
  public Preference<Float> getFloat(String key) {
    return getFloat(key, DEFAULT_FLOAT);
  }

  /**
   * Create a float preference for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public Preference<Float> getFloat(String key, Float defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, FloatAdapter.INSTANCE, keyChanges);
  }

  /**
   * Create an integer preference for {@code key}. Default is {@code 0}.
   */
  @CheckResult
  public Preference<Integer> getInteger(String key) {
    return getInteger(key, DEFAULT_INTEGER);
  }

  /**
   * Create an integer preference for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public Preference<Integer> getInteger(String key, Integer defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, IntegerAdapter.INSTANCE, keyChanges);
  }

  /**
   * Create a long preference for {@code key}. Default is {@code 0}.
   */
  @CheckResult
  public Preference<Long> getLong(String key) {
    //noinspection UnnecessaryBoxing
    return getLong(key, DEFAULT_LONG);
  }

  /**
   * Create a long preference for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public Preference<Long> getLong(String key, Long defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, LongAdapter.INSTANCE, keyChanges);
  }

  /**
   * Create a preference for type {@code T} for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public <T> Preference<T> getObject(String key, T defaultValue,
                                     Preference.Converter<T> converter) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    checkNotNull(converter, "converter == null");
    return new RealPreference<>(preferences, key, defaultValue,
        new ConverterAdapter<>(converter), keyChanges);
  }

  /**
   * Create a string preference for {@code key}. Default is {@code ""}.
   */
  @CheckResult
  public Preference<String> getString(String key) {
    return getString(key, DEFAULT_STRING);
  }

  /**
   * Create a string preference for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public Preference<String> getString(String key, String defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, StringAdapter.INSTANCE, keyChanges);
  }

  /**
   * Create a string set preference for {@code key}. Default is an empty set. Note that returned set
   * value will always be unmodifiable.
   */
  @CheckResult
  public Preference<Set<String>> getStringSet(String key) {
    return getStringSet(key, Collections.<String>emptySet());
  }

  /**
   * Create a string set preference for {@code key} with a default of {@code defaultValue}.
   */
  @CheckResult
  public Preference<Set<String>> getStringSet(String key, Set<String> defaultValue) {
    checkNotNull(key, "key == null");
    checkNotNull(defaultValue, "defaultValue == null");
    return new RealPreference<>(preferences, key, defaultValue, StringSetAdapter.INSTANCE, keyChanges);
  }

  public void clear() {
    preferences.edit().clear().apply();
  }
}
