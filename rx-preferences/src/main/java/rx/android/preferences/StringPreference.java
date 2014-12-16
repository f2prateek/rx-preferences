package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class StringPreference extends Preference<String> {
  public StringPreference(SharedPreferences preferences, String key) {
    this(preferences, key, null);
  }

  public StringPreference(SharedPreferences sharedPreferences, String key, String defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  @Override public String get() {
    return sharedPreferences.getString(key, defaultValue);
  }

  @Override public void set(String value) {
    sharedPreferences.edit().putString(key, value).commit();
  }

  @Override public Observable<String> asObservable() {
    return Observable.create(new OnSubscribeFromStringPreference());
  }

  class OnSubscribeFromStringPreference implements Observable.OnSubscribe<String> {

    @Override public void call(final Subscriber<? super String> subscriber) {
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