package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;

/**
 * create by chentao on 2023-02-27.
 */
public abstract class Engine {

  @NonNull
  protected final AsyncImageEngine asyncEngine;

  private boolean clickable;

  public Engine() {
    this.asyncEngine = new AsyncImageEngine();
  }

  public void setImageLoader(@NonNull SpanImageLoader loader) {
    this.asyncEngine.setImageLoader(loader);
  }

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
        IndexerProcessor.DynamicProxy proxy = new IndexerProcessor.DynamicProxy(c.getWidth(), c.getVerticalAlign());
        compose.addProcessor(proxy);
        asyncEngine.register(c.getUrl(), proxy);
      }
    }
    return compose;
  }

  public void inject(TextView textView) {
    Spannable spannable = end();
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

  public abstract Spannable end();

}
