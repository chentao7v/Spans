package io.github.chentao7v.span.image;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 图片加载器
 * <br>
 * create by chentao on 2023-02-24.
 */
public interface SpanImageLoader {

  void load(@Nullable String url, @NonNull Callback callback);

  interface Callback {

    void onSuccess(@NonNull Drawable resource);

    void onError(@Nullable Drawable error);

  }

}
