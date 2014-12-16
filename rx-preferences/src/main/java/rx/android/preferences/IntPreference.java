package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class IntPreference extends Preference<Integer> {
  public IntPreference(SharedPreferences preferences, String key) {
    this(preferences, key, 0);
  }

  public IntPreference(SharedPreferences sharedPreferences, String key, int defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Integer get() {
    return sharedPreferences.getInt(key, defaultValue);
  }

  @Override public void set(Integer value) {
    sharedPreferences.edit().putInt(key, value).commit();
  }

  @Override public Observable<Integer> asObservable() {
    return Observable.create(new OnSubscribeFromIntPreference());
  }

  class OnSubscribeFromIntPreference implements Observable.OnSubscribe<Integer> {
    @Override public void call(final Subscriber<? super Integer> subscriber) {
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