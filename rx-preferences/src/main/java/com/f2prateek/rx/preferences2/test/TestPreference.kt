package com.f2prateek.rx.preferences2.test

import com.f2prateek.rx.preferences2.Preference
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

/**
 * In-memory implementation of [Preference] that can be used for testing purposes. Only use this
 * if your code under test depends on the changes in the preferences. Otherwise, if you need to
 * pass the preference as a dependency and don't care about its state, simply mock the preference
 * using [Preference] interface.
 */
class TestPreference<T>(private val key: String, private val defaultValue: T) : Preference<T> {
    /**
     * Baking field for preference value. If set to `null` then it indicates that the preference
     * is unset. [value] will never be set to [defaultValue] in that case.
     *
     * @see [isSet].
     * @see [get].
     */
    private var value: T? = null
    /**
     * Allows observing changes on this preference, starts with [defaultValue].
     *
     * @see [asObservable].
     */
    private val values = BehaviorSubject.createDefault<T>(defaultValue)

    override fun key(): String = key

    override fun defaultValue(): T = defaultValue

    override fun get(): T = value ?: defaultValue

    override fun set(value: T) {
        this.value = value
        values.onNext(value)
    }

    override fun isSet(): Boolean = value != null

    override fun delete() {
        this.value = null
        values.onNext(defaultValue)
    }

    override fun asObservable(): Observable<T> = values.distinctUntilChanged()

    override fun asConsumer(): Consumer<in T> = Consumer { set(it) }
}
