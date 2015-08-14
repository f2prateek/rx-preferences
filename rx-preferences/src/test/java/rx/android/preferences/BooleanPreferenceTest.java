package rx.android.preferences;

import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import rx.Subscription;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.shadows.ShadowPreferenceManager.getDefaultSharedPreferences;

@RunWith(RobolectricTestRunner.class) //
public class BooleanPreferenceTest {
  private SharedPreferences sharedPreferences;
  private BooleanPreference preference;

  @Before public void setUp() {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    preference = new BooleanPreference(sharedPreferences, "foo", false);
  }

  @Test public void get() {
    assertThat(sharedPreferences.contains("foo")).isFalse();
    assertThat(preference.get()).isFalse(); // Default

    sharedPreferences.edit().putBoolean("foo", true).commit();
    assertThat(preference.get()).isTrue();

    sharedPreferences.edit().putBoolean("foo", false).commit();
    assertThat(preference.get()).isFalse();
  }

  @Test public void set() {
    assertThat(sharedPreferences.contains("foo")).isFalse();

    preference.set(true);
    assertThat(sharedPreferences.getBoolean("foo", false)).isTrue();

    preference.set(false);
    assertThat(sharedPreferences.getBoolean("foo", true)).isFalse();
  }

  @Test public void isSet() {
    assertThat(sharedPreferences.contains("foo")).isFalse();
    assertThat(preference.isSet()).isFalse();

    sharedPreferences.edit().putBoolean("foo", true).commit();
    assertThat(preference.isSet()).isTrue();

    sharedPreferences.edit().remove("foo").commit();
    assertThat(preference.isSet()).isFalse();
  }

  @Test public void delete() {
    sharedPreferences.edit().putBoolean("foo", true).commit();
    assertThat(sharedPreferences.contains("foo")).isTrue();

    preference.delete();
    assertThat(sharedPreferences.contains("foo")).isFalse();
  }

  @Test public void toObservable() {
    TestSubscriber<Boolean> o = new TestSubscriber<Boolean>();
    Subscription subscription = preference.toObservable().subscribe(o);
    o.assertValues(false);

    sharedPreferences.edit().putBoolean("foo", true).commit();
    o.assertValues(false, true);

    sharedPreferences.edit().putBoolean("foo", false).commit();
    o.assertValues(false, true, false);

    subscription.unsubscribe();
    o.assertValues(false, true, false);
  }

  @Test public void toAction() {
    Action1<? super Boolean> action = preference.toAction();

    action.call(true);
    assertThat(sharedPreferences.getBoolean("foo", false)).isTrue();

    action.call(false);
    assertThat(sharedPreferences.getBoolean("foo", true)).isFalse();
  }
}
