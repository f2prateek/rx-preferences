package rx.android.preferences;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import rx.Observer;
import rx.Subscription;
import rx.observers.TestObserver;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.robolectric.shadows.ShadowPreferenceManager.getDefaultSharedPreferences;

@RunWith(RobolectricTestRunner.class) //
public class ObjectPreferenceTest {
  SharedPreferences sharedPreferences;
  ObjectPreference<String> preference;
  final ObjectPreference.Converter<String> converter = new ObjectPreference.Converter<String>() {
    @Override
    public String fromString(String string) throws IOException {
      return string;
    }

    @Override
    public String toString(String string) throws IOException {
      return string;
    }
  };

  @Before
  public void setUp() throws Exception {
    sharedPreferences = getDefaultSharedPreferences(Robolectric.application);
    sharedPreferences.edit().clear().commit();
    preference = new ObjectPreference<String>(sharedPreferences, converter, "foo");
    preference.set("bar");
  }

  @Test
  public void subscriberIsInvokedWithNewValue() throws Exception {
    Observer<String> observer = mock(Observer.class);
    Subscription subscription = preference.toObservable() //
            .subscribe(new TestObserver<String>(observer));
    InOrder inOrder = inOrder(observer);

    preference.set("baz");

    inOrder.verify(observer, times(1)).onNext("bar");
    inOrder.verify(observer, times(1)).onNext("baz");

    subscription.unsubscribe();
    TestUtils.verifyNoMoreInteractionsWithObserver(inOrder, observer);
  }
}
