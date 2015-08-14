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
public class BooleanPreferenceTest {
  SharedPreferences sharedPreferences;
  BooleanPreference preference;

  @Before public void setUp() {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    preference = new BooleanPreference(sharedPreferences, "foo", false);
  }

  @Test public void subscriberIsInvoked() {
    Observer<Boolean> observer = mock(Observer.class);
    InOrder inOrder = inOrder(observer);

    preference.toObservable().subscribe(observer);
    inOrder.verify(observer).onNext(false);

    sharedPreferences.edit().putBoolean("foo", true).commit();
    inOrder.verify(observer).onNext(true);

    preference.set(false);
    inOrder.verify(observer).onNext(false);
  }

  @Test public void unsubscribedSubscriberIsNotInvoked() {
    Observer<Boolean> observer = mock(Observer.class);
    Subscription subscription = preference.toObservable() //
        .subscribe(new TestObserver<Boolean>(observer));
    InOrder inOrder = inOrder(observer);

    inOrder.verify(observer).onNext(false);

    subscription.unsubscribe();
    sharedPreferences.edit().putBoolean("foo", true).commit();
    preference.set(false);
    verifyNoMoreInteractionsWithObserver(inOrder, observer);
  }
}
