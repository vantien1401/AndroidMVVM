package com.example.androidmvvm.core.listener

import android.view.View

class OnSingleClickListener(
    private val onClickListener: View.OnClickListener,
    private val intervalMs: Long = 250L
) : View.OnClickListener {
    @Volatile
    private var lastClickTime = 0L

    override fun onClick(view: View?) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime >= intervalMs) {
            lastClickTime = now
            onClickListener.onClick(view)
        }
    }
}