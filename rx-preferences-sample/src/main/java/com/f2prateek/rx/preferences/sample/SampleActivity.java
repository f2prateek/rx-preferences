package com.f2prateek.rx.preferences.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.rx.android.schedulers.AndroidSchedulers;
import rx.Observer;
import rx.android.preferences.BooleanPreference;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.Toast.LENGTH_SHORT;

public class SampleActivity extends Activity {

  BooleanPreference fooPreference;

  @InjectView(R.id.foo_value) TextView fooValue;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Views
    setContentView(R.layout.sample_activity);
    ButterKnife.inject(this);

    // Preferences
    SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);

    // foo
    fooPreference = new BooleanPreference(sharedPreferences, "foo");
    fooPreference.toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Boolean>() {
          @Override public void onCompleted() {
            // Never invoked
          }

          @Override public void onError(Throwable e) {
            throw new RuntimeException(e);
          }

          @Override public void onNext(Boolean value) {
            fooValue.setText(String.valueOf(value));
          }
        });
  }

  @OnClick({ R.id.foo }) public void greetingClicked(Button button) {
    fooPreference.set(!fooPreference.get());
    Toast.makeText(this, "Foo preference updated!", LENGTH_SHORT).show();
  }
}
