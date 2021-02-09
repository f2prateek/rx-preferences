package com.f2prateek.rx.preferences2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import io.reactivex.functions.Consumer;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.f2prateek.rx.preferences2.Roshambo.PAPER;
import static com.f2prateek.rx.preferences2.Roshambo.ROCK;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(RobolectricTestRunner.class) //
@SuppressLint({ "NewApi", "ApplySharedPref" }) //
public class PreferenceTest {
  @Rule public final RecordingObserver.Rule observerRule = new RecordingObserver.Rule();

  private final PointPreferenceConverter pointConverter = new PointPreferenceConverter();

  private SharedPreferences preferences;
  private RxSharedPreferences rxPreferences;

  @Before public void setUp() {
    preferences = getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
    preferences.edit().clear().commit();
    rxPreferences = RxSharedPreferences.create(preferences);
  }

  @Test public void key() {
    Preference<String> preference = rxPreferences.getString("foo");
    assertThat(preference.key()).isEqualTo("foo");
  }

  @Test public void defaultDefaultValue() {
    assertThat(rxPreferences.getBoolean("foo1").defaultValue()).isFalse();
    assertThat(rxPreferences.getFloat("foo3").defaultValue()).isZero();
    assertThat(rxPreferences.getInteger("foo4").defaultValue()).isZero();
    assertThat(rxPreferences.getLong("foo5").defaultValue()).isZero();
    assertThat(rxPreferences.getString("foo6").defaultValue()).isEqualTo("");
    assertThat(rxPreferences.getStringSet("foo7").defaultValue()).isEmpty();
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
    assertThat(rxPreferences.getObject("foo8", new Point(1, 2), pointConverter).defaultValue()) //
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
    assertThat(rxPreferences.getObject("foo8", new Point(1, 2), pointConverter).get()) //
        .isEqualTo(new Point(1, 2));
  }

  @Test public void getWithStoredValue() {
    preferences.edit().putBoolean("foo1", false).commit();
    assertThat(rxPreferences.getBoolean("foo1").get()).isEqualTo(false);
    preferences.edit().putString("foo2", "ROCK").commit();
    assertThat(rxPreferences.getEnum("foo2", PAPER, Roshambo.class).get()).isEqualTo(ROCK);
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
    assertThat(rxPreferences.getObject("foo8", new Point(2, 3), pointConverter).get())
        .isEqualTo(new Point(1, 2));
  }

  @Test public void set() {
    rxPreferences.getBoolean("foo1").set(false);
    assertThat(preferences.getBoolean("foo1", true)).isFalse();
    rxPreferences.getEnum("foo2", PAPER, Roshambo.class).set(ROCK);
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
    rxPreferences.getObject("foo8", new Point(2, 3), pointConverter).set(new Point(1, 2));
    assertThat(preferences.getString("foo8", null)).isEqualTo("1,2");
  }

  @SuppressWarnings("ConstantConditions")
  @Test public void setNullThrows() {
    try {
      rxPreferences.getBoolean("foo1").set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }

    try {
      rxPreferences.getEnum("foo2", ROCK, Roshambo.class).set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }

    try {
      rxPreferences.getFloat("foo3").set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }

    try {
      rxPreferences.getInteger("foo4").set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }

    try {
      rxPreferences.getLong("foo5").set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }

    try {
      rxPreferences.getString("foo6").set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }

    try {
      rxPreferences.getStringSet("foo7").set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }

    try {
      rxPreferences.getObject("foo8", new Point(1, 2), pointConverter).set(null);
      fail("Disallow setting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }
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

  @Test public void converterMayNotReturnNull() {
    Preference<Point> preference =
        rxPreferences.getObject("foo", new Point(0, 0), new Preference.Converter<Point>() {
          @SuppressWarnings("ConstantConditions")
          @NonNull @Override public Point deserialize(@NonNull String serialized) {
            return null;
          }

          @SuppressWarnings("ConstantConditions")
          @NonNull @Override public String serialize(@NonNull Point value) {
            return null;
          }
        });
    preferences.edit().putString("foo", "1,2").apply();
    try {
      preference.get();
      fail("Disallow Converter methods from returning null.");
    } catch (NullPointerException expected) {
      assertThat(expected).hasMessage("Deserialized value must not be null from string: 1,2");
    }
    try {
      preference.set(new Point(1, 2));
      fail("Disallow Converter methods from returning null.");
    } catch (NullPointerException expected) {
      assertThat(expected).hasMessage(
          "Serialized string must not be null from value: Point{x=1, y=2}");
    }
  }

  @Test public void stringSetDefaultIsUnmodifiable() {
    Preference<Set<String>> preference = rxPreferences.getStringSet("foo");
    Set<String> stringSet = preference.get();
    try {
      stringSet.add("");
      fail(stringSet.getClass() + " should not be modifiable.");
    } catch (UnsupportedOperationException expected) {
      assertThat(expected).hasNoCause();
    }
  }

  @Test public void stringSetIsUnmodifiable() {
    Preference<Set<String>> preference = rxPreferences.getStringSet("foo");
    preference.set(new LinkedHashSet<String>());
    Set<String> stringSet = preference.get();
    try {
      stringSet.add("");
      fail(stringSet.getClass() + " should not be modifiable.");
    } catch (UnsupportedOperationException expected) {
      assertThat(expected).hasNoCause();
    }
  }

  @Test public void asObservable() {
    Preference<String> preference = rxPreferences.getString("foo", "bar");

    RecordingObserver<String> observer = observerRule.create();
    preference.asObservable().subscribe(observer);
    observer.assertValue("bar");

    preferences.edit().putString("foo", "baz").commit();
    observer.assertValue("baz");

    preferences.edit().remove("foo").commit();
    observer.assertValue("bar");
  }

  @Ignore("Robolectric needs to be updated to support API 30")
  @Test public void asObservableWhenBackingPrefsCleared() {
    Preference<String> preference = rxPreferences.getString("foo", "bar");

    RecordingObserver<String> observer = observerRule.create();
    preference.asObservable().subscribe(observer);
    observer.assertValue("bar");

    preferences.edit().putString("foo", "baz").commit();
    observer.assertValue("baz");

    preferences.edit().clear().commit();
    observer.assertValue("bar");
  }

  @Test public void asConsumer() throws Exception {
    Preference<String> preference = rxPreferences.getString("foo");
    Consumer<? super String> consumer = preference.asConsumer();

    consumer.accept("bar");
    assertThat(preferences.getString("foo", null)).isEqualTo("bar");

    consumer.accept("baz");
    assertThat(preferences.getString("foo", null)).isEqualTo("baz");

    try {
      consumer.accept(null);
      fail("Disallow accepting null.");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("value == null");
    }
  }

  @Test public void legacyNullString() {
    nullValue("string");
    assertThat(rxPreferences.getString("string", "default").get()).isEqualTo("default");
  }

  @Test public void legacyNullBoolean() {
    nullValue("bool");
    assertThat(rxPreferences.getBoolean("bool", true).get()).isEqualTo(true);
  }

  @Test public void legacyNullEnum() {
    nullValue("enum");
    assertThat(rxPreferences.getEnum("enum", PAPER, Roshambo.class).get()).isEqualTo(PAPER);
  }

  @Test public void legacyNullFloat() {
    nullValue("float");
    assertThat(rxPreferences.getFloat("float", 123.45f).get()).isEqualTo(123.45f);
  }

  @Test public void legacyNullInteger() {
    nullValue("int");
    assertThat(rxPreferences.getInteger("int", 12345).get()).isEqualTo(12345);
  }

  @Test public void legacyNullLong() {
    nullValue("long");
    assertThat(rxPreferences.getLong("long", 12345L).get()).isEqualTo(12345L);
  }

  @Test public void legacyNullObject() {
    nullValue("obj");
    assertThat(rxPreferences.getObject("obj", new Point(10, 11), pointConverter).get())
        .isEqualTo(new Point(10, 11));
  }

  @Test public void legacyNullSet() {
    nullValue("set");
    List<String> strings = asList("able", "baker", "charlie");
    HashSet<String> defaultSet = new HashSet<>(strings);
    HashSet<String> expectedSet = new HashSet<>(strings);
    assertThat(rxPreferences.getStringSet("key", defaultSet).get()).isEqualTo(expectedSet);
  }

  private void nullValue(String key) {
    preferences.edit()
        .putString(key, null)
        .commit();
  }
}
