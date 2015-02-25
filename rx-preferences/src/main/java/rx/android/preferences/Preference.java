package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static rx.android.preferences.Utils.isNullOrEmpty;

/**
 * Wraps a preference of type {@link T}.
 * </p>
 * Clients don't need to use this class directly, they'll use one of it's implementations instead.
 */
// We could omit this abstraction and duplicate the logic in the "real" preferences classes (since
// clients will never interact with this directly). This would improve performance by eliminating
// the need to box primitive values. However, we'll have to box them while emitting them as
// observables - so I don't think it's quite as much of a win.
public abstract class Preference<T> {
  final SharedPreferences sharedPreferences;
  final String key;
  final T defaultValue;

  Preference(SharedPreferences sharedPreferences, String key, T defaultValue) {
    if (sharedPreferences == null) {
      throw new IllegalArgumentException("sharedPreferences must not be null");
    }
    if (isNullOrEmpty(key)) {
      throw new IllegalArgumentException("key must not be null");
    }
    this.sharedPreferences = sharedPreferences;
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public abstract T get();

  public abstract void set(T value);

  public boolean isSet() {
    return sharedPreferences.contains(key);
  }

  public void delete() {
    sharedPreferences.edit().remove(key).commit();
  }

  public Observable<T> asObservable() {
    return Observable.create(new OnSubscribeFromPreference());
  }

  class OnSubscribeFromPreference implements Observable.OnSubscribe<T> {
    @Override public void call(final Subscriber<? super T> subscriber) {
      subscriber.onNext(get());

      final Subscription subscription = SharedPreferencesObservable.observe(sharedPreferences)
          .filter(new Func1<String, Boolean>() {
            @Override public Boolean call(String s) {
              return key.equals(s);
            }
          })
          .subscribe(new EndlessObserver<String>() {
            @Override public void onNext(String s) {
              subscriber.onNext(get());
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
