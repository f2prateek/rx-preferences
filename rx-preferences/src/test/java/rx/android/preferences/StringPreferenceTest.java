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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.robolectric.shadows.ShadowPreferenceManager.getDefaultSharedPreferences;
import static rx.android.preferences.TestUtils.verifyNoMoreInteractionsWithObserver;

@RunWith(RobolectricTestRunner.class) //
public class StringPreferenceTest {
  SharedPreferences sharedPreferences;
  StringPreference preference;

  @Before public void setUp() {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    preference = new StringPreference(sharedPreferences, "bar");
  }

  @Test public void subscriberIsInvoked() {
    Observer<String> observer = mock(Observer.class);
    InOrder inOrder = inOrder(observer);

    preference.toObservable().subscribe(observer);
    inOrder.verify(observer).onNext(null);

    sharedPreferences.edit().putString("bar", "foo").commit();
    inOrder.verify(observer).onNext("foo");

    preference.set("baz");
    inOrder.verify(observer).onNext("baz");
  }

  @Test public void unsubscribedSubscriberIsNotInvoked() {
    Observer<String> observer = mock(Observer.class);
    Subscription subscription = preference.toObservable() //
        .subscribe(new TestObserver<String>(observer));

    InOrder inOrder = inOrder(observer);
    inOrder.verify(observer).onNext(null);

    subscription.unsubscribe();
    sharedPreferences.edit().putString("foo", "bar").commit();
    preference.set("baz");
    verifyNoMoreInteractionsWithObserver(inOrder, observer);
  }
}
