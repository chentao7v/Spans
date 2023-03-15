package com.chentao.simple

import android.graphics.drawable.Drawable


val Number.dp: Int
  get() {
    val scale = appContext.resources.displayMetrics.density
    return (this.toFloat() * scale).toInt()
  }

fun colorRes(colorRes: Int): Int {
  return appContext.resources.getColor(colorRes)
}

fun drawableRes(drawableRes: Int): Drawable {
  return appContext.resources.getDrawable(drawableRes)
}