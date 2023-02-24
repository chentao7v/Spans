package me.chentao.span.simple

import android.graphics.drawable.Drawable
import me.chentao.span.appContext


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