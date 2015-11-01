package com.f2prateek.rx.preferences

import android.content.SharedPreferences

/** Create a boolean preference for {@code key}. Default is {@code false}. */
fun SharedPreferences.getBoolean(key: String): Preference<Boolean> {
  return RxSharedPreferencesCache.get(this).getBoolean(key)
}

/** Create a boolean preference for {@code key} with a default of {@code defaultValue}. */
fun SharedPreferences.getBoolean(key: String, defaultValue: Boolean): Preference<Boolean> {
  return RxSharedPreferencesCache.get(this).getBoolean(key, defaultValue)
}

/** Create an enum preference for {@code key}. Default is {@code null}. */
fun <T : Enum<T>> SharedPreferences.getEnum(key: String, enumClass: Class<T>): Preference<T> {
  return RxSharedPreferencesCache.get(this).getEnum(key, enumClass)
}

/** Create an enum preference for {@code key} with a default of {@code defaultValue}. */
fun <T : Enum<T>> SharedPreferences.getEnum(key: String, defaultValue: T,
    enumClass: Class<T>): Preference<T> {
  return RxSharedPreferencesCache.get(this).getEnum(key, defaultValue, enumClass)
}

/** Create a float preference for {@code key}. Default is {@code 0}. */
fun SharedPreferences.getFloat(key: String): Preference<Float> {
  return RxSharedPreferencesCache.get(this).getFloat(key)
}

/** Create a float preference for {@code key} with a default of {@code defaultValue}. */
fun SharedPreferences.getFloat(key: String, defaultValue: Float): Preference<Float> {
  return RxSharedPreferencesCache.get(this).getFloat(key, defaultValue)
}

/** Create an integer preference for {@code key}. Default is {@code 0}. */
fun SharedPreferences.getInteger(key: String): Preference<Int> {
  return RxSharedPreferencesCache.get(this).getInteger(key)
}

/** Create an integer preference for {@code key} with a default of {@code defaultValue}. */
fun SharedPreferences.getInteger(key: String, defaultValue: Int): Preference<Int> {
  return RxSharedPreferencesCache.get(this).getInteger(key, defaultValue)
}

/** Create a long preference for {@code key}. Default is {@code 0}. */
fun SharedPreferences.getLong(key: String): Preference<Long> {
  return RxSharedPreferencesCache.get(this).getLong(key)
}

/** Create a long preference for {@code key} with a default of {@code defaultValue}. */
fun SharedPreferences.getLong(key: String, defaultValue: Long): Preference<Long> {
  return RxSharedPreferencesCache.get(this).getLong(key, defaultValue)
}

/** Create a preference of type {@code T} for {@code key}. Default is {@code null}. */
fun <T> SharedPreferences.getObject(key: String, adapter: Preference.Adapter<T>): Preference<T> {
  return RxSharedPreferencesCache.get(this).getObject(key, adapter)
}

/** Create a preference for type {@code T} for {@code key} with a default of {@code defaultValue}. */
fun <T> SharedPreferences.getObject(key: String, defaultValue: T,
    adapter: Preference.Adapter<T>): Preference<T> {
  return RxSharedPreferencesCache.get(this).getObject(key, defaultValue, adapter)
}

/** Create a string preference for {@code key}. Default is {@code null}. */
fun SharedPreferences.getString(key: String): Preference<String> {
  return RxSharedPreferencesCache.get(this).getString(key)
}

/** Create a string preference for {@code key} with a default of {@code defaultValue}. */
fun SharedPreferences.getString(key: String, defaultValue: String): Preference<String> {
  return RxSharedPreferencesCache.get(this).getString(key, defaultValue)
}

/** Create a string set preference for {@code key}. Default is an empty set. */
fun SharedPreferences.getStringSet(key: String): Preference<Set<String>> {
  return RxSharedPreferencesCache.get(this).getStringSet(key)
}

/** Create a string set preference for {@code key} with a default of {@code defaultValue}. */
fun SharedPreferences.getStringSet(key: String,
    defaultValue: Set<String>): Preference<Set<String>> {
  return RxSharedPreferencesCache.get(this).getStringSet(key, defaultValue)
}
