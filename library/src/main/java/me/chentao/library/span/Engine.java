package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import me.chentao.library.span.image.AsyncImageEngine;
import me.chentao.library.span.image.SpanImageLoader;

/**
 * Span 处理引擎
 * <br>
 * create by chentao on 2023-02-27.
 */
public abstract class Engine {

  @NonNull
  protected final AsyncImageEngine asyncEngine = new AsyncImageEngine();

  private boolean clickable;

  /**
   * 设置异步图片加载器
   */
  public void setImageLoader(@NonNull SpanImageLoader loader) {
    this.asyncEngine.setImageLoader(loader);
  }

  /**
   * 将 {@link Config} 解析为 {@link IndexerProcessor}。
   */
  protected IndexerProcessor parse(@NonNull Config config) {
    IndexerProcessor.ComposeProcessor compose = new IndexerProcessor.ComposeProcessor();

    if (config instanceof Config.Default) {
      // 处理常见操作
      Config.Default c = (Config.Default) config;

      if (c.getListener() != null) {
        this.clickable = true;
        compose.addProcessor(new IndexerProcessor.Click(c.getColor(), c.getListener()));
      }

      if (c.getColor() != Config.NONE) {
        compose.addProcessor(new IndexerProcessor.Color(c.getColor()));
      }

      if (c.isBold()) {
        compose.addProcessor(new IndexerProcessor.Bold());
      }

      if (c.getSize() != Config.NONE) {
        compose.addProcessor(new IndexerProcessor.Size(c.getSize()));
      }

    } else if (config instanceof Config.Image) {
      // 处理图片
      Config.Image c = (Config.Image) config;
      if (c.getListener() != null) {
        this.clickable = true;
        compose.addProcessor(new IndexerProcessor.Click(Config.NONE, c.getListener()));
      }

      if (c.getBitmap() != null) {
        compose.addProcessor(new IndexerProcessor.Image(c.getBitmap(), c.getWidth(), c.getVerticalAlign()));
      } else if (c.getDrawable() != null) {
        compose.addProcessor(new IndexerProcessor.Image(c.getDrawable(), c.getWidth(), c.getVerticalAlign()));
      } else if (c.getUrl() != null) {
        IndexerProcessor.AsyncProxy proxy = new IndexerProcessor.AsyncProxy(c.getWidth(), c.getVerticalAlign());
        compose.addProcessor(proxy);
        asyncEngine.register(c.getUrl(), proxy);
      }
    }
    return compose;
  }

  /**
   * 返回处理过的文本 {@link Spannable}。
   */
  public abstract Spannable execute();

  /**
   * 将 Span 注入到 TextView 中。
   */
  public void inject(TextView textView) {
    Spannable spannable = execute();
    boolean async = asyncEngine.containsAsync();
    Spans.inject(textView, spannable, clickable, async);
    if (async) {
      Spannable text = (Spannable) textView.getText();
      asyncEngine.loadAllImages(text, new AsyncImageEngine.Listener() {
        @Override
        public void onComplete() {
          textView.invalidate();
        }
      });
    }
  }

}
