package rx.android.preferences;

import org.mockito.InOrder;
import org.mockito.Matchers;
import rx.Observer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;

public final class TestUtils {
  private TestUtils() {
    throw new AssertionError("no instances allowed");
  }

  public static <T> void verifyNoMoreInteractionsWithObserver(InOrder inOrder,
      Observer<T> observer) {
    inOrder.verify(observer, never()).onNext(Matchers.<T>any());
    inOrder.verify(observer, never()).onError(any(Throwable.class));
    inOrder.verify(observer, never()).onCompleted();
  }
}
