package com.f2prateek.rx.preferences2.sample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.jakewharton.rxbinding4.widget.RxCompoundButton;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class SampleActivity extends Activity {

    @BindView(R.id.foo_1)
    CheckBox foo1Checkbox;
    @BindView(R.id.foo_2)
    CheckBox foo2Checkbox;
    @BindView(R.id.text_1)
    EditText foo1EditText;
    @BindView(R.id.text_2)
    EditText foo2EditText;
    Preference<Boolean> fooBoolPreference;
    Preference<String> fooTextPreference;
    CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Views
        setContentView(R.layout.sample_activity);
        ButterKnife.bind(this);

        // Preferences
        SharedPreferences preferences = getSharedPreferences(getDefaultSharedPreferencesName(this), MODE_PRIVATE);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);

        // foo
        fooBoolPreference = rxPreferences.getBoolean("fooBool");
        fooTextPreference = rxPreferences.getString("fooText");
    }

    @Override
    protected void onResume() {
        super.onResume();

        disposables = new CompositeDisposable();
        bindPreference(foo1Checkbox, fooBoolPreference);
        bindPreference(foo2Checkbox, fooBoolPreference);
        bindPreference(foo1EditText, fooTextPreference);
        bindPreference(foo2EditText, fooTextPreference);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.dispose();
    }

    void bindPreference(final CheckBox checkBox, Preference<Boolean> preference) {
        // Bind the preference to the checkbox.
        disposables.add(preference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkBox::setChecked));
        // Bind the checkbox to the preference.
        disposables.add(RxCompoundButton.checkedChanges(checkBox)
                .skip(1) // First emission is the original state.
                .subscribe(preference.asConsumer()));
    }

    void bindPreference(final EditText editText, Preference<String> preference) {
        disposables.add(preference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .filter(s -> !editText.isFocused())
                .subscribe(editText::setText));
        disposables.add(RxTextView.textChangeEvents(editText)
                .skip(1) // First emission is the original state.
                .debounce(500, TimeUnit.MILLISECONDS) // Filter out UI events that are emitted in quick succession.
                .map(e -> e.getText().toString())
                .subscribe(preference.asConsumer()));
    }

    private String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }
}
