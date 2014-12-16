package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class BooleanPreference extends Preference<Boolean> {

  public BooleanPreference(SharedPreferences preferences, String key) {
    this(preferences, key, false);
  }

  public BooleanPreference(SharedPreferences sharedPreferences, String key, boolean defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public Boolean get() {
    return sharedPreferences.getBoolean(key, defaultValue);
  }

  @Override public void set(Boolean value) {
    sharedPreferences.edit().putBoolean(key, value).commit();
  }

  @Override public void delete() {
    sharedPreferences.edit().remove(key).commit();
  }

  @Override public Observable<Boolean> asObservable() {
    return Observable.create(new OnSubscribeFromBooleanPreference());
  }

  class OnSubscribeFromBooleanPreference implements Observable.OnSubscribe<Boolean> {
    @Override public void call(final Subscriber<? super Boolean> subscriber) {
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