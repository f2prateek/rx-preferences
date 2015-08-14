package rx.android.preferences;

import android.content.SharedPreferences;
import java.io.IOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import rx.Subscription;
import rx.android.preferences.ObjectPreference.Converter;
import rx.observers.TestSubscriber;

import static org.robolectric.shadows.ShadowPreferenceManager.getDefaultSharedPreferences;

@RunWith(RobolectricTestRunner.class) //
public class ObjectPreferenceTest {
  private static final Converter<String> converter = new Converter<String>() {
    @Override public String fromString(String string) throws IOException {
      return string;
    }

    @Override public String toString(String string) throws IOException {
      return string;
    }
  };

  private SharedPreferences sharedPreferences;
  private ObjectPreference<String> preference;

  @Before public void setUp() throws Exception {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    preference = new ObjectPreference<String>(sharedPreferences, converter, "foo", "bar");
  }

  @Ignore("Emits duplicates because of cached value field")
  @Test public void toObservable() throws Exception {
    TestSubscriber<String> o = new TestSubscriber<String>();
    Subscription subscription = preference.toObservable().subscribe(o);
    o.assertValues("bar");

    sharedPreferences.edit().putString("foo", "baz").commit();
    o.assertValues("bar", "baz");

    subscription.unsubscribe();
    sharedPreferences.edit().putString("foo", "foo").commit();
    o.assertValues("bar", "baz");
  }
}
