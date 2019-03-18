package com.f2prateek.rx.preferences2

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TestPreferenceTest {
    @Test
    fun testPreferenceKey() {
        val pref = createPref()

        assertThat(pref.key()).isEqualTo(KEY)

        pref.set("abcd")
        assertThat(pref.key())
                .describedAs("key name \"$KEY\" persists after value is changed")
                .isEqualTo(KEY)
    }

    @Test
    fun testSettingValue() {
        val pref = createPref()

        assertThat(pref.isSet).isFalse()
        assertThat(pref.get())
                .describedAs("default value \"$DEFAULT_VALUE\" is set initially")
                .isEqualTo(DEFAULT_VALUE)

        pref.set(NEW_VALUE)

        assertThat(pref.isSet).isTrue()
        assertThat(pref.get())
                .describedAs("new value \"$NEW_VALUE\" is set")
                .isEqualTo(NEW_VALUE)

        assertThat(pref.defaultValue())
                .describedAs("default value \"$DEFAULT_VALUE\" itself didn't change")
                .isEqualTo(DEFAULT_VALUE)

        pref.delete()
        assertThat(pref.isSet)
                .describedAs("value is not set")
                .isFalse()
        assertThat(pref.get())
                .describedAs("default value \"$DEFAULT_VALUE\" is set by default")
                .isEqualTo(DEFAULT_VALUE)

        pref.set(NEW_VALUE)
        assertThat(pref.isSet)
                .describedAs("value is set")
                .isTrue()
        assertThat(pref.get()).isEqualTo(NEW_VALUE)
    }

    @Test
    fun testPrefObservable() {
        val pref = createPref()
        val values = pref.asObservable().test()

        values.assertValue(DEFAULT_VALUE)

        pref.set(NEW_VALUE)
        values.assertValueCount(2)
                .assertValueAt(0, DEFAULT_VALUE)
                .assertValueAt(1, NEW_VALUE)

        // Setting the same value doesn't emit new event
        pref.set(NEW_VALUE)
        values.assertValueCount(2)

        val latestValues = pref.asObservable().test()
        // Only latest value is emitted
        latestValues.assertValue(NEW_VALUE)
    }

    @Test
    fun testPrefConsumer() {
        val pref = createPref()
        val consumer = pref.asConsumer()

        assertThat(pref.get()).isEqualTo(DEFAULT_VALUE)

        consumer.accept(NEW_VALUE)
        assertThat(pref.get())
                .describedAs("consumer sets accepted value")
                .isEqualTo(NEW_VALUE)
    }

    private fun createPref() = TestPreference(KEY, DEFAULT_VALUE)

    private companion object {
        const val KEY = "id"
        const val DEFAULT_VALUE = "abc"
        const val NEW_VALUE = "${DEFAULT_VALUE}d"
    }
}