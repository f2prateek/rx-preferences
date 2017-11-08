package com.f2prateek.rx.preferences2.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SampleActivity extends Activity {

  @BindView(R.id.foo_1) CheckBox foo1Checkbox;
  @BindView(R.id.foo_2) CheckBox foo2Checkbox;
  @BindView(R.id.text) EditText fooEditText;
  Preference<Boolean> fooPreference;
  Preference<String> fooTextPreference;
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
    fooTextPreference = rxPreferences.getString("fooText");
  }

  @Override protected void onResume() {
    super.onResume();

    disposables = new CompositeDisposable();
    bindPreference(foo1Checkbox, fooPreference);
    bindPreference(foo2Checkbox, fooPreference);
    bindPreference(fooEditText, fooTextPreference);
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

  void bindPreference(final EditText editText, Preference<String> preference) {
    disposables.add(preference.asObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(t -> Toast.makeText(this, t, Toast.LENGTH_SHORT).show()));
    disposables.add(RxJavaInterop.toV2Observable(RxTextView.textChangeEvents(editText))
            .skip(1) // First emission is the original state.
            .debounce(500, TimeUnit.MILLISECONDS) // Filter out UI events that are emitted in quick succession.
            .map(e -> e.text().toString())
            .subscribe(preference.asConsumer()));
  }
}
