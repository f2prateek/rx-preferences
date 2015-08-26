package com.f2prateek.rx.preferences;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Date;

final class DateAdapter implements Preference.Adapter<Date> {
  static final DateAdapter INSTANCE = new DateAdapter();

  @Override public Date get(@NonNull String key, @NonNull SharedPreferences preferences) {
    return new Date(preferences.getLong(key, 0L));
  }

  @Override public void set(@NonNull String key, @NonNull Date value,
      @NonNull SharedPreferences.Editor editor) {
    editor.putLong(key, value.getTime());
  }
}