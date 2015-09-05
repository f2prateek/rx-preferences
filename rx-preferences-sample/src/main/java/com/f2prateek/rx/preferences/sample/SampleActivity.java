package com.f2prateek.rx.preferences.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SampleActivity extends Activity {

  @InjectView(R.id.foo_1) CheckBox foo1Checkbox;
  @InjectView(R.id.foo_2) CheckBox foo2Checkbox;
  Preference<Boolean> fooPreference;
  CompositeSubscription subscriptions;

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
  }

  @Override protected void onResume() {
    super.onResume();

    subscriptions = new CompositeSubscription();
    bindPreference(foo1Checkbox, fooPreference);
    bindPreference(foo2Checkbox, fooPreference);
  }

  @Override protected void onPause() {
    super.onPause();
    subscriptions.unsubscribe();
  }

  void bindPreference(CheckBox checkBox, Preference<Boolean> preference) {
    // Bind the preference to the checkbox.
    subscriptions.add(preference.asObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(RxCompoundButton.checked(checkBox)));
    // Bind the checkbox to the preference.
    subscriptions.add(RxCompoundButton.checkedChanges(checkBox)
        .skip(1)
        .subscribe(preference.asAction()));
  }
}
