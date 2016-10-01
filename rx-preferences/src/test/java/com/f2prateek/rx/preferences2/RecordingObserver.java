/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.f2prateek.rx.preferences2;

import io.reactivex.Notification;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/** A test {@link Observer} and JUnit rule which guarantees all events are asserted. */
final class RecordingObserver<T> implements Observer<T> {
  private final Deque<Notification<T>> events = new ArrayDeque<>();

  private RecordingObserver() {
  }

  @Override public void onSubscribe(Disposable disposable) {
  }

  @Override public void onNext(T value) {
    events.add(Notification.createOnNext(value));
  }

  @Override public void onComplete() {
    events.add(Notification.<T>createOnComplete());
  }

  @Override public void onError(Throwable e) {
    events.add(Notification.<T>createOnError(e));
  }

  private Notification<T> takeNotification() {
    Notification<T> notification = events.pollFirst();
    if (notification == null) {
      throw new AssertionError("No event found!");
    }
    return notification;
  }

  public T takeValue() {
    Notification<T> notification = takeNotification();
    assertThat(notification.isOnNext()).isTrue();
    return notification.getValue();
  }

  public RecordingObserver<T> assertValue(T value) {
    assertThat(takeValue()).isEqualTo(value);
    return this;
  }

  public void assertNoEvents() {
    assertThat(events).isEmpty();
  }

  public static final class Rule implements TestRule {
    final List<RecordingObserver<?>> subscribers = new ArrayList<>();

    public <T> RecordingObserver<T> create() {
      RecordingObserver<T> subscriber = new RecordingObserver<>();
      subscribers.add(subscriber);
      return subscriber;
    }

    @Override public Statement apply(final Statement base, Description description) {
      return new Statement() {
        @Override public void evaluate() throws Throwable {
          base.evaluate();
          for (RecordingObserver<?> subscriber : subscribers) {
            subscriber.assertNoEvents();
          }
        }
      };
    }
  }
}