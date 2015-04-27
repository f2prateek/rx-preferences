package rx.android.preferences;

import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import rx.Observer;
import rx.Subscription;
import rx.observers.TestObserver;

import static rx.android.preferences.Random.nextString;
import static rx.android.preferences.TestUtils.verifyNoMoreInteractionsWithObserver;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.robolectric.shadows.ShadowPreferenceManager.getDefaultSharedPreferences;

@RunWith(RobolectricTestRunner.class) //
public class StringPreferenceTest {
  SharedPreferences sharedPreferences;
  StringPreference preference;
  String nextValue;

  @Before public void setUp() {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    nextValue = null;
    preference = new StringPreference(sharedPreferences, "bar", nextValue);
  }

  @Test public void subscriberIsInvoked() {
    Observer<String> observer = mock(Observer.class);
    InOrder inOrder = inOrder(observer);

    preference.toObservable().subscribe(observer);
    inOrder.verify(observer).onNext(nextValue);

    nextValue = nextString();
    sharedPreferences.edit().putString("bar", nextValue).commit();
    inOrder.verify(observer).onNext(nextValue);

    nextValue = nextString();
    preference.set(nextValue);
    inOrder.verify(observer).onNext(nextValue);
  }

  @Test public void unsubscribedSubscriberIsNotInvoked() {
    Observer<String> observer = mock(Observer.class);
    Subscription subscription = preference.toObservable() //
        .subscribe(new TestObserver<String>(observer));

    InOrder inOrder = inOrder(observer);
    inOrder.verify(observer).onNext(nextValue);

    subscription.unsubscribe();
    sharedPreferences.edit().putString("foo", nextString()).commit();
    preference.set(nextString());
    verifyNoMoreInteractionsWithObserver(inOrder, observer);
  }
}
