package com.chentao.simple

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.github.chentao7v.span.image.SpanImageLoader
import me.chentao.simple.R

/**
 *
 * create by chentao on 2023-02-24.
 */
class GlideImageLoader : SpanImageLoader {

  override fun load(url: String?, callback: SpanImageLoader.Callback) {
    Glide.with(appContext)
      .asDrawable()
      .placeholder(R.drawable.mini_icon4)
      .load(url)
      .into(object : SimpleTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
          callback.onSuccess(resource)
        }

        override fun onLoadStarted(placeholder: Drawable?) {
          super.onLoadStarted(placeholder)
          if (placeholder != null) {
            callback.onSuccess(placeholder)
          }
        }
      })
  }
}