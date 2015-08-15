package com.f2prateek.rx.preferences.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.rx.android.schedulers.AndroidSchedulers;
import rx.Observer;
import rx.android.preferences.Preference;
import rx.android.preferences.RxSharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.Toast.LENGTH_SHORT;

public class SampleActivity extends Activity {

  Preference<Boolean> fooPreference;

  @InjectView(R.id.foo_value) TextView fooValue;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Views
    setContentView(R.layout.sample_activity);
    ButterKnife.inject(this);

    // Preferences
    SharedPreferences preferences = getDefaultSharedPreferences(this);
    RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);

    // foo
    fooPreference = rxPreferences.getBoolean("foo");
    fooPreference.asObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Boolean>() {
          @Override public void onNext(Boolean value) {
            fooValue.setText(String.valueOf(value));
          }

          @Override public void onCompleted() {
            // Never invoked
          }

          @Override public void onError(Throwable e) {
            throw new RuntimeException(e);
          }
        });
  }

  @OnClick(R.id.foo) void greetingClicked() {
    fooPreference.set(!fooPreference.get());
    Toast.makeText(this, "Foo preference updated!", LENGTH_SHORT).show();
  }
}
