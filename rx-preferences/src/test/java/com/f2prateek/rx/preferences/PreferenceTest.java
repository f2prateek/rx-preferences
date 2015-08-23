package com.f2prateek.rx.preferences;

import android.annotation.SuppressLint;
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
import static com.f2prateek.rx.preferences.Roshambo.ROCK;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class) //
@SuppressLint({ "NewApi", "CommitPrefEdits" }) //
public class PreferenceTest {
  private final PointPreferenceAdapter pointAdapter = new PointPreferenceAdapter();

  private SharedPreferences preferences;
  private RxSharedPreferences rxPreferences;

  @Before public void setUp() {
    preferences = getDefaultSharedPreferences(RuntimeEnvironment.application);
    preferences.edit().clear().commit();
    rxPreferences = RxSharedPreferences.create(preferences);
  }

  @Test public void key() {
    Preference<String> preference = rxPreferences.getString("foo");
    assertThat(preference.key()).isEqualTo("foo");
  }

  @Test public void defaultDefaultValue() {
    assertThat(rxPreferences.getBoolean("foo1").defaultValue()).isNull();
    assertThat(rxPreferences.getEnum("foo2", Roshambo.class).defaultValue()).isNull();
    assertThat(rxPreferences.getFloat("foo3").defaultValue()).isNull();
    assertThat(rxPreferences.getInteger("foo4").defaultValue()).isNull();
    assertThat(rxPreferences.getLong("foo5").defaultValue()).isNull();
    assertThat(rxPreferences.getString("foo6").defaultValue()).isNull();
    assertThat(rxPreferences.getStringSet("foo7").defaultValue()).isEmpty();
    assertThat(rxPreferences.getObject("foo8", pointAdapter).defaultValue()).isNull();
  }

  @Test public void defaultValue() {
    assertThat(rxPreferences.getBoolean("foo1", false).defaultValue()).isEqualTo(false);
    assertThat(rxPreferences.getEnum("foo2", ROCK, Roshambo.class).defaultValue()).isEqualTo(ROCK);
    assertThat(rxPreferences.getFloat("foo3", 1f).defaultValue()).isEqualTo(1f);
    assertThat(rxPreferences.getInteger("foo4", 1).defaultValue()).isEqualTo(1);
    assertThat(rxPreferences.getLong("foo5", 1L).defaultValue()).isEqualTo(1L);
    assertThat(rxPreferences.getString("foo6", "bar").defaultValue()).isEqualTo("bar");
    assertThat(rxPreferences.getStringSet("foo7", singleton("bar")).defaultValue()) //
        .isEqualTo(singleton("bar"));
    assertThat(rxPreferences.getObject("foo8", new Point(1, 2), pointAdapter).defaultValue()) //
        .isEqualTo(new Point(1, 2));
  }

  @Test public void getWithNoValueReturnsDefaultValue() {
    assertThat(rxPreferences.getBoolean("foo1", false).get()).isEqualTo(false);
    assertThat(rxPreferences.getEnum("foo2", ROCK, Roshambo.class).get()).isEqualTo(ROCK);
    assertThat(rxPreferences.getFloat("foo3", 1f).get()).isEqualTo(1f);
    assertThat(rxPreferences.getInteger("foo4", 1).get()).isEqualTo(1);
    assertThat(rxPreferences.getLong("foo5", 1L).get()).isEqualTo(1L);
    assertThat(rxPreferences.getString("foo6", "bar").get()).isEqualTo("bar");
    assertThat(rxPreferences.getStringSet("foo7", singleton("bar")).get()) //
        .isEqualTo(singleton("bar"));
    assertThat(rxPreferences.getObject("foo8", new Point(1, 2), pointAdapter).get()) //
        .isEqualTo(new Point(1, 2));
  }

  @Test public void getWithStoredValue() {
    preferences.edit().putBoolean("foo1", false).commit();
    assertThat(rxPreferences.getBoolean("foo1").get()).isEqualTo(false);
    preferences.edit().putString("foo2", "ROCK").commit();
    assertThat(rxPreferences.getEnum("foo2", Roshambo.class).get()).isEqualTo(ROCK);
    preferences.edit().putFloat("foo3", 1f).commit();
    assertThat(rxPreferences.getFloat("foo3").get()).isEqualTo(1f);
    preferences.edit().putInt("foo4", 1).commit();
    assertThat(rxPreferences.getInteger("foo4").get()).isEqualTo(1);
    preferences.edit().putLong("foo5", 1L).commit();
    assertThat(rxPreferences.getLong("foo5").get()).isEqualTo(1L);
    preferences.edit().putString("foo6", "bar").commit();
    assertThat(rxPreferences.getString("foo6").get()).isEqualTo("bar");
    preferences.edit().putStringSet("foo7", singleton("bar")).commit();
    assertThat(rxPreferences.getStringSet("foo7").get()).isEqualTo(singleton("bar"));
    preferences.edit().putString("foo8", "1,2").commit();
    assertThat(rxPreferences.getObject("foo8", pointAdapter).get()).isEqualTo(new Point(1, 2));
  }

  @Test public void set() {
    rxPreferences.getBoolean("foo1").set(false);
    assertThat(preferences.getBoolean("foo1", true)).isFalse();
    rxPreferences.getEnum("foo2", Roshambo.class).set(ROCK);
    assertThat(preferences.getString("foo2", null)).isEqualTo("ROCK");
    rxPreferences.getFloat("foo3").set(1f);
    assertThat(preferences.getFloat("foo3", 0f)).isEqualTo(1f);
    rxPreferences.getInteger("foo4").set(1);
    assertThat(preferences.getInt("foo4", 0)).isEqualTo(1);
    rxPreferences.getLong("foo5").set(1L);
    assertThat(preferences.getLong("foo5", 0L)).isEqualTo(1L);
    rxPreferences.getString("foo6").set("bar");
    assertThat(preferences.getString("foo6", null)).isEqualTo("bar");
    rxPreferences.getStringSet("foo7").set(singleton("bar"));
    assertThat(preferences.getStringSet("foo7", null)).isEqualTo(singleton("bar"));
    rxPreferences.getObject("foo8", pointAdapter).set(new Point(1, 2));
    assertThat(preferences.getString("foo8", null)).isEqualTo("1,2");
  }

  @Test public void setNullDeletes() {
    preferences.edit().putBoolean("foo1", true).commit();
    rxPreferences.getBoolean("foo1").set(null);
    assertThat(preferences.contains("foo1")).isFalse();

    preferences.edit().putString("foo2", "ROCK").commit();
    rxPreferences.getEnum("foo2", Roshambo.class).set(null);
    assertThat(preferences.contains("foo2")).isFalse();

    preferences.edit().putFloat("foo3", 1f).commit();
    rxPreferences.getFloat("foo3").set(null);
    assertThat(preferences.contains("foo3")).isFalse();

    preferences.edit().putInt("foo4", 1).commit();
    rxPreferences.getInteger("foo4").set(null);
    assertThat(preferences.contains("foo4")).isFalse();

    preferences.edit().putLong("foo5", 1L).commit();
    rxPreferences.getLong("foo5").set(null);
    assertThat(preferences.contains("foo5")).isFalse();

    preferences.edit().putString("foo6", "bar").commit();
    rxPreferences.getString("foo6").set(null);
    assertThat(preferences.contains("foo6")).isFalse();

    preferences.edit().putStringSet("foo7", singleton("bar")).commit();
    rxPreferences.getStringSet("foo7").set(null);
    assertThat(preferences.contains("foo7")).isFalse();

    preferences.edit().putString("foo8", "1,2").commit();
    rxPreferences.getObject("foo8", pointAdapter).set(null);
    assertThat(preferences.contains("foo8")).isFalse();
  }

  @Test public void isSet() {
    Preference<String> preference = rxPreferences.getString("foo");

    assertThat(preferences.contains("foo")).isFalse();
    assertThat(preference.isSet()).isFalse();

    preferences.edit().putString("foo", "2,3").commit();
    assertThat(preference.isSet()).isTrue();

    preferences.edit().remove("foo").commit();
    assertThat(preference.isSet()).isFalse();
  }

  @Test public void delete() {
    Preference<String> preference = rxPreferences.getString("foo");

    preferences.edit().putBoolean("foo", true).commit();
    assertThat(preferences.contains("foo")).isTrue();

    preference.delete();
    assertThat(preferences.contains("foo")).isFalse();
  }

  @Test public void asObservable() {
    Preference<String> preference = rxPreferences.getString("foo", "bar");

    TestSubscriber<String> o = new TestSubscriber<>();
    Subscription subscription = preference.asObservable().subscribe(o);
    o.assertValues("bar");

    preferences.edit().putString("foo", "baz").commit();
    o.assertValues("bar", "baz");

    preferences.edit().remove("foo").commit();
    o.assertValues("bar", "baz", "bar");

    subscription.unsubscribe();
    preferences.edit().putString("foo", "foo").commit();
    o.assertValues("bar", "baz", "bar");
  }

  @Test public void asAction() {
    Preference<String> preference = rxPreferences.getString("foo");
    Action1<? super String> action = preference.asAction();

    action.call("bar");
    assertThat(preferences.getString("foo", null)).isEqualTo("bar");

    action.call("baz");
    assertThat(preferences.getString("foo", null)).isEqualTo("baz");

    action.call(null);
    assertThat(preferences.contains("foo")).isFalse();
  }
}
