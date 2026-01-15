package com.example.androidmvvm.core.extension

/**
 * Returns second element.
 * @throws [NoSuchElementException] if the list is empty or just has an element
 */
fun <T> List<T>.second(): T {
    if (size < 2) {
        throw NoSuchElementException(if (isEmpty()) "List is empty." else "List just has an element")
    }
    return this[1]
}

/**
 * Returns second element.
 * @return null if the list is empty or just has an element
 */
fun <T> List<T>.secondOrNull(): T? {
    return if (size >= 2) this[1] else null
}