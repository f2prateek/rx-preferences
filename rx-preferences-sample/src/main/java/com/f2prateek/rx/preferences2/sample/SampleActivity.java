package com.f2prateek.rx.preferences2.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SampleActivity extends Activity {

  @Bind(R.id.foo_1) CheckBox foo1Checkbox;
  @Bind(R.id.foo_2) CheckBox foo2Checkbox;
  Preference<Boolean> fooPreference;
  CompositeDisposable disposables;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Views
    setContentView(R.layout.sample_activity);
    ButterKnife.bind(this);

    // Preferences
    SharedPreferences preferences = getDefaultSharedPreferences(this);
    RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);

    // foo
    fooPreference = rxPreferences.getBoolean("foo");
  }

  @Override protected void onResume() {
    super.onResume();

    disposables = new CompositeDisposable();
    bindPreference(foo1Checkbox, fooPreference);
    bindPreference(foo2Checkbox, fooPreference);
  }

  @Override protected void onPause() {
    super.onPause();
    disposables.dispose();
  }

  void bindPreference(final CheckBox checkBox, Preference<Boolean> preference) {
    // Bind the preference to the checkbox.
    disposables.add(preference.asObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(checkBox::setChecked));
    // Bind the checkbox to the preference.
    disposables.add(RxJavaInterop.toV2Observable(RxCompoundButton.checkedChanges(checkBox))
        .skip(1) // First emission is the original state.
        .subscribe(preference.asConsumer()));
  }
}
