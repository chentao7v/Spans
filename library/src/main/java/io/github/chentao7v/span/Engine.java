package io.github.chentao7v.span;

import android.graphics.Color;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.annotation.NonNull;
import io.github.chentao7v.span.image.AsyncImageEngine;
import io.github.chentao7v.span.image.SpanImageLoader;

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
    Processor.Compose compose = new Processor.Compose();

    if (config instanceof Config.Text) {
      // 处理常见操作
      Config.Text c = (Config.Text) config;

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

      if (c.getBackgroundColor() != Config.NONE) {
        compose.addProcessor(new Processor.BackgroundColor(c.getBackgroundColor()));
      }

      if (c.isUnderline()) {
        compose.addProcessor(new Processor.Underline());
      }

      if (c.isStrikethrough()) {
        compose.addProcessor(new Processor.Strikethrough());
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
    } else {
      throw new IllegalArgumentException("unsupported config : " + config.getClass());
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
    inject(textView, spannable, clickable, async);
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

  private static void inject(@NonNull TextView textView, Spannable spannable, boolean clickable, boolean async) {
    if (async) {
      textView.setText(spannable, TextView.BufferType.SPANNABLE);
    } else {
      textView.setText(spannable);
    }
    if (clickable) {
      markClickable(textView);
    }
  }

  private static void markClickable(@NonNull TextView textView) {
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    textView.setLongClickable(false);
    textView.setHighlightColor(Color.TRANSPARENT);
  }

}
