package rx.android.preferences;

import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.robolectric.shadows.ShadowPreferenceManager.getDefaultSharedPreferences;

@RunWith(RobolectricTestRunner.class) //
public class StringPreferenceTest {
  private SharedPreferences sharedPreferences;
  private StringPreference preference;

  @Before public void setUp() {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    preference = new StringPreference(sharedPreferences, "foo", "bar");
  }

  @Test public void toObservable() {
    TestSubscriber<String> o = new TestSubscriber<String>();
    Subscription subscription = preference.toObservable().subscribe(o);
    o.assertValues("bar");

    sharedPreferences.edit().putString("foo", "baz").commit();
    o.assertValues("bar", "baz");

    sharedPreferences.edit().putString("foo", "foo").commit();
    o.assertValues("bar", "baz", "foo");

    subscription.unsubscribe();

    sharedPreferences.edit().putString("foo", "bar").commit();
    o.assertValues("bar", "baz", "foo");
  }
}
