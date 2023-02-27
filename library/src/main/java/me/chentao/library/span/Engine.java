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
   * 将 {@link Config} 解析为 {@link Processor}。
   */
  protected Processor parse(@NonNull Config config) {
    Processor.ComposeProcessor compose = new Processor.ComposeProcessor();

    if (config instanceof Config.Default) {
      // 处理常见操作
      Config.Default c = (Config.Default) config;

      if (c.getListener() != null) {
        this.clickable = true;
        compose.addProcessor(new Processor.Click(c.getColor(), c.getListener()));
      }

      if (c.getColor() != Config.NONE) {
        compose.addProcessor(new Processor.Color(c.getColor()));
      }

      if (c.isBold()) {
        compose.addProcessor(new Processor.Bold());
      }

      if (c.getSize() != Config.NONE) {
        compose.addProcessor(new Processor.Size(c.getSize()));
      }

    } else if (config instanceof Config.Image) {
      // 处理图片
      Config.Image c = (Config.Image) config;
      if (c.getListener() != null) {
        this.clickable = true;
        compose.addProcessor(new Processor.Click(Config.NONE, c.getListener()));
      }

      if (c.getBitmap() != null) {
        compose.addProcessor(new Processor.Image(c.getBitmap(), c.getWidth(), c.getVerticalAlign()));
      } else if (c.getDrawable() != null) {
        compose.addProcessor(new Processor.Image(c.getDrawable(), c.getWidth(), c.getVerticalAlign()));
      } else if (c.getUrl() != null) {
        Processor.AsyncProxy proxy = new Processor.AsyncProxy(c.getWidth(), c.getVerticalAlign());
        compose.addProcessor(proxy);
        asyncEngine.register(c.getUrl(), proxy);
      }
    }
    return compose;
  }

  /**
   * 返回处理过的文本 {@link Spannable}。
   */
  @NonNull
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
