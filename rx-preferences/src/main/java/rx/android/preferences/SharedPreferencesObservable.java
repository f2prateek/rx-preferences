package rx.android.preferences;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public final class SharedPreferencesObservable {
  private SharedPreferencesObservable() {
    throw new AssertionError("No Instances");
  }

  public synchronized static Observable<String> observe(final SharedPreferences sharedPreferences) {
    Utils.assertNotNull(sharedPreferences);

    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(final Subscriber<? super String> subscriber) {
        final SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
              @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                  String key) {
                subscriber.onNext(key);
              }
            };

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        subscriber.add(Subscriptions.create(new Action0() {
          @Override public void call() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
          }
        }));
      }
    });
  }
}

