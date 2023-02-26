package me.chentao.library.span;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * create by chentao on 2023-02-24.
 */
public interface SpanImageLoader {

  void load(@Nullable String url, @Nullable Callback callback);

  interface Callback {

    void onSuccess(@NonNull Drawable resource);

    void onError(@Nullable Drawable error);

  }

}
