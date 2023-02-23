package me.chentao.span.simple

import android.content.Context

fun dpToPx(context: Context, dp: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (dp*scale).toInt()
}