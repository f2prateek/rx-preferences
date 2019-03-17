package com.f2prateek.rx.preferences2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class) //
@SuppressLint("ApplySharedPref") //
@SuppressWarnings({ "ResourceType", "ConstantConditions" }) //
public class RxSharedPreferencesTest {
  private RxSharedPreferences rxPreferences;

  @Before public void setUp() {
    SharedPreferences preferences = getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
    preferences.edit().clear().commit();
    rxPreferences = RxSharedPreferences.create(preferences);
  }

  @Test public void clearRemovesAllPreferences() {
    Preference<String> preference = rxPreferences.getString("key", "default");
    preference.set("foo");
    rxPreferences.clear();
    assertThat(preference.get()).isEqualTo("default");
  }

  @Test public void createWithNullThrows() {
    try {
      RxSharedPreferences.create(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("preferences == null");
    }
  }

  @Test public void booleanNullKeyThrows() {
    try {
      rxPreferences.getBoolean(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getBoolean(null, false);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void booleanNullDefaultValueThrows() {
    try {
      rxPreferences.getBoolean("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }

  @Test public void enumNullKeyThrows() {
    try {
      rxPreferences.getEnum(null, Roshambo.ROCK, Roshambo.class);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void enumNullClassThrows() {
    try {
      rxPreferences.getEnum("key", Roshambo.ROCK, null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("enumClass == null");
    }
  }

  @Test public void enumNullDefaultValueThrows() {
    try {
      rxPreferences.getEnum("key", null, Roshambo.class);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }

  @Test public void floatNullKeyThrows() {
    try {
      rxPreferences.getFloat(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getFloat(null, 0f);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void floatNullDefaultValueThrows() {
    try {
      rxPreferences.getFloat("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }

  @Test public void integerNullKeyThrows() {
    try {
      rxPreferences.getInteger(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getInteger(null, 0);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void integerNullDefaultValueThrows() {
    try {
      rxPreferences.getInteger("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }

  @Test public void longNullKeyThrows() {
    try {
      rxPreferences.getLong(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getLong(null, 0L);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void longNullDefaultValueThrows() {
    try {
      rxPreferences.getLong("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }

  @Test public void objectNullKeyThrows() {
    try {
      rxPreferences.getObject(null, new Point(1, 2), new PointPreferenceConverter());
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void objectNullAdapterThrows() {
    try {
      rxPreferences.getObject("key", new Point(1, 2), null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("converter == null");
    }
  }

  @Test public void objectNullDefaultValueThrows() {
    try {
      rxPreferences.getObject("key", null, new PointPreferenceConverter());
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }

  @Test public void stringNullKeyThrows() {
    try {
      rxPreferences.getString(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getString(null, "default");
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void stringNullDefaultValueThrows() {
    try {
      rxPreferences.getString("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }

  @Test public void stringSetNullKeyThrows() {
    try {
      rxPreferences.getStringSet(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getStringSet(null, Collections.<String>emptySet());
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void stringSetNullDefaultValueThrows() {
    try {
      rxPreferences.getStringSet("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("defaultValue == null");
    }
  }
}
