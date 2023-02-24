package me.chentao.span.simple

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import me.chentao.library.span.SpanImageLoader
import me.chentao.span.appContext

/**
 *
 * create by chentao on 2023-02-24.
 */
class GlideImageLoader : SpanImageLoader {

  override fun load(url: String?, callback: SpanImageLoader.Callback?) {
    Glide.with(appContext)
      .asBitmap()
      .load(url)
      .into(object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
          callback?.onSuccess(resource)
        }
      })
  }

}