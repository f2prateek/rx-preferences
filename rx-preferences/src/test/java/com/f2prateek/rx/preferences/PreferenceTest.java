package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import rx.Subscription;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class) //
public class PreferenceTest {
  private final Point defaultValue = new Point(1, 2);
  private SharedPreferences preferences;
  private Preference<Point> preference;

  @Before public void setUp() {
    preferences = getDefaultSharedPreferences(RuntimeEnvironment.application);
    preferences.edit().clear().commit();
    RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
    preference = rxPreferences.getObject("foo", defaultValue, new PointPreferenceAdapter());
  }

  @Test public void key() {
    assertThat(preference.key()).isEqualTo("foo");
  }

  @Test public void defaultValue() {
    assertThat(preference.defaultValue()).isSameAs(defaultValue);
  }

  @Test public void get() {
    assertThat(preferences.contains("foo")).isFalse();
    assertThat(preference.get()).isEqualTo(new Point(1, 2)); // Default

    preferences.edit().putString("foo", "2,3").commit();
    assertThat(preference.get()).isEqualTo(new Point(2, 3));

    preferences.edit().putString("foo", "3,4").commit();
    assertThat(preference.get()).isEqualTo(new Point(3, 4));
  }

  @Test public void set() {
    assertThat(preferences.contains("foo")).isFalse();

    preference.set(new Point(2, 3));
    assertThat(preferences.getString("foo", null)).isEqualTo("2,3");

    preference.set(new Point(3, 4));
    assertThat(preferences.getString("foo", null)).isEqualTo("3,4");

    preference.set(null);
    assertThat(preferences.getString("foo", null)).isNull();
  }

  @Test public void isSet() {
    assertThat(preferences.contains("foo")).isFalse();
    assertThat(preference.isSet()).isFalse();

    preferences.edit().putString("foo", "2,3").commit();
    assertThat(preference.isSet()).isTrue();

    preferences.edit().remove("foo").commit();
    assertThat(preference.isSet()).isFalse();
  }

  @Test public void delete() {
    preferences.edit().putBoolean("foo", true).commit();
    assertThat(preferences.contains("foo")).isTrue();

    preference.delete();
    assertThat(preferences.contains("foo")).isFalse();
  }

  @Test public void asObservable() {
    TestSubscriber<Point> o = new TestSubscriber<>();
    Subscription subscription = preference.asObservable().subscribe(o);
    o.assertValues(new Point(1, 2)); // Default value.

    preferences.edit().putString("foo", "2,3").commit();
    o.assertValues(new Point(1, 2), new Point(2, 3));

    preferences.edit().putString("foo", "3,4").commit();
    o.assertValues(new Point(1, 2), new Point(2, 3), new Point(3, 4));

    preferences.edit().remove("foo").commit();
    o.assertValues(new Point(1, 2), new Point(2, 3), new Point(3, 4), new Point(1, 2));

    subscription.unsubscribe();
    o.assertValues(new Point(1, 2), new Point(2, 3), new Point(3, 4), new Point(1, 2));
  }

  @Test public void asAction() {
    Action1<? super Point> action = preference.asAction();

    action.call(new Point(2, 3));
    assertThat(preferences.getString("foo", null)).isEqualTo("2,3");

    action.call(new Point(3, 4));
    assertThat(preferences.getString("foo", null)).isEqualTo("3,4");

    action.call(null);
    assertThat(preferences.getString("foo", null)).isNull();
  }
}
