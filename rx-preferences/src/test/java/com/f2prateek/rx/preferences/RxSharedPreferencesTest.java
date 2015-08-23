package com.f2prateek.rx.preferences;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class) //
@SuppressLint("CommitPrefEdits") //
@SuppressWarnings({ "ResourceType", "ConstantConditions" }) //
public class RxSharedPreferencesTest {
  private RxSharedPreferences rxPreferences;

  @Before public void setUp() {
    SharedPreferences preferences = getDefaultSharedPreferences(RuntimeEnvironment.application);
    preferences.edit().clear().commit();
    rxPreferences = RxSharedPreferences.create(preferences);
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

  @Test public void enumNullKeyThrows() {
    try {
      rxPreferences.getEnum(null, Roshambo.class);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getEnum(null, Roshambo.ROCK, Roshambo.class);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void enumNullClassThrows() {
    try {
      rxPreferences.getEnum("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("enumClass == null");
    }
    try {
      rxPreferences.getEnum("key", Roshambo.ROCK, null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("enumClass == null");
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

  @Test public void objectNullKeyThrows() {
    try {
      rxPreferences.getObject(null, new PointPreferenceAdapter());
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
    try {
      rxPreferences.getObject(null, new Point(1, 2), new PointPreferenceAdapter());
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("key == null");
    }
  }

  @Test public void objectNullAdapterThrows() {
    try {
      rxPreferences.getObject("key", null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("adapter == null");
    }
    try {
      rxPreferences.getObject("key", new Point(1, 2), null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("adapter == null");
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
}
