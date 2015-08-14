package rx.android.preferences;

import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.robolectric.shadows.ShadowPreferenceManager.getDefaultSharedPreferences;

@RunWith(RobolectricTestRunner.class) //
public class SharedPreferencesObservableTest {
  private SharedPreferences sharedPreferences;
  private Observable<String> observable;

  @Before public void setUp() {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    observable = SharedPreferencesObservable.observe(sharedPreferences);
  }

  @Test public void keys() {
    TestSubscriber<String> o = new TestSubscriber<String>();
    Subscription subscription = observable.subscribe(o);
    o.assertNoValues();

    sharedPreferences.edit().putBoolean("foo", false).commit();
    o.assertValues("foo");

    sharedPreferences.edit().putString("bar", "baz").commit();
    o.assertValues("foo", "bar");

    subscription.unsubscribe();
    sharedPreferences.edit().putFloat("qux", 42).commit();
    o.assertValues("foo", "bar");
  }
}
