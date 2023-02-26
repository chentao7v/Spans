package me.chentao.library.span;

import android.graphics.Bitmap;
import android.text.Spannable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对异步图片的处理。
 * <br》
 * create by chentao on 2023-02-26.
 */
public final class AsyncImageSpanEngine {

  private final List<String> urls = new LinkedList<>();
  private final List<IndexerProcessor.DynamicProxy> processors = new LinkedList<>();
  private final AtomicInteger counter = new AtomicInteger(0);

  @Nullable
  private SpanImageLoader loader;

  public void register(@NonNull String url, @NonNull IndexerProcessor.DynamicProxy processor) {
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
      IndexerProcessor.DynamicProxy proxy = processors.get(i);

      if (loader == null) {
        throw new NullPointerException("call #load(SpanImageLoader) first !! ");
      }

      loader.load(url, new SpanImageLoader.Callback() {
        @Override
        public void onSuccess(@NonNull Bitmap resource) {
          update(spannable, resource, proxy, listener);
        }

        @Override
        public void onError(@Nullable Bitmap error) {
          update(spannable, error, proxy, listener);
        }
      });
    }
  }

  private void update(@NonNull Spannable spannable, @Nullable Bitmap resource, IndexerProcessor.DynamicProxy proxy, @NonNull Listener listener) {
    if (resource != null) {
      IndexerProcessor.Image image = new IndexerProcessor.Image(
        resource,
        proxy.getSize(),
        AlignImageSpan.VERTICAL_ALIGN_CENTER);
      proxy.update(spannable, image);
    }

    counter.incrementAndGet();

    // 标识处理完成
    if (counter.get() == urls.size()) {
      listener.onFinish();
      destroy();
    }

  }

  public void setImageLoader(SpanImageLoader loader) {
    this.loader = loader;
  }

  public boolean containsAsync() {
    return !urls.isEmpty();
  }

  public void destroy() {
    urls.clear();
    processors.clear();
  }

  public interface Listener {
    void onFinish();
  }

}
