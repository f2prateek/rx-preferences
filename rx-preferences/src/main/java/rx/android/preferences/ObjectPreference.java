package rx.android.preferences;

import android.content.SharedPreferences;
import java.io.IOException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static rx.android.preferences.Utils.isNullOrEmpty;

public class ObjectPreference<T> {
  private final Converter<T> converter;
  private final SharedPreferences sharedPreferences;
  private final String key;
  private final T defaultValue;
  private T value;

  /**
   * Convert a byte stream to and from a concrete type.
   *
   * @param <T> Object type.
   */
  public interface Converter<T> {
    /** Converts bytes to an object. */
    T fromString(String string) throws IOException;

    /** Converts o to bytes written to the specified stream. */
    String toString(T o) throws IOException;
  }

  public ObjectPreference(Converter<T> converter, SharedPreferences preferences, String key) {
    this(converter, preferences, key, null);
  }

  public ObjectPreference(Converter<T> converter, SharedPreferences sharedPreferences, String key,
      T defaultValue) {
    if (converter == null) {
      throw new IllegalArgumentException("converter == null");
    }
    if (sharedPreferences == null) {
      throw new IllegalArgumentException("sharedPreferences must not be null");
    }
    if (isNullOrEmpty(key)) {
      throw new IllegalArgumentException("key must not be null");
    }
    this.converter = converter;
    this.sharedPreferences = sharedPreferences;
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public T get() throws IOException {
    if (value == null) {
      String data = sharedPreferences.getString(key, null);
      if (data == null) {
        value = defaultValue;
      } else {
        value = converter.fromString(data);
      }
    }
    return value;
  }

  public void set(T value) throws IOException {
    sharedPreferences.edit().putString(key, converter.toString(value)).commit();
    this.value = value;
  }

  public void delete() {
    sharedPreferences.edit().remove(key).commit();
  }

  public Observable<T> asObservable() {
    return Observable.create(new OnSubscribeFromPreference());
  }

  class OnSubscribeFromPreference implements Observable.OnSubscribe<T> {

    // Publish the latest value to the subscriber
    private void publish(final Subscriber<? super T> subscriber) {
      try {
        T value = get();
        subscriber.onNext(value);
      } catch (IOException e) {
        subscriber.onError(e);
      }
    }

    @Override public void call(final Subscriber<? super T> subscriber) {
      publish(subscriber);

      final Subscription subscription = SharedPreferencesObservable.observe(sharedPreferences)
          .filter(new Func1<String, Boolean>() {
            @Override public Boolean call(String s) {
              return key.equals(s);
            }
          })
          .subscribe(new EndlessObserver<String>() {
            @Override public void onNext(String s) {
              publish(subscriber);
            }
          });

      subscriber.add(Subscriptions.create(new Action0() {
        @Override public void call() {
          subscription.unsubscribe();
        }
      }));
    }
  }
}