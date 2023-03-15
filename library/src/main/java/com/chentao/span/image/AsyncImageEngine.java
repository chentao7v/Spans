package com.chentao.span.image;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import com.chentao.span.Processor;

/**
 * 异步图片处理引擎。
 * <br>
 * create by chentao on 2023-02-26.
 */
public final class AsyncImageEngine {

  @NonNull
  private final List<String> urls = new LinkedList<>();
  @NonNull
  private final List<Processor.AsyncProxy> processors = new LinkedList<>();

  @Nullable
  private SpanImageLoader loader;

  public void register(@NonNull String url, @NonNull Processor.AsyncProxy processor) {
    urls.add(url);
    processors.add(processor);
  }

  public void unregister(@NonNull String url) {
    int index = urls.indexOf(url);
    if (index != -1) {
      urls.remove(index);
      processors.remove(index);
    }
  }

  public void loadAllImages(@NonNull Spannable spannable, @NonNull Listener listener) {
    int size = urls.size();

    for (int i = 0; i < size; i++) {
      String url = urls.get(i);
      Processor.AsyncProxy proxy = processors.get(i);

      if (loader == null) {
        throw new NullPointerException("call #load(SpanImageLoader) first !! ");
      }

      loader.load(url, new SpanImageLoader.Callback() {
        @Override
        public void onSuccess(@NonNull Drawable resource) {
          update(spannable, resource, proxy, listener);
        }

        @Override
        public void onError(@Nullable Drawable error) {
          update(spannable, error, proxy, listener);
        }
      });
    }
  }

  /**
   * 图片下载完成后，再使用 ImageSpan 去更新 TextView
   */
  private void update(@NonNull Spannable spannable, @Nullable Drawable resource, Processor.AsyncProxy proxy, @NonNull Listener listener) {
    if (resource != null) {
      Processor.Image image = new Processor.Image(
        resource,
        proxy.size(),
        proxy.verticalAlign());
      proxy.update(spannable, image);
    }

    // 标识处理完成
    listener.onComplete();
  }

  public void setImageLoader(SpanImageLoader loader) {
    this.loader = loader;
  }

  /**
   * 是否包含异步图片
   */
  public boolean containsAsync() {
    return !urls.isEmpty();
  }

  public void destroy() {
    urls.clear();
    processors.clear();
  }

  public interface Listener {
    void onComplete();
  }

}
