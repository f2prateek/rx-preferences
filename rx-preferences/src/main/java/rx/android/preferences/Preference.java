package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static rx.android.preferences.Utils.assertNotNull;
import static rx.android.preferences.Utils.assertNotNullOrEmpty;

/**
 * Wraps a preference of type {@link T}.
 * </p>
 * Clients don't need to use this class directly, they'll use one of it's implementations instead.
 */
// We could omit this abstraction and duplicate the logic in the "real" preferences classes (since
// clients will never interact with this directly), and improve performance by eliminating the need
// to box primitive values. However we lose those benefits since the purpose is to emit these values
// as observables.
public abstract class Preference<T> {
  final SharedPreferences sharedPreferences;
  final String key;
  final T defaultValue;

  Preference(SharedPreferences sharedPreferences, String key, T defaultValue) {
    this.sharedPreferences = assertNotNull(sharedPreferences);
    this.key = assertNotNullOrEmpty(key);
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

  public abstract Observable<T> asObservable();

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
