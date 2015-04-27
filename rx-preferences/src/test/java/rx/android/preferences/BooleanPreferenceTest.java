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

@RunWith(RobolectricTestRunner.class) //
public class BooleanPreferenceTest {
  SharedPreferences sharedPreferences;
  BooleanPreference preference;
  boolean nextValue;

  @Before public void setUp() {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    nextValue = false;
    preference = new BooleanPreference(sharedPreferences, "foo", nextValue);
  }

  @Test public void subscriberIsInvoked() {
    Observer<Boolean> observer = mock(Observer.class);
    InOrder inOrder = inOrder(observer);

    preference.toObservable().subscribe(observer);
    inOrder.verify(observer).onNext(nextValue);

    nextValue = Random.nextBoolean();
    sharedPreferences.edit().putBoolean("foo", nextValue).commit();
    inOrder.verify(observer).onNext(nextValue);

    nextValue = Random.nextBoolean();
    preference.set(nextValue);
    inOrder.verify(observer).onNext(nextValue);
  }

  @Test public void unsubscribedSubscriberIsNotInvoked() {
    Observer<Boolean> observer = mock(Observer.class);
    Subscription subscription = preference.toObservable() //
        .subscribe(new TestObserver<Boolean>(observer));
    InOrder inOrder = inOrder(observer);

    inOrder.verify(observer).onNext(nextValue);

    subscription.unsubscribe();
    sharedPreferences.edit().putBoolean("foo", Random.nextBoolean()).commit();
    preference.set(Random.nextBoolean());
    TestUtils.verifyNoMoreInteractionsWithObserver(inOrder, observer);
  }
}
